package Logic;

import Dto.TrayCreationDto;
import animalPart.AddAnimalPartRequest;
import animalPart.AnimalPartServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import tray.AddAnimalPartToTrayRequest;
import tray.AddTrayRequest;
import tray.TrayServiceGrpc;

public class TrayLogic implements ITrayLogic {


    public TrayLogic() {}

    @Override
    public void addTray(TrayCreationDto dto) throws Exception {
        if(dto.getWeightCapacity() <= 0)
            throw new Exception("Weight capacity has to be a positive number.");
        if(dto.getPartType() == null || dto.getPartType().isEmpty())
            throw new Exception("Part has to be declared.");

        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

            TrayServiceGrpc.TrayServiceBlockingStub stub;
            stub = TrayServiceGrpc.newBlockingStub(channel);
            stub.addTray(AddTrayRequest.newBuilder().setWeightCapacity(dto.getWeightCapacity()).setPartType(dto.getPartType()).build());
            channel.shutdown();
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addAnimalPartToTray(int trayId, int animalPartRegNo) throws Exception {
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

            TrayServiceGrpc.TrayServiceBlockingStub stub;
            stub = TrayServiceGrpc.newBlockingStub(channel);
            stub.addAnimalPartToTray(AddAnimalPartToTrayRequest.newBuilder().setTrayId(trayId).setAnimalPartRegNo(animalPartRegNo).build());
            channel.shutdown();
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
