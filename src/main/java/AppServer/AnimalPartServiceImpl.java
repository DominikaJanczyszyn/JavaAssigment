package AppServer;

import Dao.IAnimalPartDao;
import Domain.AnimalPart;
import Dto.AnimalPartCreationDTO;
import animalPart.*;
import com.google.gson.Gson;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class AnimalPartServiceImpl extends AnimalPartServiceGrpc.AnimalPartServiceImplBase
{
  private IAnimalPartDao dao;
  private Gson gson;

  public AnimalPartServiceImpl(IAnimalPartDao dao)
  {
    this.dao = dao;
    this.gson = new Gson();
  }

  @Override public void addAnimalPart(AddAnimalPartRequest request,
      StreamObserver<EmptyResponse> responseObserver)
  {
    int animalRegNo = request.getAnimalRegNo();
    double partWeight = request.getPartWeight();
    String partType = request.getPartType();

    AnimalPartCreationDTO dto = new AnimalPartCreationDTO(animalRegNo, partWeight, partType);

    try
    {
      dao.addAnimalPart(dto);
    }
    catch (Exception e)
    {
      responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).withCause(new RuntimeException(e.getCause())).asRuntimeException());
    }

    animalPart.EmptyResponse response = animalPart.EmptyResponse.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getAnimalPartByRegNo(GetAnimalPartByRegNoRequest request, StreamObserver<AnimalPartResponse> responseObserver)
  {
    int animalPartRegNo = request.getRegNo();
    AnimalPart animalPartObj = null;
    try
    {
      animalPartObj = dao.getAnimalPartByRegNo(animalPartRegNo);
    }
    catch (Exception e)
    {
      responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).withCause(new RuntimeException(e.getCause())).asRuntimeException());
    }

    animalPart.AnimalPartResponse response = animalPart.AnimalPartResponse.newBuilder().build();
    if (animalPartObj != null)
    {
       response = animalPart.AnimalPartResponse.newBuilder().setPartRegNo(animalPartObj.getPartRegNo()).setAnimal(gson.toJson(animalPartObj.getAnimal())).setPartWeight(animalPartObj.getPartWeight()).setPartType(animalPartObj.getPartType()).build();
    }
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
