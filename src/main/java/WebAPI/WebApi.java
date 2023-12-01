package WebAPI;

import Logic.BrokerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApi
{
    public static void main(String[] args) throws Exception{
        SpringApplication.run(WebApi.class, args);
    }
}