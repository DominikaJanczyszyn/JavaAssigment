package Logic;

import Domain.AnimalPart;
import Domain.Tray;
import Dto.AnimalPartCreationDTO;

public interface IAnimalPartLogic {
    void addAnimalPart(AnimalPartCreationDTO dto) throws Exception;
    AnimalPart getAnimalPartByRegNo(int regNo) throws Exception;
}
