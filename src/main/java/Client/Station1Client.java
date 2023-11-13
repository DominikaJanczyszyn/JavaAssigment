package Client;

import Dto.AnimalCreationDTO;
import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Station1Client {
    private static final String ROOT = "http://localhost:8080/";
    private static final Gson gson = new Gson();

    private static RestTemplate rest;

    public Station1Client()
    {
        rest = new RestTemplate();
    }


    private String addAnimal(AnimalCreationDTO dto)
    {
        ResponseEntity<String> response = rest.postForEntity(ROOT + "animal", gson.toJson(dto) , String.class);
        return response.getBody();
    }

    public static void main( String[] args )
    {
        new Station1Client().run();
    }


    private void run()
    {
        AnimalCreationDTO dto = new AnimalCreationDTO("pig", 200);
        System.out.println(gson.fromJson(addAnimal(dto), AnimalCreationDTO.class));
    }
}
