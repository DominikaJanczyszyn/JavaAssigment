package Dao;

import Domain.AnimalPart;
import Dto.AnimalPartCreationDTO;
import java.util.ArrayList;

import java.sql.SQLException;

public interface IAnimalPartDao {
    void addAnimalPart(AnimalPartCreationDTO dto) throws SQLException;
    AnimalPart getAnimalPartByRegNo(int regNo) throws SQLException;
    ArrayList<AnimalPart> getAllAnimalParts() throws SQLException;
}
