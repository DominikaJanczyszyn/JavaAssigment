package Logic;
import com.google.gson.reflect.TypeToken;
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
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.ArrayList;

public class AnimalPartLogic implements IAnimalPartLogic {
    private static final String QUEUE = "AnimalPartQueue";
    private static final String ANIMAL_PART = "AnimalPart";
    private ArrayList<AnimalPart> animalParts;

    private final Gson gson;

    public AnimalPartLogic() {
        this.gson = new Gson();
        this.animalParts = new ArrayList<>();
        try {
            run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private synchronized void run() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(QUEUE, "topic");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, QUEUE, ANIMAL_PART);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            TypeToken<ArrayList<AnimalPart>> typeToken = new TypeToken<ArrayList<AnimalPart>>() {};
            this.animalParts = gson.fromJson(message, typeToken.getType());
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
