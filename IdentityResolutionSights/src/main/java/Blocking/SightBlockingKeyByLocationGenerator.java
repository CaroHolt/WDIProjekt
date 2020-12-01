package Blocking;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;

import java.text.Normalizer;

import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.BlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

public class SightBlockingKeyByLocationGenerator extends RecordBlockingKeyGenerator<Sight, Attribute> {

    private static final long serialVersionUID = 1L;

    @Override
    public void generateBlockingKeys(Sight record, Processable<Correspondence<Attribute, Matchable>> processable, DataIterator<Pair<String, Sight>> dataIterator) {

	    if(record.getLatitude() == 0.0f || record.getLongitude() == 0.0f) {
			dataIterator.next(new Pair<>("", record));
		} else {
			String location = String.valueOf(record.getLatitude()).substring(0, String.valueOf(record.getLatitude()).indexOf('.')) + "," + String.valueOf(record.getLongitude()).substring(0, String.valueOf(record.getLongitude()).indexOf('.'));  
			dataIterator.next(new Pair<>(location.toString(), record));
		} 
    }
}