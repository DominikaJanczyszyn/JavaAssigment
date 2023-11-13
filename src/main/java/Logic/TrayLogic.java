package Logic;

import Dao.IAnimalPartDao;
import Dao.ITrayDao;
import Domain.AnimalPart;
import Domain.Tray;
import Dto.TrayCreationDto;

public class TrayLogic implements ITrayLogic {
    private ITrayDao trayDao;
    private IAnimalPartDao animalPartDao;

    public TrayLogic(ITrayDao trayDao, IAnimalPartDao animalPartDao)
    {
        this.trayDao = trayDao;
        this.animalPartDao = animalPartDao;
    }

    @Override
    public void addTray(TrayCreationDto dto) throws Exception {
        if(dto.getWeightCapacity() <= 0)
            throw new Exception("Weight capacity has to be a positive number.");
        if(dto.getPartType() == null || dto.getPartType().equals(""))
            throw new Exception("Part has to be declared.");

        try {
            trayDao.addTray(dto);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addAnimalPartToTray(int trayId, int animalPartRegNo) throws Exception {
        Tray tray = trayDao.getTrayById(trayId);
        AnimalPart animalPart = animalPartDao.getAnimalPartByRegNo(animalPartRegNo);
        if (!tray.getPartType().equals(animalPart.getPartType()))
            throw new Exception("The type of the animal part does not match the tray type.");

        try
        {
            System.out.println("ok");
            trayDao.addAnimalPartToTray(trayId, animalPartRegNo);
            System.out.println("ok1");

        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
