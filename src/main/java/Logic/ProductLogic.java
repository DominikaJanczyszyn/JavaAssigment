package Logic;

import Dao.IAnimalPartDao;
import Dao.IProductDao;
import Domain.AnimalPart;
import Domain.HalfAnimal;
import Domain.Package;
import Dto.PackageCreationDto;

public class ProductLogic implements IProductLogic{
    private IProductDao productDao;
    private IAnimalPartDao animalPartDao;

    public ProductLogic(IProductDao productDao, IAnimalPartDao animalPartDao)
    {
        this.productDao = productDao;
        this.animalPartDao =animalPartDao;
    }

    @Override
    public void addPackage(PackageCreationDto dto) throws Exception {
        if (dto.getAnimalPartType() == null || dto.getAnimalPartType().isEmpty())
            throw new Exception("Animal part type has to be declared.");

        try
        {
            productDao.addPackage(dto);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo) throws Exception {
        if(packageRegNo <= 0)
            throw new Exception("Package registration number has to be larger than 0.");
        if(animalPartRegNo <=0)
            throw new Exception("Animal part registration number has to be larger than 0.");

        Package p = productDao.getPackageByRegNo(packageRegNo);
        AnimalPart animalPart = animalPartDao.getAnimalPartByRegNo(animalPartRegNo);

        if (!p.getAnimalPartType().equals(animalPart.getPartType()))
            throw new Exception("The type of the animal part does not match the package type.");

        try
        {
            productDao.addAnimalPartToProduct(packageRegNo, animalPartRegNo);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addHalfAnimal() throws Exception {
        try
        {
            productDao.addHalfAnimal();
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo) throws Exception {
        if(halfAnimalRegNo <= 0)
            throw new Exception("Half animal registration number has to be larger than 0.");
        if(animalPartRegNo <=0)
            throw new Exception("Animal part registration number has to be larger than 0.");

        HalfAnimal halfAnimal = productDao.getHalfAnimalByRegNo(halfAnimalRegNo);
        AnimalPart animalPart = animalPartDao.getAnimalPartByRegNo(animalPartRegNo);

        if (!halfAnimal.ifContainsPart(animalPart.getPartType()))
            throw new Exception("The type of the animal part is already in the half animal package.");

        try
        {
            productDao.addAnimalPartToProduct(halfAnimalRegNo, animalPartRegNo);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
