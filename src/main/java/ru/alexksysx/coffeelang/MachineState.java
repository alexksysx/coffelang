package ru.alexksysx.coffeelang;

public class MachineState {
    private static final MachineState INSTANCE = new MachineState();
    private Double waterTemperature = 30D;
    private Double pressure = 9D;
    private Double coffeeGroundAmount = 0D;
    private Double coffeeMadeAmount = 0D;
    private Boolean holderInserted = false;
    private Boolean cupReady = false;
    private String holderType = "одинарный холдер";

    public static MachineState getInstance() {
        return INSTANCE;
    }

    public void restoreState() {
        Double waterTemperature = 30D;
        Double pressure = 9D;
        Double coffeeGroundAmount = 0D;
        Double coffeeMadeAmount = 0D;
        holderInserted = false;
        cupReady = false;
        holderType = "одинарный холдер";
    }

    public void setWaterTemperature(Double temperature) {
        this.waterTemperature = temperature;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public void addGroundCoffeeAmount(Double weight) {
        coffeeGroundAmount += weight;
    }

    public void addMadeCoffeeAmount(Double weight) {
        coffeeMadeAmount += weight;
    }

    public void insertHolder() {
        holderInserted = true;
    }

    public void removeHolder() {
        holderInserted = false;
    }

    public Double getWaterTemperature() {
        return waterTemperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getCoffeeGroundAmount() {
        return coffeeGroundAmount;
    }

    public Double getCoffeeMadeAmount() {
        return coffeeMadeAmount;
    }

    public Boolean getHolderInserted() {
        return holderInserted;
    }

    public String getHolderType() {
        return holderType;
    }

    public void setHolderType(String holderType) {
        this.holderType = holderType;
    }

    public Boolean getCupReady() {
        return cupReady;
    }

    public void setCupReady() {
        this.cupReady = true;
    }
}
