package Blocking;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;

import java.text.Normalizer;

import org.apache.commons.lang3.StringEscapeUtils;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.BlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

public class SightBlockingKeyByNameFirstLetterGenerator extends RecordBlockingKeyGenerator<Sight, Attribute> {

    private static final long serialVersionUID = 1L;

    @Override
    public void generateBlockingKeys(Sight record, Processable<Correspondence<Attribute, Matchable>> processable, DataIterator<Pair<String, Sight>> dataIterator) {

    	
    	// Check for empty values
    	String name = record.getName();
    	
    	if (name == null) {
        	name = "";
        }       	
    	
    	// normalize name
    	name = Normalizer.normalize(StringEscapeUtils.unescapeHtml4(name), Normalizer.Form.NFD).
    					replaceAll("[^\\p{ASCII}]", "").replace("'", "").replace(".", "").toLowerCase().replaceAll("\\s+", " ").trim();
    	
    	// extract first letter
    	String firstletter = "";
    		try {
    			firstletter = name.substring(0, 1);
    		} catch (Exception e) {
    				
    		}


    	// create pair
    	dataIterator.next(new Pair<>(firstletter, record));

    }
}
