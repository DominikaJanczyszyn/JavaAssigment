package Dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class AnimalCreationDTO implements Serializable {
    private String species;
    private double weight;

    private static final ObjectMapper mapper = new ObjectMapper();

    public AnimalCreationDTO(String species, double weight) {
        this.species = species;
        this.weight = weight;
    }

    public String getSpecies() {
        return species;
    }

    public double getWeight() {
        return weight;
    }
}
