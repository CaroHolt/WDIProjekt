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

public class SightBlockingKeyByNameGenerator extends RecordBlockingKeyGenerator<Sight, Attribute> {

    private static final long serialVersionUID = 1L;

    @Override
    public void generateBlockingKeys(Sight record, Processable<Correspondence<Attribute, Matchable>> processable, DataIterator<Pair<String, Sight>> dataIterator) {

        String name  = record.getName();
        
        //System.out.println(name);
        // Remove blanks & lower-case string
        //String name_prep = name.toLowerCase();
        
        //String blockingKeyValue = "";

        //String blockingKeyValue = "";

        dataIterator.next(new Pair<>(name, record));

    }
}
