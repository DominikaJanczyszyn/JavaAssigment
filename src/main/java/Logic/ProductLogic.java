package Logic;

import Dao.IAnimalPartDao;
import Dao.IProductDao;
import Domain.*;
import Domain.Package;
import Dto.PackageCreationDto;
import animal.AnimalServiceGrpc;
import animalPart.AnimalPartResponse;
import animalPart.AnimalPartServiceGrpc;
import animalPart.GetAnimalPartByRegNoRequest;
import com.google.gson.Gson;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.checkerframework.checker.units.qual.A;
import product.*;
import tray.AddTrayRequest;
import tray.TrayServiceGrpc;
import java.util.ArrayList;

public class ProductLogic implements IProductLogic{

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
            throw new Exception(e.getMessage());
        }
    }
}
