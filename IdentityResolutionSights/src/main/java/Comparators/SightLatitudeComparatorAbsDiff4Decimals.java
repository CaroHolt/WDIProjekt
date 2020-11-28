package Comparators;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;
import model.Sight;

public class SightLatitudeComparatorAbsDiff4Decimals implements Comparator<Sight, Attribute> {

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
        
        // Set new decimal format and decimal separator
        DecimalFormat df = new DecimalFormat("#.####");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        
        // Cut double to only contain 4 decimal places
        double roundedRecord1_cut = Double.parseDouble(df.format(roundedRecord1));
        double roundedRecord2_cut = Double.parseDouble(df.format(roundedRecord2));
 
        double similarity = sim.calculate(roundedRecord1_cut, roundedRecord2_cut);

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Double.toString(roundedRecord1_cut));
            this.comparisonLog.setRecord2Value(Double.toString(roundedRecord2_cut));

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
