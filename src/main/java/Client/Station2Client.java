package Client;

import Domain.AnimalPart;
import Domain.Tray;
import Dto.AnimalPartCreationDTO;
import Dto.TrayCreationDto;
import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Station2Client {
    private static final String ROOT = "http://localhost:8080/";
    private static final Gson gson = new Gson();

    private static RestTemplate rest;

    public Station2Client()
    {
        rest = new RestTemplate();
    }

    private String addAnimalPart(AnimalPartCreationDTO dto)
    {
        ResponseEntity<String> response = rest.postForEntity(ROOT + "animalpart", gson.toJson(dto) , String.class);
        return response.getBody();
    }

    private String getAnimalPartByRegNo(int animalPartRegNo)
    {
        ResponseEntity<String> response = rest.getForEntity(ROOT + "animalpart/" + animalPartRegNo, String.class);
        return response.getBody();
    }

    private String addTray(TrayCreationDto dto)
    {
        ResponseEntity<String> response = rest.postForEntity(ROOT + "tray/" + dto.getPartType(), gson.toJson(dto) , String.class);
        return response.getBody();
    }

    private void addAnimalPartToTray(int id, int animalPartRegNo)
    {
        rest.put(ROOT + "tray/" + id, animalPartRegNo);
    }

    public static void main( String[] args )
    {
        new Station2Client().run();
    }


    private void run()
    {
        System.out.println(getAnimalPartByRegNo(1));
    }
}
