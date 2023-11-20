package AppServer;

import Dao.IAnimalDao;
import Dto.AnimalCreationDTO;
import animal.AddAnimalRequest;
import animal.AnimalServiceGrpc;
import animal.EmptyResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.sql.SQLException;

public class AnimalServiceImpl extends AnimalServiceGrpc.AnimalServiceImplBase {

    private IAnimalDao dao;

    public AnimalServiceImpl(IAnimalDao dao) {
        this.dao = dao;
    }

    @Override
    public void addAnimal(AddAnimalRequest request, StreamObserver<EmptyResponse> responseObserver) {
        String species = request.getSpecies();
        double weight = request.getWeight();

        AnimalCreationDTO dto = new AnimalCreationDTO(species, weight);
        try {
            dao.addAnimal(dto);
        } catch (SQLException e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).withCause(new RuntimeException(e.getCause())).asRuntimeException());
        }
        EmptyResponse response = EmptyResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
