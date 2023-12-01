package Logic;

import Dao.IAnimalDao;
import Dto.AnimalCreationDTO;
import animal.AddAnimalRequest;
import animal.AnimalServiceGrpc;
import animal.EmptyResponse;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;

public class AnimalLogic implements IAnimalLogic {
    public AnimalLogic(){}

    @Override
    public void addAnimal(AnimalCreationDTO dto) throws Exception{
        if(dto.getSpecies() == null || dto.getSpecies().equals(""))
            throw new Exception("Species have to be declared.");
        if(dto.getWeight() <= 0)
            throw new Exception("Weight has to be a positive number.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            AnimalServiceGrpc.AnimalServiceBlockingStub stub;
            stub = AnimalServiceGrpc.newBlockingStub(channel);
            stub.addAnimal(AddAnimalRequest.newBuilder().setSpecies(dto.getSpecies()).setWeight(dto.getWeight()).build());
            channel.shutdown();
        }
        catch (Exception e)
        {
            e.printStackTrace();
           throw new Exception(e.getMessage());
        }
    }


}
