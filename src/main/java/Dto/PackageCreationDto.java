package Dto;

public class PackageCreationDto
{
    private String animalPartType;
    private int maxNrOfParts;

    public PackageCreationDto(String animalPartType, int maxNrOfParts) {
        this.animalPartType = animalPartType;
        this.maxNrOfParts = maxNrOfParts;
    }

    public String getAnimalPartType() {
        return animalPartType;
    }

    public int getMaxNrOfParts()
    {
        return maxNrOfParts;
    }
}
