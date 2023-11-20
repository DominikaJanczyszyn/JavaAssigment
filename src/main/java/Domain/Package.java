package Domain;

public class Package extends Product {
    private String animalPartType;
    private int maxNrOfParts;

    public Package(int regNo ,String animalPartType, int maxNrOfParts) {
        super(regNo);
        this.animalPartType = animalPartType;
        this.maxNrOfParts = maxNrOfParts;
    }

    public void addAnimalPart(AnimalPart animalPart) throws Exception
    {
        if (getParts().size() != maxNrOfParts)
            getParts().add(animalPart);
        throw new Exception("Maximum number of parts is the package reached.");
    }

    public String getAnimalPartType() {
        return animalPartType;
    }
}
