package Logic;

import Dao.IAnimalPartDao;
import Domain.Animal;
import Domain.AnimalPart;
import Dto.AnimalPartCreationDTO;
import animal.AddAnimalRequest;
import animal.AnimalServiceGrpc;
import animalPart.AddAnimalPartRequest;
import animalPart.AnimalPartResponse;
import animalPart.AnimalPartServiceGrpc;
import animalPart.GetAnimalPartByRegNoRequest;
import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AnimalPartLogic implements IAnimalPartLogic {
    private final Gson gson;

    public AnimalPartLogic() {
        this.gson = new Gson();
    }

    @Override
    public void addAnimalPart(AnimalPartCreationDTO dto) throws Exception{
        if(dto.getAnimalRegNo() <= 0)
            throw new Exception("Animal Registration Number has to be declared.");
        if(dto.getPartWeight() <= 0)
            throw new Exception("Weight has to be a positive number.");
        if(dto.getPartType() == null || dto.getPartType().isEmpty())
            throw new Exception("Part type has to be declared.");

        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

            AnimalPartServiceGrpc.AnimalPartServiceBlockingStub stub;
            stub = AnimalPartServiceGrpc.newBlockingStub(channel);
            stub.addAnimalPart(AddAnimalPartRequest.newBuilder().setAnimalRegNo(dto.getAnimalRegNo()).setPartWeight(dto.getPartWeight()).setPartType(dto.getPartType()).build());
            channel.shutdown();
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public AnimalPart getAnimalPartByRegNo(int regNo) throws Exception {
        if(regNo <= 0) throw new Exception("Registration number has to be a positive number.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

            AnimalPartServiceGrpc.AnimalPartServiceBlockingStub stub;
            stub = AnimalPartServiceGrpc.newBlockingStub(channel);
            AnimalPartResponse response = stub.getAnimalPartByRegNo(GetAnimalPartByRegNoRequest.newBuilder().setRegNo(regNo).build());
            channel.shutdown();

            Animal animal = gson.fromJson(response.getAnimal(), Animal.class);
            AnimalPart animalPart = new AnimalPart(animal, response.getPartWeight(), response.getPartRegNo(), response.getPartType());
            return animalPart;
        }
        catch (StatusRuntimeException ex)
        {
            try{
                for (AnimalPart animalPart : animalParts) {
                    if (animalPart.getPartRegNo() == regNo) {
                        System.out.println(animalPart);
                        return animalPart;
                    }
                }
                return null;
            }
            catch (Exception exception){
                throw new Exception(exception.getMessage());
            }
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
