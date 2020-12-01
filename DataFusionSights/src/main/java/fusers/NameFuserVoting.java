package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class NameFuserVoting extends AttributeValueFuser<String, Sight, Attribute> {

    public NameFuserVoting(){ super(new Voting<String, Sight, Attribute>());}

    @Override
    public String getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getName();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<String, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
        fusedRecord.setName(fused.getValue());
        fusedRecord.setAttributeProvenance(Sight.NAME, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.NAME);
    }
}
