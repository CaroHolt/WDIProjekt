import Blocking.*;
import Comparators.*;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import model.Sight;
import model.SightXMLReader;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class IR_using_linear_combination {

    /*
     * Logging Options:
     * 		default: 	level INFO	- console
     * 		trace:		level TRACE - console
     * 		infoFile:	level INFO	- console/file
     * 		traceFile:	level TRACE	- console/file
     *
     * To set the log level to trace and write the log to winter.log and console,
     * activate the "traceFile" logger as follows:
     *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
     *
     */

    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main( String[] args ) throws Exception
    {
        /* Define Data pair that should be matched
         * The options are:
         *      - PAIR_WIKI_GEO
         *      - PAIR_WIKI_OTM
         *      - PAIR_OTM_GEO
         */
        IR_using_machine_learning.MatchingPair selected = IR_using_machine_learning.MatchingPair.PAIR_WIKI_GEO;

        // Add time measuring
        long startTime = System.nanoTime();

        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Sight, Attribute> dataset1 = new HashedDataSet<>();
        HashedDataSet<Sight, Attribute> dataset2 = new HashedDataSet<>();
        switch (selected){
            case PAIR_WIKI_GEO:
                new SightXMLReader().loadFromXML(new File("data/input/wikidata_deduplicated.xml"), "/sights/sight", dataset1);
                new SightXMLReader().loadFromXML(new File("data/input/geonames_sampled.xml"), "/sights/sight", dataset2);
                break;
            case PAIR_WIKI_OTM:
                new SightXMLReader().loadFromXML(new File("data/input/wikidata_deduplicated.xml"), "/sights/sight", dataset1);
                new SightXMLReader().loadFromXML(new File("data/input/opentripmap_deduplicated.xml"), "/sights/sight", dataset2);
                break;
            case PAIR_OTM_GEO:
                new SightXMLReader().loadFromXML(new File("data/input/opentripmap_deduplicated.xml"), "/sights/sight", dataset1);
                new SightXMLReader().loadFromXML(new File("data/input/geonames_sampled.xml"), "/sights/sight", dataset2);
                break;
        }

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        switch (selected){
            case PAIR_WIKI_GEO:
                gsTest.loadFromCSVFile(new File("data/goldstandard/gs_wiki_geo_test.csv"));
                break;
            case PAIR_WIKI_OTM:
                gsTest.loadFromCSVFile(new File("data/goldstandard/gs_wikidata_opentripmap_test.csv"));
                break;
            case PAIR_OTM_GEO:
                gsTest.loadFromCSVFile(new File("data/goldstandard/gs_opentripmap_geonames_test.csv"));
                break;
        }

        // create a matching rule
        LinearCombinationMatchingRule<Sight, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTest);

        // add comparators
        // Matchers for Name
        //matchingRule.addComparator(new SightNameComparatorEqual(), 0.5);
        //matchingRule.addComparator(new SightNameComparatorJaccard(), 0.5);
        //matchingRule.addComparator(new SightNameComparatorLevenshtein(), 0.2);
        matchingRule.addComparator(new SightNameComparatorLowercaseJaccard(), 0.3);
        //matchingRule.addComparator(new SightNameComparatorLowercasePunctuationJaccard(), 0.2);
        matchingRule.addComparator(new SightNameComparatorNGramJaccard(), 0.3);

        // Matchers for COORDINATES
        //matchingRule.addComparator(new SightLongitudeComparatorAbsDiff(), 0.5);
        matchingRule.addComparator(new SightLongitudeComparatorAbsDiff4Decimals(), 0.2);
        //matchingRule.addComparator(new SightLatitudeComparatorAbsDiff(), 0.5);
        matchingRule.addComparator(new SightLatitudeComparatorAbsDiff4Decimals(), 0.2);
        //matchingRule.addComparator(new SightLocationComparator(), 0.5);

        // Matchers for FOR COUNTRY
        // matchingRule.addComparator(new SightCountryComparator(), 0.1);
        // Matchers for FOR CITY
        //matchingRule.addComparator(new SightCityComparator(), 0.1);
        // matchingRule.addComparator(new SightCityComparatorTokenJaccard(), 0.2);

        // create a blocker (blocking strategy)
        //NoBlocker<Sight, Attribute> blocker = new NoBlocker<>();
        //StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByNameGenerator());
        //StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByCountryGenerator());
		//StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByCityGenerator());
        StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByLocationGenerator());
        //StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByLocationGeneratorLight());
        blocker.setMeasureBlockSizes(true);

        //Write debug results to file:
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Sight, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Sight, Attribute>> correspondences = engine.runIdentityResolution(
                dataset1, dataset2, null, matchingRule,
                blocker);

        // Create a top-1 global matching
        //correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

        //Alternative: Create a maximum-weight, bipartite matching
        MaximumBipartiteMatchingAlgorithm<Sight,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
        maxWeight.run();
        correspondences = maxWeight.getResult();

        // write the correspondences to the output file
        switch (selected){
            case PAIR_WIKI_GEO:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences.csv"), correspondences);
                break;
            case PAIR_WIKI_OTM:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_otm_correspondences.csv"), correspondences);
                break;
            case PAIR_OTM_GEO:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/otm_2_geo_correspondences.csv"), correspondences);
                break;
        }



        System.out.println("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Sight, Attribute> evaluator = new MatchingEvaluator<Sight, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        System.out.println(selected.PAIRINFO);
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));

        long stopTime = System.nanoTime();
        // Print execution time in ms
        System.out.println("required time: " + (stopTime - startTime) / 1000000 + " ms");
    }

    public enum MatchingPair{
    	PAIR_WIKI_GEO("Wikidata <-> Geonames"),
    	PAIR_WIKI_OTM("Wikidata <-> OpenTripMap"),
    	PAIR_OTM_GEO("OpenTripMap <-> Geonames");
        
        final String PAIRINFO;
        MatchingPair(String info) {
            PAIRINFO = info;
        }
    }
}
