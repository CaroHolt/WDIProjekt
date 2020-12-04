package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.IntersectionKSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

import java.util.List;

public class TypesFuserIntersectionKSources extends AttributeValueFuser<List<String>, Sight, Attribute> {

    /**
     *
     * @param k specifies the number of sources
     */
    public TypesFuserIntersectionKSources(int k) {
        super(new IntersectionKSources<String, Sight, Attribute>(k));
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.TYPES);
    }

    @Override
    public List<String> getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getTypes();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<List<String>, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
        fusedRecord.setTypes(fused.getValue());
        fusedRecord
                .setAttributeProvenance(Sight.TYPES, fused.getOriginalIds());
    }

}