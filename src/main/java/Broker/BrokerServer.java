package Broker;
import Dao.*;
import Domain.AnimalPart;
import Domain.HalfAnimal;
import Domain.Package;
import Domain.Tray;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class BrokerServer{

    private static final String QUEUE1 = "AnimalPartQueue";
    private static final String QUEUE2 = "TrayQueue";
    private static final String QUEUE3 = "HalfAnimalQueue";
    private static final String QUEUE4 = "PackageQueue";

    private static final String ANIMAL_PART = "AnimalPart";
    private static final String TRAY = "Tray";
    private static final String HALF_ANIMAL = "HalfAnimal";
    private static final String PACKAGE = "Package";

    private IAnimalPartDao animalPartDao;
    private ITrayDao trayDao;
    private IProductDao productDao;

    private Gson gson;

    public BrokerServer(IAnimalPartDao animalPartDao, IProductDao productDao, ITrayDao trayDao)
    {
        this.animalPartDao = animalPartDao;
        this.productDao = productDao;
        this.trayDao = trayDao;
        this.gson = new Gson();

    }

    public synchronized void run() throws Exception {
        while (true)
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.exchangeDeclare(QUEUE1, "topic");
                channel.exchangeDeclare(QUEUE2, "topic");
                channel.exchangeDeclare(QUEUE3, "topic");
                channel.exchangeDeclare(QUEUE4, "topic");

                ArrayList<AnimalPart> animalParts = animalPartDao.getAllAnimalParts();
                String message = gson.toJson(animalParts);

                channel.basicPublish(QUEUE1, ANIMAL_PART, null, message.getBytes("UTF-8"));
                System.out.println(" [" + LocalDate.now() + "] Sent '" + ANIMAL_PART + "':'" + message + "'");

                ArrayList<Tray> trays = trayDao.getAllTrays();
                message = gson.toJson(trays);

                channel.basicPublish(QUEUE2, TRAY, null, message.getBytes("UTF-8"));
                System.out.println(" [" + LocalDate.now() + "] Sent '" + TRAY + "':'" + message + "'");

                ArrayList<HalfAnimal> halfAnimals = productDao.getAllHalfAnimals();
                message = gson.toJson(halfAnimals);

                channel.basicPublish(QUEUE3, HALF_ANIMAL, null, message.getBytes("UTF-8"));
                System.out.println(" [" + LocalDate.now() + "] Sent '" + HALF_ANIMAL + "':'" + message + "'");

                ArrayList<Package> packages = productDao.getAllPackages();
                message = gson.toJson(packages);

                channel.basicPublish(QUEUE4, PACKAGE, null, message.getBytes("UTF-8"));
                System.out.println(" [" + LocalDate.now() + "] Sent '" + PACKAGE + "':'" + message + "'");
            }
            Thread.sleep(600_000);
        }
    }
}
