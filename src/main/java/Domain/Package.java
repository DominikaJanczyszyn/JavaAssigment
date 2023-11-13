package Domain;

import java.util.ArrayList;

public class Package {
    private int regNo;
    private String animalPartType;
    private int maxNrOfParts;
    private ArrayList<AnimalPart> parts;

    public Package(int regNo ,String animalPartType, int maxNrOfParts) {
        this.regNo = regNo;
        this.animalPartType = animalPartType;
        this.maxNrOfParts = maxNrOfParts;
        this.parts = new ArrayList<>();
    }

    public void addAnimalPart(AnimalPart animalPart) throws Exception
    {
        if (parts.size() != maxNrOfParts)
            parts.add(animalPart);
        throw new Exception("Maximum number of parts is the package reached.");
    }

    public int getRegNo() {return regNo;}

    public String getAnimalPartType() {
        return animalPartType;
    }

    public ArrayList<AnimalPart> getParts() {
        return parts;
    }
}
