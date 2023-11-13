package Logic;

import Dao.IAnimalPartDao;
import Domain.AnimalPart;
import Dto.AnimalPartCreationDTO;

public class AnimalPartLogic implements IAnimalPartLogic {
    private IAnimalPartDao dao;

    public AnimalPartLogic(IAnimalPartDao dao) {
        this.dao = dao;
    }

    @Override
    public void addAnimalPart(AnimalPartCreationDTO dto) throws Exception{
        if(dto.getAnimalRegNo() <= 0)
            throw new Exception("Animal Registration Number has to be declared.");
        if(dto.getPartWeight() <= 0)
            throw new Exception("Weight has to be a positive number.");
        if(dto.getPartType() == null || dto.getPartType().equals(""))
            throw new Exception("Part type has to be declared.");

        try
        {
            dao.addAnimalPart(dto);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public AnimalPart getAnimalPartByRegNo(int regNo) throws Exception {
        if(regNo <= 0) throw new Exception("Registration number has to be a positive number.");
        try{
            return dao.getAnimalPartByRegNo(regNo);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
