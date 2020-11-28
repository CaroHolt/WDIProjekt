package Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;
import model.Sight;

public class SightLatitudeComparatorAbsDiff implements Comparator<Sight, Attribute> {

    private static final long serialVersionUID = 1L;
    private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(0.5);

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Sight record1,
            Sight record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        double roundedRecord1 = record1.getLatitude();
        double roundedRecord2 = record2.getLatitude();
        
        double similarity = sim.calculate(roundedRecord1, roundedRecord2);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Double.toString(roundedRecord1));
            this.comparisonLog.setRecord2Value(Double.toString(roundedRecord2));

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        return similarity;

    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }
}
