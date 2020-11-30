package model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.apache.jena.base.Sys;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SightXMLReader extends XMLMatchableReader<Sight, Attribute> {

    @Override
    protected void initialiseDataset(DataSet<Sight, Attribute> dataset) {
        super.initialiseDataset(dataset);

    }

    @Override
    public Sight createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node, "id");

        // create the object with id and provenance information
        Sight sight = new Sight(id, provenanceInfo);

        // fill the attributes
        sight.setName(getValueFromChildElement(node, "name"));
        sight.setDescription(getValueFromChildElement(node, "description"));
        sight.setCountry(getValueFromChildElement(node, "country"));
        sight.setCity(getValueFromChildElement(node, "city"));

        try{
            String latitude = getValueFromChildElement(node, "latitude");
            if(latitude != null && !latitude.isEmpty()){
                float fLatitude = Float.parseFloat(latitude);
                sight.setLatitude(fLatitude);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            String longitude = getValueFromChildElement(node, "longitude");
            if(longitude != null && !longitude.isEmpty()){
                float fLongitude = Float.parseFloat(longitude);
                sight.setLongitude(fLongitude);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            String popularity = getValueFromChildElement(node, "popularity");
            if(popularity != null && !popularity.isEmpty()){
                byte bPopularity = Byte.parseByte(popularity);
                sight.setPopularity(bPopularity);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        String[] types = getValueFromChildElement(node, "types").split("\\r?\\n");
        for(int i = 0; i < types.length; i++) {
            String type = types[i].replace("\t", "");
            types[i] = type;
        }
        List<String> types_list = Arrays.asList(types);
        sight.setTypes(types_list);

        return sight;
    }
}
