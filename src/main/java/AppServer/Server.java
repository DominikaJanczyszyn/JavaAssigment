package AppServer;

import Dao.IAnimalDao;
import Dao.IAnimalPartDao;
import Dao.IProductDao;
import Dao.ITrayDao;
import Domain.Animal;
import Domain.Product;
import Dto.AnimalCreationDTO;
import Dto.AnimalPartCreationDTO;
import Dto.PackageCreationDto;
import Dto.TrayCreationDto;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server implements IServer {
    private IAnimalDao animalDao;
    private IAnimalPartDao animalPartDao;
    private IProductDao productDao;
    private ITrayDao trayDao;

    public Server(IAnimalDao animalDao, IAnimalPartDao animalPartDao, IProductDao productDao, ITrayDao trayDao) {
        this.animalDao = animalDao;
        this.animalPartDao = animalPartDao;
        this.productDao = productDao;
        this.trayDao = trayDao;
    }

    @Override
    public void addAnimal(AnimalCreationDTO dto) {
        try {
            animalDao.addAnimal(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAnimalPart(AnimalPartCreationDTO dto) {
        try {
            animalPartDao.addAnimalPart(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTray(TrayCreationDto dto, String type) {
        try {
            trayDao.addTray(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAnimalPartToTray(int id, int animalPartRegNo) {
        try {
            trayDao.addAnimalPartToTray(id, animalPartRegNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPackage(PackageCreationDto dto) {
        try {
            productDao.addPackage(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo) {
        try {
            productDao.addAnimalPartToProduct(packageRegNo, animalPartRegNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addHalfAnimal() {
        try {
            productDao.addHalfAnimal();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo) {
        try {
            productDao.addAnimalPartToProduct(halfAnimalRegNo, animalPartRegNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Animal> getAnimalsByProductRegNo(int productRegNo) {
        try {
            return productDao.getAnimalsByProductRegNo(productRegNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Product> getProductsByAnimalRegNo(int animalRegNo) {
        try {
            return productDao.getProductsByAnimalRegNo(animalRegNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
