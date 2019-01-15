package kz.shymkent.weatherapp;

public class City {
    public City(String city,String temperature){

    }
    public City(){

    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    private String city;
    private String temperature;
}
