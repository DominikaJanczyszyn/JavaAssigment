package Dao;

import Domain.Animal;
import Domain.HalfAnimal;
import Domain.Package;
import Domain.Product;
import Dto.PackageCreationDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IProductDao {
    void addPackage(PackageCreationDto dto) throws SQLException;
    Package getPackageByRegNo(int regNo) throws SQLException;
    void addAnimalPartToProduct(int regNo, int animalPartRegNo) throws SQLException;
    void addHalfAnimal() throws SQLException;
    HalfAnimal getHalfAnimalByRegNo(int regNo) throws SQLException;
    ArrayList<Animal> getAnimalsByProductRegNo(int productRegNo) throws SQLException;
    ArrayList<Product> getProductsByAnimalRegNo(int animalRegNo) throws SQLException;
}
