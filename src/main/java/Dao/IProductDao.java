package Dao;

import Domain.HalfAnimal;
import Domain.Package;
import Dto.PackageCreationDto;

import java.sql.SQLException;

public interface IProductDao {
    void addPackage(PackageCreationDto dto) throws SQLException;
    Package getPackageByRegNo(int regNo) throws SQLException;
    void addAnimalPartToProduct(int regNo, int animalPartRegNo) throws SQLException;
    void addHalfAnimal() throws SQLException;
    HalfAnimal getHalfAnimalByRegNo(int regNo) throws SQLException;
}
