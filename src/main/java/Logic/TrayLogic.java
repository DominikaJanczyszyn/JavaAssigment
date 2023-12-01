package Logic;

import Domain.Package;
import Domain.Tray;
import Dto.TrayCreationDto;
import animalPart.AddAnimalPartRequest;
import animalPart.AnimalPartServiceGrpc;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import tray.AddAnimalPartToTrayRequest;
import tray.AddTrayRequest;
import tray.TrayServiceGrpc;

import java.util.ArrayList;

public class TrayLogic implements ITrayLogic {
    private static final String QUEUE = "TrayQueue";
    private static final String TRAY = "Tray";
    private ArrayList<Tray> trays;
    private final Gson gson;

    public TrayLogic() {
        this.trays = new ArrayList<>();
        this.gson = new Gson();
        try {
            run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
            e.printStackTrace();
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

        channel.queueBind(queueName, QUEUE, TRAY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            TypeToken<ArrayList<Tray>> typeToken = new TypeToken<ArrayList<Tray>>() {};
            this.trays = gson.fromJson(message, typeToken.getType());
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
