package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

public class LongitudeFuserFavorSource extends AttributeValueFuser<Float, Sight, Attribute> {

    public LongitudeFuserFavorSource() {
        super(new FavourSources<Float, Sight, Attribute>());
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.LONGITUDE);
    }

    @Override
    public Float getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getLongitude();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Float, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
        fusedRecord.setLongitude(fused.getValue());
        fusedRecord.setAttributeProvenance(Sight.LONGITUDE, fused.getOriginalIds());
    }

}