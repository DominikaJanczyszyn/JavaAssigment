package Dto;

public class TrayCreationDto {
    private double weightCapacity;
    private String partType;

    public TrayCreationDto(double weightCapacity, String partType) {
        this.weightCapacity = weightCapacity;
        this.partType = partType;
    }
    
    public double getWeightCapacity() {
        return weightCapacity;
    }

    public String getPartType() {
        return partType;
    }
}
