package model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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


        return sight;
    }
}
