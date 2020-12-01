package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

public class LatitudeFuserVoting extends AttributeValueFuser<Float, Sight, Attribute> {

    public LatitudeFuserVoting() { super(new Voting<Float, Sight, Attribute>());}


    @Override
    public Float getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getLatitude();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Float, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
        fusedRecord.setLatitude(fused.getValue());
        fusedRecord.setAttributeProvenance(Sight.LATITUDE, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.LATITUDE);
    }
}