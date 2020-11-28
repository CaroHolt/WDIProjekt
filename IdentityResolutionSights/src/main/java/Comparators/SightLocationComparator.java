package Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import model.Sight;

public class SightLocationComparator implements Comparator <Sight, Attribute>{

    private static final long serialVersionUID = 1L;

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Sight record1,
            Sight record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        Double doubleLat1 = (double) record1.getLatitude();
        Double doubleLat2 = (double) record2.getLatitude();
        Double doubleLong1 = (double) record1.getLongitude();
        Double doubleLong2 = (double) record2.getLongitude();

        double similarity;
        Double dist = 0.0;

        if (doubleLat1 != null && doubleLat2 != null && doubleLong1 != null && doubleLong2 != null) {
            dist = distance(doubleLat1, doubleLong1, doubleLat2, doubleLong2);
            if (dist < 0.1) {
                similarity = 1.0;
            } else if (dist < 0.3) {
                similarity = 0.8;
            } else if (dist < 0.5) {
                similarity = 0.6;
            } else if (dist < 1.0) {
                similarity = 0.4;
            } else {
                similarity = 0.0;
            }

            if (this.comparisonLog != null) {
                this.comparisonLog.setComparatorName(getClass().getName());

                this.comparisonLog.setRecord1Value(doubleLat1.toString() + " & " + doubleLong1.toString());
                this.comparisonLog.setRecord2Value(doubleLat2.toString() + " & " + doubleLong2.toString());

                this.comparisonLog.setSimilarity(Double.toString(similarity));
            }
        }else{
            similarity = 0.0;
        }
        return similarity;

    }

    /**
     * Source:
     * https://stackoverflow.com/questions/18170131/comparing-two-locations-using-their-longitude-and-latitude
     */
    /** calculates the distance between two locations in MILES so we converted to kilometers*/
    private double distance(double lat1, double long1, double lat2, double long2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(long2-long1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        double distInKM = dist * 1.6;

        return distInKM; // output distance, in MILES
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
