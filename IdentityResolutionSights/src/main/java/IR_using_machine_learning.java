import java.io.File;


import Blocking.SightBlockingKeyByNameGenerator;
import Comparators.*;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
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

    //private static final Logger logger = (Logger) WinterLogManager.activateLogger("default");

    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        /*HashedDataSet<Sight, Attribute> dataGeonames = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/geonames.xml"), "/sights/sight", dataGeonames);*/
        HashedDataSet<Sight, Attribute> dataOpentripmap = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/opentripmap_deduplicated.xml"), "/sights/sight", dataOpentripmap);
        HashedDataSet<Sight, Attribute> dataWikidata = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/wikidata_deduplicated.xml"), "/sights/sight", dataWikidata);

        // load the training set
        /*MatchingGoldStandard gsGEO_OPEN = new MatchingGoldStandard();
        gsGEO_OPEN.loadFromCSVFile(new File("data/goldstandard/.csv"));*/
        MatchingGoldStandard gsOPEN_WIKI = new MatchingGoldStandard();
        gsOPEN_WIKI.loadFromCSVFile(new File("data/goldstandard/goldstandard_wikidata_opentripmap.csv"));
        /*MatchingGoldStandard gsWIKI_GEO = new MatchingGoldStandard();
        gsWIKI_GEO.loadFromCSVFile(new File("data/goldstandard/.csv"));*/

        // create a matching rule for each goldstandard
        // create a matching rule for opentrip and geonames
        String options[] = new String[] { "-S" };
        String modelType = "SimpleLogistic"; // use a logistic regression
        /*WekaMatchingRule<Sight, Attribute> matchingRule_opentrip_geonames = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_opentrip_geonames.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsGEO_OPEN);

        // add comparators for Geonames and OpenTripMap
        matchingRule_opentrip_geonames.addComparator(new SightNameComparatorEqual());
        matchingRule_opentrip_geonames.addComparator(new SightNameComparatorJaccard());
        matchingRule_opentrip_geonames.addComparator(new SightNameComparatorLevenshtein());
        matchingRule_opentrip_geonames.addComparator(new SightNameComparatorNGramJaccard());
        matchingRule_opentrip_geonames.addComparator(new SightNameComparatorLowercaseJaccard());
        matchingRule_opentrip_geonames.addComparator(new SightNameComparatorLowercasePunctuationJaccard());
        matchingRule_opentrip_geonames.addComparator(new SightLocationComparator());
        matchingRule_opentrip_geonames.addComparator(new SightLongitudeComparatorAbsDiff());*/

        // create a matching rule for opentrip and wikidata
        WekaMatchingRule<Sight, Attribute> matchingRule_opentrip_wikidata = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_opentrip_wikidata.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsOPEN_WIKI);

        // add comparators for opentrip and wikidata
        matchingRule_opentrip_wikidata.addComparator(new SightNameComparatorEqual());
        matchingRule_opentrip_wikidata.addComparator(new SightNameComparatorJaccard());
        matchingRule_opentrip_wikidata.addComparator(new SightNameComparatorLevenshtein());
        matchingRule_opentrip_wikidata.addComparator(new SightNameComparatorNGramJaccard());
        matchingRule_opentrip_wikidata.addComparator(new SightNameComparatorLowercaseJaccard());
        matchingRule_opentrip_wikidata.addComparator(new SightNameComparatorLowercasePunctuationJaccard());
        matchingRule_opentrip_wikidata.addComparator(new SightLocationComparator());
        matchingRule_opentrip_wikidata.addComparator(new SightLongitudeComparatorAbsDiff());

        // create a matching rule for geonames and wikidata
        /*WekaMatchingRule<Sight, Attribute> matchingRule_geonames_wikidata = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_geonames_wikidata.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsWIKI_GEO);

        // add comparators for geonames and wikidata
        matchingRule_geonames_wikidata.addComparator(new SightNameComparatorEqual());
        matchingRule_geonames_wikidata.addComparator(new SightNameComparatorJaccard());
        matchingRule_geonames_wikidata.addComparator(new SightNameComparatorLevenshtein());
        matchingRule_geonames_wikidata.addComparator(new SightNameComparatorNGramJaccard());
        matchingRule_geonames_wikidata.addComparator(new SightNameComparatorLowercaseJaccard());
        matchingRule_geonames_wikidata.addComparator(new SightNameComparatorLowercasePunctuationJaccard());
        matchingRule_geonames_wikidata.addComparator(new SightLocationComparator());
        matchingRule_geonames_wikidata.addComparator(new SightLongitudeComparatorAbsDiff());*/

        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Sight, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataWikidata, dataOpentripmap, null, matchingRule_opentrip_wikidata, gsOPEN_WIKI);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule_opentrip_wikidata.getModelDescription()));

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByNameGenerator());
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Sight, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Sight, Attribute>> correspondences_wiki_geo = engine.runIdentityResolution(
                dataWikidata, dataOpentripmap, null, matchingRule_opentrip_wikidata,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences.csv"), correspondences_wiki_geo);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/goldstandard_wikidata_opentripmap.csv"));

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Sight, Attribute> evaluator = new MatchingEvaluator<Sight, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences_wiki_geo,
                gsTest);

        // print the evaluation result
        System.out.println("Academy Awards <-> Actors");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
