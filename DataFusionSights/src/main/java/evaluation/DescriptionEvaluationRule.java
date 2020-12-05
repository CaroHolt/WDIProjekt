package evaluation;

import java.text.Normalizer;

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

    	// Normalize descriptions prior to comparison
		String s1 = Normalizer.normalize(record1.getDescription(), Normalizer.Form.NFD)
				.replaceAll("\\s+", "").replace("'", "").replace(".", "").trim().toLowerCase();
		String s2 = Normalizer.normalize(record2.getDescription(), Normalizer.Form.NFD)
				.replaceAll("\\s+", "").replace("'", "").replace(".", "").trim().toLowerCase();
		
		return sim.calculate(s1, s2) == 1.0;
    }

    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }
}
