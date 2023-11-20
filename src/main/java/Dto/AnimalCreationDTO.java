package Dto;

import java.io.Serializable;

public class AnimalCreationDTO implements Serializable {
    private String species;
    private double weight;

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
