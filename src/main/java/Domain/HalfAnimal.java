package Domain;

import java.util.ArrayList;

public class HalfAnimal {
    private int regNo;
    private ArrayList<AnimalPart> parts;

    public HalfAnimal(int regNo) {
        this.regNo = regNo;
        this.parts = new ArrayList<>();
    }

    public boolean ifContainsPart(String type)
    {
        for (int i = 0; i < parts.size(); i++) {
            if(parts.get(i).getPartType().equals(type))
                return true;
        }
        return false;
    }
}
