package AppServer;
import Domain.HalfAnimal;
import Domain.Product;
import com.google.gson.Gson;
import Dao.IProductDao;
import Domain.Animal;
import Domain.Package;
import Dto.PackageCreationDto;
import Logic.IProductLogic;
import io.grpc.stub.StreamObserver;
import product.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {


    private IProductDao dao;
    private Gson gson;

    public ProductServiceImpl(IProductDao dao) {
        this.dao = dao;
        this.gson = new Gson();
    }

    @Override
    public void addPackage(AddPackageRequest request, StreamObserver<EmptyResponse> responseObserver) {
        String animalPartType  = request.getAnimalPartType();
        int maxNrOfParts = request.getMaxNrOfParts();
        PackageCreationDto dto = new PackageCreationDto(animalPartType, maxNrOfParts);

        try {
            dao.addPackage(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmptyResponse response = EmptyResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addAnimalPartToPackage(AddAnimalPartToPackageRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            dao.addAnimalPartToProduct(request.getPackageRegNo(), request.getAnimalPartRegNo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmptyResponse response = EmptyResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addHalfAnimal(AddHalfAnimalRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            dao.addHalfAnimal();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmptyResponse response = EmptyResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addAnimalPartToHalfAnimal(AddAnimalPartToHalfAnimalRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            dao.addAnimalPartToProduct(request.getHalfAnimalRegNo(), request.getAnimalPartRegNo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmptyResponse response = EmptyResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPackageByRegNo(GetPackageByRegNoRequest request, StreamObserver<PackageResponse> responseObserver) {
        try {
            Package p = dao.getPackageByRegNo(request.getRegNo());
            PackageResponse response = PackageResponse.newBuilder().setAnimalPartType(p.getAnimalPartType()).setAnimalPartType(p.getAnimalPartType()).setRegNo(p.getRegNo()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getHalfAnimalByRegNo(GetHalfAnimalByRegNoRequest request, StreamObserver<HalfAnimalResponse> responseObserver) {
        try {
            HalfAnimal halfAnimal = dao.getHalfAnimalByRegNo(request.getRegNo());
            HalfAnimalResponse response = HalfAnimalResponse.newBuilder().setRegNo(halfAnimal.getRegNo()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAnimalsByProductRegNo(GetAnimalsByProductRegNoRequest request, StreamObserver<AnimalsResponse> responseObserver) {
        try {
            ArrayList<Animal> animals =  dao.getAnimalsByProductRegNo(request.getRegNo());
            String s = gson.toJson(animals);
            AnimalsResponse response = AnimalsResponse.newBuilder().setAnimalList(s).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getProductsByAnimalRegNo(GetProductsByAnimalRegNo request, StreamObserver<ProductResponse> responseObserver) {
        try {
            ArrayList<Product> products = dao.getProductsByAnimalRegNo(request.getRegNo());
            String s = gson.toJson(products);
            ProductResponse response = ProductResponse.newBuilder().setProductList(s).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
