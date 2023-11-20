package AppServer;

import Domain.Animal;
import Domain.Product;
import Dto.AnimalCreationDTO;
import Dto.AnimalPartCreationDTO;
import Dto.PackageCreationDto;
import Dto.TrayCreationDto;

import java.util.ArrayList;

public interface IServer {
    void addAnimal(AnimalCreationDTO dto);
    void addAnimalPart(AnimalPartCreationDTO dto);
    void addTray(TrayCreationDto dto, String type);
    void addAnimalPartToTray(int id, int animalPartRegNo);
    void addPackage(PackageCreationDto dto);
    void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo);
    void addHalfAnimal();
    void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo);
    ArrayList<Animal> getAnimalsByProductRegNo(int productRegNo);
    ArrayList<Product> getProductsByAnimalRegNo(int animalRegNo);
}
