package Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;
import model.Sight;

public class SightCountryComparator implements Comparator<Sight, Attribute> {


    private static final long serialVersionUID = 1L;
    private EqualsSimilarity<String> sim = new EqualsSimilarity<String>();

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Sight record1,
            Sight record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        String s1 = record1.getCountry();
        String s2 = record2.getCountry();

        double similarity = sim.calculate(s1, s2);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(s1);
            this.comparisonLog.setRecord2Value(s2);

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
