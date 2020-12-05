package fusers;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.Collection;


public class AverageByte <RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends
        ConflictResolutionFunction<Byte, RecordType, SchemaElementType> {

    @Override
    public FusedValue<Byte, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Byte, RecordType, SchemaElementType>> values) {

        if (values.size() == 0) {
            return new FusedValue<>((Byte) null);
        } else {

            int sum = 0;
            int count = 0;

            for (FusibleValue<Byte, RecordType, SchemaElementType> value : values) {
                sum += value.getValue();
                count++;
            }
            int average = sum / count;

            return new FusedValue<>((byte) average);

        }
    }

}