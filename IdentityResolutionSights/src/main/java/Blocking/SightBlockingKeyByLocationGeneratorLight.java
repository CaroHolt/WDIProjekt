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

public class SightBlockingKeyByLocationGeneratorLight extends RecordBlockingKeyGenerator<Sight, Attribute> {

    private static final long serialVersionUID = 1L;
    
	@Override
	public void generateBlockingKeys(Sight record, Processable<Correspondence<Attribute, Matchable>> correspondences,DataIterator<Pair<String, Sight>> resultCollector) {
		
		if(record.getLatitude() == null || record.getLongitude() == null) {
			resultCollector.next(new Pair<>("", record));
		}else {
			String location = record.getLatitude().toString().substring(0,3)+","+record.getLongitude().toString().substring(0,3);
			//String location = record.getLatitude().toString().substring(0,record.getLatitude().toString().indexOf('.'))+","+record.getLongitude().toString().substring(0,record.getLongitude().toString().indexOf('.'));  
			resultCollector.next(new Pair<>(location.toString(), record));
		}        
	}
}