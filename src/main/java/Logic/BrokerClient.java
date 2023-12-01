package Logic;

import Domain.AnimalPart;
import Domain.HalfAnimal;
import Domain.Package;
import Domain.Tray;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.ArrayList;

public class BrokerClient
{
    private static final String QUEUE1 = "AnimalPartQueue";
    private static final String QUEUE2 = "TrayQueue";
    private static final String QUEUE3 = "HalfAnimalQueue";
    private static final String QUEUE4 = "PackageQueue";

    private static final String ANIMAL_PART = "AnimalPart";
    private static final String TRAY = "Tray";
    private static final String HALF_ANIMAL = "HalfAnimal";
    private static final String PACKAGE = "Package";

    private final Gson gson;

    private ArrayList<AnimalPart> animalParts;
    private ArrayList<Tray> trays;
    private ArrayList<HalfAnimal> halfAnimals;
    private ArrayList<Package> packages;

    public BrokerClient()
    {
        this.gson = new Gson();
        this.animalParts = new ArrayList<>();
        this.trays = new ArrayList<>();
        this.halfAnimals = new ArrayList<>();
        this.packages = new ArrayList<>();
    }

    public synchronized static void run() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(QUEUE1, "topic");
        String queueName1 = channel.queueDeclare().getQueue();
        channel.exchangeDeclare(QUEUE2, "topic");
        String queueName2 = channel.queueDeclare().getQueue();
        channel.exchangeDeclare(QUEUE3,"topic");
        String queueName3 = channel.queueDeclare().getQueue();
        channel.exchangeDeclare(QUEUE4, "topic");
        String queueName4 = channel.queueDeclare().getQueue();

        channel.queueBind(queueName1, QUEUE1, ANIMAL_PART);
        channel.queueBind(queueName2, QUEUE2, TRAY);
        channel.queueBind(queueName3, QUEUE3, HALF_ANIMAL);
        channel.queueBind(queueName4, QUEUE4, PACKAGE);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");

        };
        channel.basicConsume(queueName1, true, deliverCallback, consumerTag -> { });
        channel.basicConsume(queueName2, true, deliverCallback, consumerTag -> { });
        channel.basicConsume(queueName3, true, deliverCallback, consumerTag -> { });
        channel.basicConsume(queueName4, true, deliverCallback, consumerTag -> { });
    }
}
