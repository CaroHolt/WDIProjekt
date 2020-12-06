package evaluation;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
        if (record1.getDescription() == null && record2.getDescription() == null)
            return true;
        else if (record1.getDescription() == null ^ record2.getDescription() == null)
            return false;
        else {
            // Normalize descriptions prior to comparison
            String s1 = record1.getDescription().replaceAll("\\p{Punct}", "").toLowerCase();
            String s2 = record2.getDescription().replaceAll("\\p{Punct}", "").toLowerCase();

            Set<String> tokens1 = splitIntoTokens(s1);
            Set<String> tokens2 = splitIntoTokens(s2);

            int counter = 0;
            for (String token : tokens2) {
                if (tokens1.contains(token)) {
                    counter = counter + 1;
                }
            }

            int setSize80Percent = (int) (tokens2.size() * 0.7);

            if (setSize80Percent <= counter) {
                return true;
            } else {
                return false;
            }
        }
    }

    private Set<String> splitIntoTokens(String s){
        Set<String> tokens = new HashSet<>();

        Scanner tokenize = new Scanner(s);
        while (tokenize.hasNext()) {
            tokens.add(tokenize.next());
        }
        return tokens;
    }


    @Override
    public boolean isEqual(Sight record1, Sight record2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(record1, record2, (Attribute)null);
    }
}
