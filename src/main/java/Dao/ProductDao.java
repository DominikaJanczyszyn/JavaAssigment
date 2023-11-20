package Dao;

import Domain.Animal;
import Domain.HalfAnimal;
import Domain.Package;
import Domain.Product;
import Dto.PackageCreationDto;

import java.sql.*;
import java.util.ArrayList;

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
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse", "postgres", "xf31bhl9");
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

    @Override
    public ArrayList<Animal> getAnimalsByProductRegNo(int productRegNo) throws SQLException {
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Animal JOIN AnimalPart On Animal.reg_no = AnimalPart.animal_reg_no WHERE product_reg_no = ?;");
            statement.setInt(1, productRegNo);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Animal> animals = new ArrayList<>();

            while(resultSet.next()){
                String species = resultSet.getString("species");
                double weight = resultSet.getDouble("animal.weight");
                int regNo = resultSet.getInt("animal.reg_no");
                Animal animal = new Animal(species, weight, regNo);
                animals.add(animal);
            }
            return animals;
        }
    }

    @Override
    public ArrayList<Product> getProductsByAnimalRegNo(int animalRegNo) throws SQLException {
        try (Connection connection = getConnection())
        {
            ArrayList<Product> products = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Package JOIN Product ON package.reg_no = product.reg_no JOIN AnimalPart ON product.reg_no = animalpart.product_reg_no WHERE animalPart.reg_no = ?;");
            statement.setInt(1, animalRegNo);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                int regNo = resultSet.getInt("package.reg_no");
                Package p = getPackageByRegNo(regNo);
                products.add(p);
            }
            PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM HalfAnimal JOIN Product ON halfanimal.reg_no = product.reg_no JOIN AnimalPart ON product.reg_no = animalpart.product_reg_no WHERE animalPart.reg_no = ?;");
            statement1.setInt(1, animalRegNo);
            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next())
            {
                int regNo = resultSet1.getInt("halfanimal.reg_no");
                HalfAnimal halfAnimal = getHalfAnimalByRegNo(regNo);
                products.add(halfAnimal);
            }
            return products;
        }
    }
}
