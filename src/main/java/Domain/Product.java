package Domain;

import java.util.ArrayList;

public abstract class Product {
    private int regNo;
    private ArrayList<AnimalPart> parts;

    public Product(int regNo)
    {
        this.regNo = regNo;
        this.parts = new ArrayList<>();
    }

    public int getRegNo() {return regNo;}

    public ArrayList<AnimalPart> getParts() {
        return parts;
    }
}
