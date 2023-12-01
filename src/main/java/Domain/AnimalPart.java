package Domain;

import java.io.Serializable;

public class AnimalPart {
    private Animal animal;
    private double partWeight;
    private int partRegNo;
    private String partType;

    public AnimalPart(Animal animal, double partWeight, int partRegNo, String partType) {
        this.animal = animal;
        this.partWeight = partWeight;
        this.partRegNo = partRegNo;
        this.partType = partType;
    }

    public Animal getAnimal() {
        return animal;
    }

    public double getPartWeight() {
        return partWeight;
    }

    public int getPartRegNo() {
        return partRegNo;
    }

    public String getPartType() {
        return partType;
    }

    @Override
    public String toString() {
        return "AnimalPart{" +
                "animal=" + animal +
                ", partWeight=" + partWeight +
                ", partRegNo=" + partRegNo +
                ", partType='" + partType + '\'' +
                '}';
    }
}
