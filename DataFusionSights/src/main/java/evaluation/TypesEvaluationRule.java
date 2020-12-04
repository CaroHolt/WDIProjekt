package evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import model.Sight;

import java.util.HashSet;
import java.util.Set;

public class TypesEvaluationRule extends EvaluationRule<Sight, Attribute> {
    @Override
    public boolean isEqual(Sight record1, Sight record2, Attribute attribute) {

        Set<String> types1 = new HashSet<>();
        Set<String> types2 = new HashSet<>();

        for (String typeName1 : record1.getTypes()){
            if (typeName1 != null && typeName1 != ""){
                types1.add(typeName1);
            }
        }

        for (String typeName2 : record2.getTypes()){
            if (typeName2 != null && typeName2 != ""){
                types2.add(typeName2);
            }
        }

        return types1.containsAll(types2) && types2.containsAll(types1);
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute)null);
    }
}
