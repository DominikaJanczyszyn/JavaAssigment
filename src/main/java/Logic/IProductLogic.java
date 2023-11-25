package Logic;

import Domain.Animal;
import Domain.HalfAnimal;
import Domain.Package;
import Domain.Product;
import Dto.PackageCreationDto;

import java.util.ArrayList;

public interface IProductLogic {
    void addPackage(PackageCreationDto dto) throws Exception;
    void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo) throws Exception;
    void addHalfAnimal() throws Exception;
    void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo) throws Exception;
    Package getPackageByRegNo(int regNo) throws Exception;
    HalfAnimal getHalfAnimalByRegNo(int regNo) throws Exception;
    ArrayList<Animal> getAnimalsByProductRegNo(int productRegNo) throws Exception;
    ArrayList<Product> getProductsByAnimalRegNo(int animalRegNo) throws Exception;
}
