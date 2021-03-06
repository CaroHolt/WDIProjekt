package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
    private Float longitude;
    private Float latitude;
    private Byte popularity;

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

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Byte getPopularity() {
        return popularity;
    }

    public void setPopularity(Byte popularity) {
        this.popularity = popularity;
    }


    private Map<Attribute, Collection<String>> provenance = new HashMap<>();
    private Collection<String> recordProvenance;

    public void setRecordProvenance(Collection<String> provenance) {
        recordProvenance = provenance;
    }

    public Collection<String> getRecordProvenance() {
        return recordProvenance;
    }

    public void setAttributeProvenance(Attribute attribute,
                                       Collection<String> provenance) {
        this.provenance.put(attribute, provenance);
    }


    public Collection<String> getAttributeProvenance(String attribute) {
        return provenance.get(attribute);
    }

    public String getMergedAttributeProvenance(Attribute attribute) {
        Collection<String> prov = provenance.get(attribute);

        if (prov != null) {
            return StringUtils.join(prov, "+");
        } else {
            return "";
        }
    }


    public static final Attribute NAME = new Attribute("Name");
    public static final Attribute TYPES = new Attribute("Types");
    public static final Attribute DESCRIPTION = new Attribute("Description");
    public static final Attribute COUNTRY = new Attribute("Country");
    public static final Attribute CITY = new Attribute("City");
    public static final Attribute LONGITUDE = new Attribute("Longitude");
    public static final Attribute LATITUDE = new Attribute("Latitude");
    public static final Attribute POPULARITY = new Attribute("Popularity");


    @Override
    public boolean hasValue(Attribute attribute) {
        if(attribute==NAME)
            return getName() != null && !getName().isEmpty() && !getName().equals("None");
        else if(attribute==DESCRIPTION)
            return getDescription() != null && !getDescription().isEmpty() && !getDescription().equals("None");
        else if(attribute==TYPES)
            return getTypes() != null && getTypes().size() > 0;
        else if(attribute==COUNTRY)
            return getCountry() != null && !getCountry().isEmpty() && !getCountry().equals("None") && !getCountry().equals("Unknown");
        else if(attribute==CITY)
            return getCity() != null && !getCity().isEmpty() && !getCity().equals("None") && !getCity().equals("Unknown");
        else if(attribute==LONGITUDE)
            return getLongitude() != null && !getLongitude().equals("None");
        else if(attribute==LATITUDE)
            return getLatitude() != null && !getLatitude().equals("None");
        else if(attribute==POPULARITY)
            return getPopularity() != null && getPopularity() > 0;
        else
            return false;
    }

    @Override
    public String toString() {
        return String.format("[Sight %s: %s / %s / %s]", getIdentifier(), getName(),
                getLongitude().toString(), getLatitude().toString());
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