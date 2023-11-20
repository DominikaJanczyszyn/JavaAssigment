package WebAPI;

import Dao.AnimalPartDao;
import Dao.ProductDao;
import Dto.PackageCreationDto;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Logic.*;

@RestController
@ResponseBody
public class Station3Controller
{
    private IProductLogic productLogic;
    private Gson gson = new Gson();

    public Station3Controller(){
        try {
            this.productLogic = new ProductLogic(ProductDao.getInstance(), AnimalPartDao.getInstance());
            this.gson= new Gson();
        }catch (Exception e){
            System.out.println("Initialization error.");
        }
    }
    @PostMapping("/package")
    public synchronized ResponseEntity<String> addPackage(@RequestBody String stringDto){
        try{
            PackageCreationDto dto = gson.fromJson(stringDto, PackageCreationDto.class);
            productLogic.addPackage(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Package has been created.");
    }

    @PutMapping("/package/{animalPartRegNo}")
    public synchronized ResponseEntity<String> addAnimalPartToPackage(@RequestBody int packageRegNo ,@PathVariable(value = "animalPartRegNo") int animalPartRegNo){
        try{
            productLogic.addAnimalPartToPackage(packageRegNo, animalPartRegNo);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Animal part added to the package.");
    }

    @PostMapping("/halfanimal")
    public synchronized ResponseEntity<String> addHalfAnimal(){
        try{
            productLogic.addHalfAnimal();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Half Animal has been created.");
    }

    @PutMapping("/halfanimal/{animalPartRegNo}")
    public synchronized ResponseEntity<String> addAnimalPartToHalfAnimal(@RequestBody int halfAnimalRegNo ,@PathVariable(value = "animalPartRegNo") int animalPartRegNo){
        try
        {
            productLogic.addAnimalPartToHalfAnimal(halfAnimalRegNo, animalPartRegNo);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Animal part added to the half animal.");
    }
}
