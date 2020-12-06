import java.io.File;


import Blocking.*;
import Comparators.*;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import model.Sight;
import model.SightXMLReader;
import org.apache.logging.log4j.Logger;

public class IR_using_machine_learning {
    /*
     * Logging Options:
     * 		default: 	level INFO	- console
     * 		trace:		level TRACE     - console
     * 		infoFile:	level INFO	- console/file
     * 		traceFile:	level TRACE	- console/file
     *
     * To set the log level to trace and write the log to winter.log and console,
     * activate the "traceFile" logger as follows:
     *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
     *
     */

    private static final Logger logger = (Logger) WinterLogManager.activateLogger("default");

    public static void main( String[] args ) throws Exception
    {
        /* Define Data pair that should be matched
         * The options are:
         *      - PAIR_WIKI_GEO
         *      - PAIR_WIKI_OTM
         *      - PAIR_OTM_GEO
         */
        MatchingPair selected = MatchingPair.PAIR_WIKI_GEO;

        /* Define Blocking strategy that should be applied
         * The options are:
         *      - NAME_BLOCKING
         *      - CITY_BLOCKING
         *      - COUNTRY_BLOCKING
         *      - LOCATION_BLOCKING
         *      - LOCATION_3DEC_BLOCKING
         */
        BlockingStrategy blockingStrategy = BlockingStrategy.LOCATION_3DEC_BLOCKING;

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

        // load the training set
        System.out.println("*\n*\tLoading goldstandard\n*");
        MatchingGoldStandard gsTrainset = new MatchingGoldStandard();
        switch (selected){
            case PAIR_WIKI_GEO:
                gsTrainset.loadFromCSVFile(new File("data/goldstandard/gs_wiki_geo_train.csv"));
                break;
            case PAIR_WIKI_OTM:
                gsTrainset.loadFromCSVFile(new File("data/goldstandard/gs_wikidata_opentripmap_train.csv"));
                break;
            case PAIR_OTM_GEO:
                gsTrainset.loadFromCSVFile(new File("data/goldstandard/gs_opentripmap_geonames_train.csv"));
                break;
        }

        // create a matching rules
        String options[] = new String[] { "-S" };
        String modelType = "SimpleLogistic"; // use a logistic regression
        WekaMatchingRule<Sight, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTrainset);

        // add comparators
        // Matchers for Name
        matchingRule.addComparator(new SightNameComparatorEqual());
        matchingRule.addComparator(new SightNameComparatorJaccard());
        matchingRule.addComparator(new SightNameComparatorLevenshtein());
        matchingRule.addComparator(new SightNameComparatorLowercasePunctuationJaccard());
        matchingRule.addComparator(new SightNameComparatorNGramJaccard());

        // Matchers for COORDINATES
        matchingRule.addComparator(new SightLongitudeComparatorAbsDiff());
        matchingRule.addComparator(new SightLongitudeComparatorAbsDiff4Decimals());
        matchingRule.addComparator(new SightLatitudeComparatorAbsDiff());
        matchingRule.addComparator(new SightLatitudeComparatorAbsDiff4Decimals());
        matchingRule.addComparator(new SightLocationComparator());

        // Matchers for FOR COUNTRY
        matchingRule.addComparator(new SightCountryComparator());
        // Matchers for FOR CITY
        matchingRule.addComparator(new SightCityComparator());
        matchingRule.addComparator(new SightCityComparatorTokenJaccard());


        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Sight, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataset1, dataset2, null, matchingRule, gsTrainset);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Sight, Attribute> blocker;
        switch (blockingStrategy){
            case NAME_BLOCKING:
                blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByNameGenerator());
                break;
            case CITY_BLOCKING:
                blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByCityGenerator());
                break;
            case COUNTRY_BLOCKING:
                blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByCountryGenerator());
                break;
            case LOCATION_BLOCKING:
                blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByLocationGenerator());
                break;
            case LOCATION_3DEC_BLOCKING:
                blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByLocationGeneratorLight());
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + blockingStrategy);
        }

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

        // write the correspondences to the output file
        switch (selected){
            case PAIR_WIKI_GEO:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences_ml.csv"), correspondences);
                break;
            case PAIR_WIKI_OTM:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_otm_correspondences_ml.csv"), correspondences);
                break;
            case PAIR_OTM_GEO:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/otm_2_geo_correspondences_ml.csv"), correspondences);
                break;
        }

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTestset = new MatchingGoldStandard();
        switch (selected){
            case PAIR_WIKI_GEO:
                gsTestset.loadFromCSVFile(new File("data/goldstandard/gs_wiki_geo_test.csv"));
                break;
            case PAIR_WIKI_OTM:
                gsTestset.loadFromCSVFile(new File("data/goldstandard/gs_wikidata_opentripmap_test.csv"));
                break;
            case PAIR_OTM_GEO:
                gsTestset.loadFromCSVFile(new File("data/goldstandard/gs_opentripmap_geonames_test.csv"));
                break;
        }

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Sight, Attribute> evaluator = new MatchingEvaluator<Sight, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences, gsTestset);

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

    public enum BlockingStrategy{
        NO_BLOCKING,
        NAME_BLOCKING,
        CITY_BLOCKING,
        COUNTRY_BLOCKING,
        LOCATION_BLOCKING,
        LOCATION_3DEC_BLOCKING;
    }

}
