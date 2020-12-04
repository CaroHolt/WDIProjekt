package evaluation;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import model.Sight;

public class DescriptionEvaluationRule extends EvaluationRule<Sight, Attribute> {

    SimilarityMeasure<String> sim = new TokenizingJaccardSimilarity();

    @Override
    public boolean isEqual(Sight record1, Sight record2, Attribute schemaElement) {

        return sim.calculate(record1.getDescription(), record2.getDescription()) == 1.0;
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }
}