package WebAPI;

import Dto.AnimalCreationDTO;
import Logic.AnimalLogic;
import Logic.IAnimalLogic;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@ResponseBody
public class Station1Controller{

    private IAnimalLogic animalLogic;
    private Gson gson;

    public Station1Controller(){
        try {
            this.animalLogic = new AnimalLogic();
            this.gson= new Gson();
        }catch (Exception e){
            System.out.println("Initialization error.");
        }
    }

    @PostMapping("/animal")
    public synchronized ResponseEntity<String> addAnimal(@RequestBody String stringDto){
        AnimalCreationDTO dto;
        try
        {
            dto = gson.fromJson(stringDto, AnimalCreationDTO.class);
            animalLogic.addAnimal(dto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(dto));
      }
}
