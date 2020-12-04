package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.MostRecent;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

public class PopularityFuserMostRecent extends AttributeValueFuser<Byte, Sight, Attribute> {

    public PopularityFuserMostRecent() {
        super(new MostRecent<Byte, Sight, Attribute>());
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.POPULARITY);
    }

    @Override
    public Byte getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getPopularity();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> group, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Byte, Sight, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setPopularity(fused.getValue());
        fusedRecord.setAttributeProvenance(Sight.POPULARITY, fused.getOriginalIds());
    }

}