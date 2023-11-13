package Dao;

import Dto.TrayCreationDto;

import java.sql.SQLException;
import Domain.Tray;

public interface ITrayDao {
    void addTray(TrayCreationDto dto) throws SQLException;
    Tray getTrayById(int id) throws SQLException;
    void addAnimalPartToTray(int trayId, int animalPartRegNo) throws SQLException;
}
