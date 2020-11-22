package model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;


public class Sight extends AbstractRecord<Attribute> implements Serializable{

    private static final long serialVersionUID = 1L;

    public Sight(String identifier, String provenance) {
        super(identifier, provenance);
    }

    private String name;
    private List<String> types;
    private String description;
    private String country;
    private String city;
    private float longitude;
    private float latitude;
    private byte popularity;

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

    public static final Attribute NAME = new Attribute("Name");
    public static final Attribute DESCRIPTION = new Attribute("Description");
    public static final Attribute TYPES = new Attribute("Types");
    public static final Attribute COUNTRY = new Attribute("Country");
    public static final Attribute CITY = new Attribute("City");
    public static final Attribute LONGITUDE = new Attribute("Longitude");
    public static final Attribute LATITUDE = new Attribute("Latitude");
    public static final Attribute POPULARITY = new Attribute("Popularity");



    @Override
    public boolean hasValue(Attribute attribute) {
        if(attribute==NAME)
            return getName() != null && !getName().isEmpty();
        else if(attribute==DESCRIPTION)
            return getDescription() != null && !getDescription().isEmpty();
        else if(attribute==TYPES)
            return getTypes() != null && getTypes().size() > 0;
        else if(attribute==COUNTRY)
            return getCountry() != null && !getCountry().isEmpty();
        else if(attribute==CITY)
            return getCity() != null && !getCity().isEmpty();
        else if(attribute==LONGITUDE)
            return getLongitude() != 0.0;
        else if(attribute==LATITUDE)
            return getLatitude() != 0.0;
        else if(attribute==POPULARITY)
            return getPopularity() != 0;
        else
            return false;
    }
}