package Dao;

import Domain.Animal;
import Domain.AnimalPart;
import Dto.AnimalPartCreationDTO;

import java.sql.*;

public class AnimalPartDao implements IAnimalPartDao
{
    private static AnimalPartDao instance;

    private AnimalPartDao() throws SQLException
    {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static synchronized AnimalPartDao getInstance() throws SQLException
    {
        if(instance == null) instance = new AnimalPartDao();
        return instance;
    }

    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse", "postgres", "sql3486");
    }

    @Override
    public void addAnimalPart(AnimalPartCreationDTO dto) throws SQLException {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO AnimalPart(weight, part_type, animal_reg_no) VALUES (?,?,?)");
            statement.setDouble(1, dto.getPartWeight());
            statement.setString(2, dto.getPartType());
            statement.setInt(3, dto.getAnimalRegNo());
            statement.executeUpdate();
        }
    }

    @Override
    public AnimalPart getAnimalPartByRegNo(int regNo) throws SQLException {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM AnimalPart join Animal on AnimalPart.animal_reg_no = Animal.reg_no where AnimalPart.reg_no = ?;");
            statement.setInt(1, regNo);
            ResultSet resultSet = statement.executeQuery();
            AnimalPart animalPart = null;
            if (resultSet.next())
            {
                double partWeight = resultSet.getDouble(2);
                String partType = resultSet.getString(3);
                int animalRegNo = resultSet.getInt(4);
                String species = resultSet.getString(8);
                double animalWeight = resultSet.getDouble(9);

                Animal animal = new Animal(species, animalWeight, animalRegNo);
                animalPart = new AnimalPart(animal, partWeight, regNo, partType);
            }
            return animalPart;
        }
    }
}
