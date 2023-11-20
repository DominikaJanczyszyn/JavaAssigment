package Dao;

import Dto.AnimalCreationDTO;

import java.sql.SQLException;
import java.sql.*;

public class AnimalDao implements IAnimalDao{
    private static AnimalDao instance;

    public static synchronized AnimalDao getInstance() throws SQLException
    {
        if(instance == null) instance = new AnimalDao();
        return instance;
    }
    private AnimalDao() throws SQLException{
        DriverManager.registerDriver(new org.postgresql.Driver());
    }
    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse", "postgres", "xf31bhl9");
    }

    @Override
    public void addAnimal(AnimalCreationDTO animal) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Animal(species, weight) values(?, ?);");
            statement.setString(1, animal.getSpecies());
            statement.setDouble(2, animal.getWeight());
            statement.executeUpdate();
        }
    }

}
