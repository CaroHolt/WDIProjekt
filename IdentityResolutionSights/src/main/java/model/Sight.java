package model;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.List;

public class Sight implements Matchable {

    protected String id;
    protected String provenance;
    private String name;
    private List<String> types;
    private String description;
    private String country;
    private String city;
    private float longitude;
    private float latitude;
    private byte popularity;

    public Sight(String identifier, String provenance){
        id = identifier;
        this.provenance = provenance;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(byte popularity) {
        this.popularity = popularity;
    }


    @Override
    public String toString() {
        return String.format("[Movie %s: %s / %s / %s]", getIdentifier(), getName(), getLatitude(), getLongitude());
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Sight){
            return this.getIdentifier().equals(((Sight) obj).getIdentifier());
        }else
            return false;
    }


}
