package evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import model.Sight;

import java.text.DecimalFormat;

public class LatitudeEvaluationRule extends EvaluationRule<Sight, Attribute> {


    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public boolean isEqual(Sight record1, Sight record2, Attribute schemaElement) {
        if(record1.getLatitude()==0.0 && record2.getLatitude()==0.0)
            return true;
        else if(record1.getLatitude()==0.0 ^ record2.getLatitude()==0.0)
            return false;
        else
            return df.format(record1.getLatitude()) == df.format(record1.getLatitude());
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute)null);
    }
}
