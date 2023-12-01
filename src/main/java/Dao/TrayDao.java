package Dao;

import Domain.Animal;
import Domain.AnimalPart;
import Domain.Tray;
import Dto.TrayCreationDto;

import java.sql.*;
import java.util.ArrayList;

public class TrayDao implements ITrayDao{

    private static TrayDao instance;

    public static synchronized TrayDao getInstance() throws SQLException
    {
        if(instance == null) instance = new TrayDao();
        return instance;
    }
    private  TrayDao() throws SQLException{
        DriverManager.registerDriver(new org.postgresql.Driver());
    }
    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse", "postgres", "sql3486");
    }
    @Override
    public void addTray(TrayCreationDto dto) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Tray(weight_capacity, part_type) values(?, ?);");
            statement.setDouble(1, dto.getWeightCapacity());
            statement.setString(2, dto.getPartType());
            statement.executeUpdate();
        }
    }

    @Override
    public Tray getTrayById(int id) throws SQLException {

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Tray WHERE reg_no = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Tray tray = null;
            if(resultSet.next()){
                int trayId = resultSet.getInt("reg_no");
                double weightCapacity = resultSet.getDouble("weight_capacity");
                String partsType = resultSet.getString("part_type");
                tray = new Tray(trayId, weightCapacity, partsType);

                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM AnimalPart join Animal on AnimalPart.animal_reg_no = Animal.reg_no where AnimalPart.tray_reg_no = ?;");
                statement1.setInt(1, trayId);
                ResultSet resultSet1 = statement1.executeQuery();
                ArrayList<AnimalPart> parts = new ArrayList<>();
                while(resultSet1.next()){
                    String species = resultSet1.getString(8);
                    double animalWeight = resultSet1.getDouble(9);
                    int animalRegNo = resultSet1.getInt(7);
                    double partWeight = resultSet1.getDouble(2);
                    int partRegNo = resultSet1.getInt(1);
                    String partType = resultSet1.getString(3);
                    AnimalPart animalPart = new AnimalPart(new Animal(species, animalWeight, animalRegNo), partWeight, partRegNo, partType);
                    parts.add(animalPart);
                }
                tray.setParts(parts);
            }
            return tray;
        }
    }

    @Override
    public void addAnimalPartToTray(int trayId, int animalPartRegNo) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("UPDATE animalpart SET tray_reg_no = ? WHERE reg_no = ?");
            statement.setInt(1, trayId);
            statement.setInt(2, animalPartRegNo);
            statement.executeUpdate();
        }
    }

    @Override
    public ArrayList<Tray> getAllTrays() throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Tray");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Tray> trays = new ArrayList<>();
            Tray tray = null;
            if(resultSet.next()){
                int trayId = resultSet.getInt("reg_no");
                double weightCapacity = resultSet.getDouble("weight_capacity");
                String partsType = resultSet.getString("part_type");
                tray = new Tray(trayId, weightCapacity, partsType);

                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM AnimalPart join Animal on AnimalPart.animal_reg_no = Animal.reg_no where AnimalPart.tray_reg_no = ?;");
                statement1.setInt(1, trayId);
                ResultSet resultSet1 = statement1.executeQuery();
                ArrayList<AnimalPart> parts = new ArrayList<>();
                while(resultSet1.next()){
                    String species = resultSet1.getString(8);
                    double animalWeight = resultSet1.getDouble(9);
                    int animalRegNo = resultSet1.getInt(7);
                    double partWeight = resultSet1.getDouble(2);
                    int partRegNo = resultSet1.getInt(1);
                    String partType = resultSet1.getString(3);
                    AnimalPart animalPart = new AnimalPart(new Animal(species, animalWeight, animalRegNo), partWeight, partRegNo, partType);
                    parts.add(animalPart);
                }
                tray.setParts(parts);
                trays.add(tray);
            }
            return trays;
        }
    }
}
