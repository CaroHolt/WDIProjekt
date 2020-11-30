package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class SightXMLFormatter extends XMLFormatter<Sight>{

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("sights");
    }

    @Override
    public Element createElementFromRecord(Sight record, Document document) {

        Element sight = document.createElement("sight");

        sight.appendChild(createTextElement("id", record.getIdentifier(), document));

        sight.appendChild(createTextElementWithProvenance("name",
                record.getName(),
                record.getMergedAttributeProvenance(Sight.NAME), document));

        // Do we need provenance for description??
        if(record.getDescription() != null){
            sight.appendChild(createTextElementWithProvenance("description",
                    record.getDescription(),
                    record.getMergedAttributeProvenance(Sight.DESCRIPTION), document));
        }

        if(record.getCountry() != null) {
            sight.appendChild(createTextElementWithProvenance("country",
                    record.getCountry(),
                    record.getMergedAttributeProvenance(Sight.COUNTRY), document));
        }

        if(record.getCity() != null) {
            sight.appendChild(createTextElementWithProvenance("city",
                    record.getCity(),
                    record.getMergedAttributeProvenance(Sight.CITY), document));
        }

        if(record.getLongitude() != null) {
            sight.appendChild(createTextElementWithProvenance("longitude",
                    record.getLongitude().toString(),
                    record.getMergedAttributeProvenance(Sight.LONGITUDE), document));
        }

        if(record.getLatitude() != null) {
            sight.appendChild(createTextElementWithProvenance("latitude",
                    record.getLatitude().toString(),
                    record.getMergedAttributeProvenance(Sight.LATITUDE), document));
        }

        if(record.getPopularity() != null) {
            sight.appendChild(createTextElementWithProvenance("popularity",
                    record.getPopularity().toString(),
                    record.getMergedAttributeProvenance(Sight.POPULARITY), document));
        }

        sight.appendChild(createTypesElements(record, document));
        return sight;
    }

    protected Element createTextElementWithProvenance(String name,
                                                      String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

    protected Element createTypesElements(Sight record, Document document){
        Element typesRoot = document.createElement("types");
        for (String i : record.getTypes()){
            typesRoot.appendChild(createTextElement("type", i, document));
        }
        return typesRoot;
    }


}
