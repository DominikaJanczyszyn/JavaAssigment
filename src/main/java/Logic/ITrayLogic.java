package Logic;

import Dto.TrayCreationDto;

public interface ITrayLogic {
    void addTray(TrayCreationDto dto) throws Exception;
    void addAnimalPartToTray(int trayId, int animalPartRegNo) throws Exception;
}
