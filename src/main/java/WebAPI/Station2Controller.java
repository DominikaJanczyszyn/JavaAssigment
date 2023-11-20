package WebAPI;

import Dao.AnimalPartDao;
import Dao.TrayDao;
import Domain.AnimalPart;
import Dto.AnimalPartCreationDTO;
import Dto.TrayCreationDto;
import Logic.*;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class Station2Controller {
    private IAnimalPartLogic animalPartLogic;
    private ITrayLogic trayLogic;
    private Gson gson;

    public Station2Controller(){
        try {
            this.animalPartLogic = new AnimalPartLogic();
            this.trayLogic = new TrayLogic();
            this.gson= new Gson();
        }catch (Exception e){
            System.out.println("Initialization error.");
        }
    }

    @PostMapping("/animalpart")
    public synchronized ResponseEntity<String> addAnimalPart(@RequestBody String stringDto)
    {
        AnimalPartCreationDTO dto = gson.fromJson(stringDto, AnimalPartCreationDTO.class);

        try
        {
            animalPartLogic.addAnimalPart(dto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(dto));
    }

    @GetMapping("/animalpart/{animalPartRegNo}")
    public synchronized ResponseEntity<String> getAnimalPartByRegNo( @PathVariable(value = "animalPartRegNo") int animalPartRegNo )
    {
        AnimalPart animalPart;
        try
        {
            animalPart = animalPartLogic.getAnimalPartByRegNo(animalPartRegNo);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        if( animalPart == null )
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal part not found.");
        else
            return ResponseEntity.status(HttpStatus.FOUND).body(gson.toJson(animalPart));
    }

    @PostMapping("/tray/{type}")
    public synchronized ResponseEntity<String> addTray(@RequestBody String stringDto, @PathVariable(value = "type") String type){
        TrayCreationDto dto = gson.fromJson(stringDto, TrayCreationDto.class);
        try
        {
            trayLogic.addTray(dto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(gson.toJson(dto));
    }

    @PutMapping("/tray/{id}")
    public synchronized ResponseEntity<String> addAnimalPartToTray( @PathVariable(value = "id") int id, @RequestBody int animalPartRegNo){
        try{
            trayLogic.addAnimalPartToTray(id, animalPartRegNo);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Animal part added to the tray.");
    }
}
