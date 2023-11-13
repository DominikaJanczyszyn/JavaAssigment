package Logic;

import Dto.PackageCreationDto;

public interface IProductLogic {
    void addPackage(PackageCreationDto dto) throws Exception;
    void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo) throws Exception;
    void addHalfAnimal() throws Exception;
    void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo) throws Exception;
}
