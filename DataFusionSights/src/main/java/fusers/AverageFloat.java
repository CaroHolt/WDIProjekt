package fusers;

import java.util.Collection;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import model.Sight;

public class AverageFloat<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<Float, RecordType, SchemaElementType> {

	@Override
	public FusedValue<Float, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Float, RecordType, SchemaElementType>> values) {
	
		if (values.size() == 0) {
			return new FusedValue<>((Float) 0.0f);
		} else {
		
			Float sum = 0.0f;
			Float count = 0.0f;
		
			for (FusibleValue<Float, RecordType, SchemaElementType> value : values) {
				sum += value.getValue();
				count++;
			}
		
			return new FusedValue<>(sum / count);
		
		}
	}
}
