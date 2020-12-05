package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.util.List;

public class TypesFuserUnion extends AttributeValueFuser<List<String>, Sight, Attribute> {

    public TypesFuserUnion() {super(new Union<String, Sight, Attribute>());}

    @Override
    public List<String> getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getTypes();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<List<String>, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
        // Remove empty strings
        List<String> tmpList= fused.getValue();
        tmpList.remove("");
        fusedRecord.setTypes(tmpList);
        fusedRecord.setAttributeProvenance(Sight.TYPES, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.TYPES);
    }
}
