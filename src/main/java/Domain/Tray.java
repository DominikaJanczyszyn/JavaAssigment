package Domain;

import java.util.ArrayList;

public class Tray {
    private int id;
    private double weightCapacity;
    private String partType;
    private ArrayList<AnimalPart> parts;

    public Tray(int id, double weightCapacity, String partType) {
        this.id = id;
        this.weightCapacity = weightCapacity;
        this.partType = partType;
        this.parts = new ArrayList<>();
    }

    public double getWeightCapacity() {
        return weightCapacity;
    }

    public String getPartType() {
        return partType;
    }

    public ArrayList<AnimalPart> getParts() {
        return parts;
    }

    public void setParts(ArrayList<AnimalPart> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return "Tray{" +
                "id=" + id +
                ", weightCapacity=" + weightCapacity +
                ", partType='" + partType + '\'' +
                ", parts=" + parts +
                '}';
    }
}
