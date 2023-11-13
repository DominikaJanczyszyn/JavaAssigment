package Logic;

import Dao.IAnimalDao;
import Dto.AnimalCreationDTO;

public class AnimalLogic implements IAnimalLogic {

    private IAnimalDao dao;

    public AnimalLogic(IAnimalDao dao){
        this.dao = dao;
    }

    @Override
    public void addAnimal(AnimalCreationDTO dto) throws Exception{
        if(dto.getSpecies() == null || dto.getSpecies().equals(""))
            throw new Exception("Species have to be declared.");
        if(dto.getWeight() <= 0)
            throw new Exception("Weight has to be a positive number.");
        try
        {
            dao.addAnimal(dto);
        }
        catch (Exception e)
        {
           throw new Exception(e.getMessage());
        }
    }
}
