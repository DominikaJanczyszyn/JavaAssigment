package Dao;

import Domain.HalfAnimal;
import Domain.Package;
import Dto.PackageCreationDto;

import java.sql.*;

public class ProductDao implements IProductDao{
    private static ProductDao instance;

    private ProductDao() throws SQLException
    {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static synchronized ProductDao getInstance() throws SQLException
    {
        if(instance == null) instance = new ProductDao();
        return instance;
    }

    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse", "postgres", "sql3486");
    }

    @Override
    public void addPackage(PackageCreationDto dto) throws SQLException {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO product default values;", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next())
            {
                int id = keys.getInt(1);
                PreparedStatement statement1 = connection.prepareStatement("INSERT INTO package(reg_no, animal_part_type, max_nr_of_parts) values (?,?,?);");
                statement1.setInt(1, id);
                statement1.setString(2, dto.getAnimalPartType());
                statement1.setInt(3, dto.getMaxNrOfParts());
                statement1.executeUpdate();
            }
        }
    }

    @Override
    public Package getPackageByRegNo(int regNo) throws SQLException {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM package WHERE reg_no = ?;");
            statement.setInt(1, regNo);
            ResultSet resultSet = statement.executeQuery();
            Package p = null;
            if(resultSet.next())
            {
                String animalPartType = resultSet.getString("animal_part_type");
                int maxNrOfParts = resultSet.getInt("max_nr_of_parts");
                p = new Package(regNo, animalPartType, maxNrOfParts);
            }
            return p;
        }
    }

    @Override
    public void addAnimalPartToProduct(int regNo, int animalPartRegNo) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE animalpart SET product_reg_no = ? WHERE reg_no = ?");
            statement.setInt(1, regNo);
            statement.setInt(2, animalPartRegNo);
            statement.executeUpdate();
        }
    }

    @Override
    public void addHalfAnimal() throws SQLException {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO product default values;", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next())
            {
                int id = keys.getInt(1);
                PreparedStatement statement1 = connection.prepareStatement("INSERT INTO halfanimal(reg_no) values (?);");
                statement1.setInt(1, id);
                statement1.executeUpdate();
            }
        }
    }

    @Override
    public HalfAnimal getHalfAnimalByRegNo(int regNo) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM halfAnimal WHERE reg_no = ?;");
            statement.setInt(1, regNo);
            ResultSet resultSet = statement.executeQuery();
            HalfAnimal halfAnimal = null;
            if (resultSet.next()) {
                int reg_no = resultSet.getInt("reg_no");
                halfAnimal = new HalfAnimal(reg_no);
            }
            return halfAnimal;
        }
    }
}
