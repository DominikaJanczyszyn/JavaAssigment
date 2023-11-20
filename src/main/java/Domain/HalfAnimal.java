package Domain;


public class HalfAnimal extends Product {
    public HalfAnimal(int regNo) {
        super(regNo);
    }

    public boolean ifContainsPart(String type)
    {
        for (int i = 0; i < getParts().size(); i++) {
            if(getParts().get(i).getPartType().equals(type))
                return true;
        }
        return false;
    }
}
