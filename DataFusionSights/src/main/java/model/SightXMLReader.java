package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class SightXMLReader extends XMLMatchableReader<Sight, Attribute> implements FusibleFactory<Sight, Attribute> {

    @Override
    protected void initialiseDataset(DataSet<Sight, Attribute> dataset) {
        super.initialiseDataset(dataset);

        // the schema is defined in the Sight class and not interpreted from the file, so we have to set the attributes manually
        dataset.addAttribute(Sight.NAME);
        dataset.addAttribute(Sight.LATITUDE);
        dataset.addAttribute(Sight.CITY);
        dataset.addAttribute(Sight.COUNTRY);
        dataset.addAttribute(Sight.LONGITUDE);

    }

    @Override
    public Sight createModelFromElement(Node node, String provenanceInformation) {
        String id = getValueFromChildElement(node, "id");

        // Create sight object with id and provenance information
        Sight sight = new Sight(id, provenanceInformation);

        // Fill attribute values
        sight.setName(getValueFromChildElement(node, "name"));
        sight.setDescription(getValueFromChildElement(node, "description"));
        sight.setCountry(getValueFromChildElement(node, "country"));
        sight.setCity(getValueFromChildElement(node, "city"));

        // convert the latitude string into a float object
        try {
            String latitude = getValueFromChildElement(node, "latitude");
            if (latitude != null && !latitude.isEmpty()) {
                float floatLatitude=Float.parseFloat(latitude);
                sight.setLatitude(floatLatitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // convert the longitude string into a float object
        try {
            String longitude = getValueFromChildElement(node, "longitude");
            if (longitude != null && !longitude.isEmpty()) {
                float floatLongitude=Float.parseFloat(longitude);
                sight.setLongitude(floatLongitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // convert the popularity string into a byte object
        try {
            String popularity = getValueFromChildElement(node, "popularity");
            if (popularity != null && !popularity.isEmpty()) {
                Byte bytePopularity = Byte.parseByte(popularity);
                sight.setPopularity(bytePopularity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        // load the list of types
        List<String> types = getListFromChildElement(node, "types");
        sight.setTypes(types);

        return sight;
    }

    @Override
    public Sight createInstanceForFusion(RecordGroup<Sight, Attribute> recordGroup) {

        List<String> ids = new LinkedList<>();

        for (Sight s : recordGroup.getRecords()) {
            ids.add(s.getIdentifier());
        }

        Collections.sort(ids);

        String mergedId = StringUtils.join(ids, '+');

        return new Sight(mergedId, "fused");
    }

}
