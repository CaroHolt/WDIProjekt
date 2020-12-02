package fusers;
import model.Sight;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.LongestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class NameFuserLongestString extends AttributeValueFuser<String, Sight, Attribute> {

    public NameFuserLongestString() {
        super(new LongestString<Sight, Attribute>());
    }


    @Override
    public String getValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getName();
    }

    @Override
    public void fuse(RecordGroup<Sight, Attribute> recordGroup, Sight sight, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        //get the fused value
        FusedValue<String, Sight, Attribute> fused = getFusedValue(recordGroup, schemaCorrespondences, schemaElement);
    }

    @Override
    public boolean hasValue(Sight record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Sight.NAME);
    }
}
