package Dto;

public class AnimalPartCreationDTO {
    private int animalRegNo;
    private double partWeight;
    private String partType;

    public AnimalPartCreationDTO(int animalRegNo, double partWeight, String partType) {
        this.animalRegNo = animalRegNo;
        this.partWeight = partWeight;
        this.partType = partType;
    }

    public int getAnimalRegNo() {
        return animalRegNo;
    }

    public double getPartWeight() {
        return partWeight;
    }

    public String getPartType() {
        return partType;
    }
}
