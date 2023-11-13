package Dao;

import Domain.Animal;
import Dto.AnimalCreationDTO;

import java.sql.SQLException;

public interface IAnimalDao {
    void addAnimal(AnimalCreationDTO animal) throws SQLException;
}
