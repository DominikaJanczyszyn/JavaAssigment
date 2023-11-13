package Client;

import Domain.Package;
import Dto.PackageCreationDto;
import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Station3Client {
    private static final String ROOT = "http://localhost:8080/";
    private static final Gson gson = new Gson();

    private static RestTemplate rest;

    public Station3Client()
    {
        rest = new RestTemplate();
    }

    private String addPackage(PackageCreationDto dto)
    {
        ResponseEntity<String> response = rest.postForEntity(ROOT + "package", gson.toJson(dto) , String.class);
        return response.getBody();
    }

    private void addAnimalPartToPackage(int packageRegNo, int animalPartRegNo)
    {
        rest.put(ROOT + "package/" + animalPartRegNo, packageRegNo);
    }

    private String addHalfAnimal()
    {
        ResponseEntity<String> response = rest.postForEntity(ROOT + "halfanimal", null , String.class);
        return response.getBody();
    }

    private void addAnimalPartToHalfAnimal(int halfAnimalRegNo, int animalPartRegNo)
    {
        rest.put(ROOT + "package/" + animalPartRegNo, halfAnimalRegNo);
    }

    public static void main( String[] args )
    {
        new Station3Client().run();
    }


    private void run()
    {
        PackageCreationDto dto = new PackageCreationDto("leg", 10);
        System.out.println(addPackage(dto));

        addAnimalPartToPackage(1, 1);

        System.out.println(addHalfAnimal());

        addAnimalPartToHalfAnimal(2,1);
    }
}
