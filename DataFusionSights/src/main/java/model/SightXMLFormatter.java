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

        //...



        return sight;
    }

    protected Element createTextElementWithProvenance(String name,
                                                      String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }


}
