import java.io.File;


import Blocking.SightBlockingKeyByNameGenerator;
import Comparators.*;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import model.Sight;
import model.SightXMLReader;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.slf4j.Logger;

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
         *      - PAIR_OPEN_GEO
         *      - PAIR_OPEN_WIKI
         *      - PAIR_GEO_WIKI
         */
        MatchingPair selected = MatchingPair.PAIR_GEO_WIKI;


        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Sight, Attribute> dataset1 = new HashedDataSet<>();
        HashedDataSet<Sight, Attribute> dataset2 = new HashedDataSet<>();
        switch (selected){
            case PAIR_GEO_WIKI:
                new SightXMLReader().loadFromXML(new File("data/input/geonames.xml"), "/sights/sight", dataset1);
                new SightXMLReader().loadFromXML(new File("data/input/wikidata_deduplicated.xml"), "/sights/sight", dataset2);
                break;
            case PAIR_OPEN_WIKI:
                new SightXMLReader().loadFromXML(new File("data/input/opentripmap_deduplicated.xml"), "/sights/sight", dataset1);
                new SightXMLReader().loadFromXML(new File("data/input/wikidata_deduplicated.xml"), "/sights/sight", dataset2);
                break;
            case PAIR_OPEN_GEO:
                new SightXMLReader().loadFromXML(new File("data/input/opentripmap_deduplicated.xml"), "/sights/sight", dataset1);
                new SightXMLReader().loadFromXML(new File("data/input/geonames.xml"), "/sights/sight", dataset2);
                break;
        }

        // load the training set
        System.out.println("*\n*\tLoading goldstandard\n*");
        MatchingGoldStandard gsTrainset = new MatchingGoldStandard();
        switch (selected){
            case PAIR_GEO_WIKI:
                gsTrainset.loadFromCSVFile(new File("data/goldstandard/.csv"));
                break;
            case PAIR_OPEN_WIKI:
                gsTrainset.loadFromCSVFile(new File("data/goldstandard/goldstandard_wikidata_opentripmap.csv"));
                break;
            case PAIR_OPEN_GEO:
                gsTrainset.loadFromCSVFile(new File("data/goldstandard/.csv"));
                break;
        }

        // create a matching rules
        String options[] = new String[] { "-S" };
        String modelType = "SimpleLogistic"; // use a logistic regression
        WekaMatchingRule<Sight, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTrainset);

        // add comparators for opentrip and wikidata
        matchingRule.addComparator(new SightNameComparatorEqual());
        matchingRule.addComparator(new SightNameComparatorJaccard());
        matchingRule.addComparator(new SightNameComparatorLevenshtein());
        matchingRule.addComparator(new SightNameComparatorNGramJaccard());
        matchingRule.addComparator(new SightNameComparatorLowercaseJaccard());
        matchingRule.addComparator(new SightNameComparatorLowercasePunctuationJaccard());
        matchingRule.addComparator(new SightLocationComparator());
        matchingRule.addComparator(new SightLongitudeComparatorAbsDiff());

        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Sight, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataset1, dataset2, null, matchingRule, gsTrainset);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByNameGenerator());
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
            case PAIR_GEO_WIKI:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences.csv"), correspondences);
                break;
            case PAIR_OPEN_WIKI:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences.csv"), correspondences);
                break;
            case PAIR_OPEN_GEO:
                new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences.csv"), correspondences);
                break;
        }

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTestset = new MatchingGoldStandard();
        switch (selected){
            case PAIR_GEO_WIKI:
                gsTestset.loadFromCSVFile(new File("data/goldstandard/.csv"));
                break;
            case PAIR_OPEN_WIKI:
                gsTestset.loadFromCSVFile(new File("data/goldstandard/goldstandard_wikidata_opentripmap.csv"));
                break;
            case PAIR_OPEN_GEO:
                gsTestset.loadFromCSVFile(new File("data/goldstandard/.csv"));
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
    }

    public enum MatchingPair{
        PAIR_OPEN_GEO("OpenTripMap <-> Geonames"),
        PAIR_OPEN_WIKI("OpenTripMap <-> Wikidata"),
        PAIR_GEO_WIKI("Geonames <-> Wikidata");

        final String PAIRINFO;
        MatchingPair(String info) {
            PAIRINFO = info;
        }
    }

}
