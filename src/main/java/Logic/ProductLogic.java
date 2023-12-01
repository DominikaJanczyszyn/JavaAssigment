package Logic;

import Domain.*;
import Domain.Package;
import Dto.PackageCreationDto;
import animalPart.AnimalPartResponse;
import animalPart.AnimalPartServiceGrpc;
import animalPart.GetAnimalPartByRegNoRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;
import product.*;
import java.util.ArrayList;

public class ProductLogic implements IProductLogic{
    private static final String QUEUE1 = "HalfAnimalQueue";
    private static final String QUEUE2 = "PackageQueue";
    private static final String HALF_ANIMAL = "HalfAnimal";
    private static final String PACKAGE = "Package";

    private final Gson gson;

    private ArrayList<HalfAnimal> halfAnimals;
    private ArrayList<Package> packages;

    public ProductLogic()
    {
        this.gson = new Gson();
        this.halfAnimals = new ArrayList<>();
        this.packages = new ArrayList<>();
        try {
            run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPackage(PackageCreationDto dto) throws Exception {
        if (dto.getAnimalPartType() == null || dto.getAnimalPartType().isEmpty())
            throw new Exception("Animal part type has to be declared.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            ProductServiceGrpc.ProductServiceBlockingStub stub;
            stub = ProductServiceGrpc.newBlockingStub(channel);
            stub.addPackage(AddPackageRequest.newBuilder().setAnimalPartType(dto.getAnimalPartType()).setMaxNrOfParts(dto.getMaxNrOfParts()).build());
            channel.shutdown();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo) throws Exception {
        if(packageRegNo <= 0)
            throw new Exception("Package registration number has to be larger than 0.");
        if(animalPartRegNo <=0)
            throw new Exception("Animal part registration number has to be larger than 0.");
        try
        {
        Package p = getPackageByRegNo(packageRegNo);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

        AnimalPartServiceGrpc.AnimalPartServiceBlockingStub stub;
        stub = AnimalPartServiceGrpc.newBlockingStub(channel);
        AnimalPartResponse response = stub.getAnimalPartByRegNo(GetAnimalPartByRegNoRequest.newBuilder().setRegNo(animalPartRegNo).build());
        Animal animal = new Gson().fromJson(response.getAnimal(), Animal.class);
        AnimalPart animalPart = new AnimalPart(animal, response.getPartWeight(), response.getPartRegNo(), response.getPartType());
        if (!p.getAnimalPartType().equals(animalPart.getPartType()))
            throw new Exception("The type of the animal part does not match the package type.");

        ProductServiceGrpc.ProductServiceBlockingStub stub1;
        stub1 = ProductServiceGrpc.newBlockingStub(channel);
        stub1.addAnimalPartToPackage(AddAnimalPartToPackageRequest.newBuilder().setPackageRegNo(packageRegNo).setAnimalPartRegNo(animalPartRegNo).build());
        channel.shutdown();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addHalfAnimal() throws Exception {
        try{
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();
            ProductServiceGrpc.ProductServiceBlockingStub stub;
            stub = ProductServiceGrpc.newBlockingStub(channel);
            stub.addHalfAnimal(AddHalfAnimalRequest.newBuilder().build());
            channel.shutdown();
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo) throws Exception {
        if(halfAnimalRegNo <= 0)
            throw new Exception("Half animal registration number has to be larger than 0.");
        if(animalPartRegNo <=0)
            throw new Exception("Animal part registration number has to be larger than 0.");
        try {
            HalfAnimal halfAnimal = getHalfAnimalByRegNo(halfAnimalRegNo);
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();
            AnimalPartServiceGrpc.AnimalPartServiceBlockingStub stub;
            stub = AnimalPartServiceGrpc.newBlockingStub(channel);
            AnimalPartResponse response = stub.getAnimalPartByRegNo(GetAnimalPartByRegNoRequest.newBuilder().setRegNo(animalPartRegNo).build());
            Animal animal = new Gson().fromJson(response.getAnimal(), Animal.class);
            AnimalPart animalPart = new AnimalPart(animal, response.getPartWeight(), response.getPartRegNo(), response.getPartType());

            if (halfAnimal.ifContainsPart(animalPart.getPartType()))
                throw new Exception("The type of the animal part is already in the half animal package.");

            ProductServiceGrpc.ProductServiceBlockingStub stub1;
            stub1 = ProductServiceGrpc.newBlockingStub(channel);
            stub1.addAnimalPartToHalfAnimal(AddAnimalPartToHalfAnimalRequest.newBuilder().setAnimalPartRegNo(animalPartRegNo).setHalfAnimalRegNo(halfAnimalRegNo).build());

            channel.shutdown();
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Package getPackageByRegNo(int regNo) throws Exception {
        if(regNo <= 0)
            throw new Exception("Package registration number has to be larger than 0.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            ProductServiceGrpc.ProductServiceBlockingStub stub;
            stub = ProductServiceGrpc.newBlockingStub(channel);
            PackageResponse packageResponse = stub.getPackageByRegNo(GetPackageByRegNoRequest.newBuilder().setRegNo(regNo).build());
            Package p = new Package(packageResponse.getRegNo(), packageResponse.getAnimalPartType(), packageResponse.getMaxNrOfParts());
            channel.shutdown();
            return p;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public HalfAnimal getHalfAnimalByRegNo(int regNo) throws Exception {
        if(regNo <= 0)
            throw new Exception("Half Animal registration number has to be larger than 0.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            ProductServiceGrpc.ProductServiceBlockingStub stub;
            stub = ProductServiceGrpc.newBlockingStub(channel);
            HalfAnimalResponse response = stub.getHalfAnimalByRegNo(GetHalfAnimalByRegNoRequest.newBuilder().setRegNo(regNo).build());
            HalfAnimal halfAnimal = new HalfAnimal(response.getRegNo());
            channel.shutdown();
            return halfAnimal;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ArrayList<Animal> getAnimalsByProductRegNo(int productRegNo) throws Exception {
        if(productRegNo <= 0)
            throw new Exception("Product registration number has to be larger than 0.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            ProductServiceGrpc.ProductServiceBlockingStub stub;
            stub = ProductServiceGrpc.newBlockingStub(channel);
            AnimalsResponse response = stub.getAnimalsByProductRegNo(GetAnimalsByProductRegNoRequest.newBuilder().setRegNo(productRegNo).build());
            String list = response.getAnimalList();
            ArrayList<Animal> animals = new Gson().fromJson(list, ArrayList.class);;
            channel.shutdown();
            return animals ;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ArrayList<Product> getProductsByAnimalRegNo(int animalRegNo) throws Exception {
        if(animalRegNo <= 0)
            throw new Exception("Animal registration number has to be larger than 0.");
        try
        {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
                    .usePlaintext()
                    .build();

            ProductServiceGrpc.ProductServiceBlockingStub stub;
            stub = ProductServiceGrpc.newBlockingStub(channel);
            ProductResponse response = stub.getProductsByAnimalRegNo(GetProductsByAnimalRegNo.newBuilder().setRegNo(animalRegNo).build());
            String list = response.getProductList();
            ArrayList<Product> animals = new Gson().fromJson(list, ArrayList.class);;
            channel.shutdown();
            return animals ;
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
        Connection connection1 = factory.newConnection();
        Channel channel1 = connection1.createChannel();
        Connection connection2 = factory.newConnection();
        Channel channel2 = connection2.createChannel();

        channel1.exchangeDeclare(QUEUE1, "topic");
        channel2.exchangeDeclare(QUEUE2, "topic");
        String queueName1 = channel1.queueDeclare().getQueue();
        String queueName2 = channel2.queueDeclare().getQueue();

        channel1.queueBind(queueName1, QUEUE1, HALF_ANIMAL);
        channel2.queueBind(queueName2, QUEUE2, PACKAGE);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            if (message.contains("HalfAnimal"))
            {
                TypeToken<ArrayList<HalfAnimal>> typeToken = new TypeToken<ArrayList<HalfAnimal>>() {};
                this.halfAnimals = gson.fromJson(message, typeToken.getType());
            }
            else
            {
                TypeToken<ArrayList<Package>> typeToken = new TypeToken<ArrayList<Package>>() {};
                this.packages = gson.fromJson(message, typeToken.getType());
            }
        };
        channel1.basicConsume(queueName1, true, deliverCallback, consumerTag -> { });
        channel2.basicConsume(queueName2, true, deliverCallback, consumerTag -> { });
    }
}
