package Dao;

import Dto.TrayCreationDto;

import java.sql.SQLException;
import java.util.ArrayList;

import Domain.Tray;

public interface ITrayDao {
    void addTray(TrayCreationDto dto) throws SQLException;
    Tray getTrayById(int id) throws SQLException;
    void addAnimalPartToTray(int trayId, int animalPartRegNo) throws SQLException;
    ArrayList<Tray> getAllTrays() throws SQLException;
}
