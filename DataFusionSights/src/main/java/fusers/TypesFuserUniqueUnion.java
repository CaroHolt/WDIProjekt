package fusers;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Sight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TypesFuserUniqueUnion extends AttributeValueFuser<List<String>, Sight, Attribute> {

    public TypesFuserUniqueUnion() {super(new Union<String, Sight, Attribute>());}

    @Override
    public List<String> getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getTypes();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<List<String>, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
        List<String> listWithDuplicates= fused.getValue();
        List<String> listWithoutDuplicates = new ArrayList<String>(new HashSet<>(listWithDuplicates));
        listWithoutDuplicates.remove("");
        listWithoutDuplicates.remove(null);
        fusedRecord.setTypes(listWithoutDuplicates);
        fusedRecord.setAttributeProvenance(Sight.TYPES, fused.getOriginalIds());
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.TYPES);
    }
}
