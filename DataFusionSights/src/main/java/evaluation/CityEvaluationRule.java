package evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import model.Sight;

public class CityEvaluationRule extends EvaluationRule<Sight, Attribute> {

    @Override
    public boolean isEqual(Sight record1, Sight record2, Attribute schemaElement) {
        if(record1.getCity()== null && record2.getCity()==null)
            return true;
        else if(record1.getCity()== null ^ record2.getCity()==null)
            return false;
        else
            return record1.getCity().equals(record2.getCity());
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }

}
