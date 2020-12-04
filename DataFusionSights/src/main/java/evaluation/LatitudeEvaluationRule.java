package evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import model.Sight;

import java.text.DecimalFormat;

public class LatitudeEvaluationRule extends EvaluationRule<Sight, Attribute> {

    private static Float acceptable_difference;

    @Override
    public boolean isEqual(Sight record1, Sight record2, Attribute schemaElement) {
        if(record1.getLatitude()==null && record2.getLatitude()==null)
            return true;
        else if(record1.getLatitude()==null ^ record2.getLatitude()==null)
            return false;
        else{
            acceptable_difference = record1.getLatitude() - record2.getLatitude();
            return Math.abs(acceptable_difference) <= 0.01;

        }
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute)null);
    }
}
