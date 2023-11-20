package AppServer;

import Dao.IAnimalPartDao;
import Dao.ITrayDao;
import Domain.AnimalPart;
import Domain.Tray;
import Dto.AnimalPartCreationDTO;
import Dto.TrayCreationDto;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import tray.AddAnimalPartToTrayRequest;
import tray.AddTrayRequest;
import tray.EmptyResponse;
import tray.TrayServiceGrpc;

public class TrayServiceImpl extends TrayServiceGrpc.TrayServiceImplBase
{
  private ITrayDao trayDao;
  private IAnimalPartDao animalPartDao;

  public TrayServiceImpl(ITrayDao trayDao, IAnimalPartDao animalPartDao)
  {
    this.trayDao = trayDao;
    this.animalPartDao = animalPartDao;
  }

  @Override public void addTray(AddTrayRequest request,
      StreamObserver<EmptyResponse> responseObserver)
  {
    double weightCapacity = request.getWeightCapacity();
    String partType = request.getPartType();

    TrayCreationDto dto = new TrayCreationDto(weightCapacity, partType);

    try
    {
      trayDao.addTray(dto);
      tray.EmptyResponse response = tray.EmptyResponse.newBuilder().build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).withCause(new RuntimeException(e.getCause())).asRuntimeException());
    }
  }

  @Override public void addAnimalPartToTray(AddAnimalPartToTrayRequest request,
      StreamObserver<EmptyResponse> responseObserver)
  {
    int trayId = request.getTrayId();
    int animalPartRegNo = request.getAnimalPartRegNo();
    boolean exceptionCatched = false;

    try
    {
      Tray tray = trayDao.getTrayById(trayId);
      AnimalPart animalPart = animalPartDao.getAnimalPartByRegNo(animalPartRegNo);
      if (!tray.getPartType().equals(animalPart.getPartType()))
      {
        exceptionCatched = true;
        responseObserver.onError(Status.INTERNAL.withDescription(
            "The type of the animal part does not match the tray type.").withCause(new RuntimeException(
            "The type of the animal part does not match the tray type.")).asRuntimeException());
      }

      trayDao.addAnimalPartToTray(trayId, animalPartRegNo);
      tray.EmptyResponse response = EmptyResponse.newBuilder().build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (Exception e)
    {
      if (!exceptionCatched)
        responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).withCause(new RuntimeException(e.getCause())).asRuntimeException());
    }
  }
}
