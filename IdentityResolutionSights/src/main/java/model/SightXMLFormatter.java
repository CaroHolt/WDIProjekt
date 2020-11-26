package model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SightXMLFormatter extends XMLFormatter<Sight> {


    @Override
    public Element createRootElement(Document document) {
        return document.createElement("sights");
    }

    @Override
    public Element createElementFromRecord(Sight record, Document document) {
        Element sight = document.createElement("sight");

        sight.appendChild(createTextElement("id", record.getIdentifier(), document));

        sight.appendChild(createTextElement("name", record.getName(), document));

        return sight;
    }

    protected Element createTextElementWithProvenance(String name,
                                                      String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

}
