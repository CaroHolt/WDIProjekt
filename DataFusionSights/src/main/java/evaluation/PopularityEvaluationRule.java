package evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import model.Sight;

public class PopularityEvaluationRule extends EvaluationRule<Sight, Attribute> {

    private static int acceptable_difference;

    @Override
    public boolean isEqual(Sight record1, Sight record2, Attribute schemaElement) {
        if(record1.getPopularity()== null && record2.getPopularity()==null)
            return true;
        else if(record1.getPopularity()== null ^ record2.getPopularity()==null)
            return false;
        else
            acceptable_difference = record1.getPopularity() - record2.getPopularity();
            return Math.abs(acceptable_difference) <= 1;
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }

}