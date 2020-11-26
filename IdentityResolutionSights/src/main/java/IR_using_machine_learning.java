import java.io.File;


import Blocking.SightBlockingKeyByNameGenerator;
import Comparators.*;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
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

    private static final Logger logger = (Logger) WinterLogManager.activateLogger("default");

    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Sight, Attribute> dataGeonames = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/geonames.xml"), "/sights/sight", dataGeonames);
        HashedDataSet<Sight, Attribute> dataOpentripmap = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/opentripmap.xml"), "/sights/sight", dataOpentripmap);
        HashedDataSet<Sight, Attribute> dataWikidata = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/wikidata.xml"), "/sights/sight", dataWikidata);

        // load the training set
        MatchingGoldStandard gsGEO_OPEN = new MatchingGoldStandard();
        gsGEO_OPEN.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));
        MatchingGoldStandard gsOPEN_WIKI = new MatchingGoldStandard();
        gsOPEN_WIKI.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));
        MatchingGoldStandard gsWIKI_GEO = new MatchingGoldStandard();
        gsWIKI_GEO.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors_training.csv"));

        // create a matching rule for each goldstandard
        // create a matching rule for opentrip and geonames
        String options[] = new String[] { "-S" };
        String modelType = "SimpleLogistic"; // use a logistic regression
        WekaMatchingRule<Sight, Attribute> matchingRule_opentrip_geonames = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_opentrip_geonames.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsGEO_OPEN);

        // add comparators for Geonames and OpenTripMap
        matchingRule_opentrip_geonames.addComparator(new MovieTitleComparatorEqual());
        matchingRule_opentrip_geonames.addComparator(new MovieDateComparator2Years());
        matchingRule_opentrip_geonames.addComparator(new MovieDateComparator10Years());
        matchingRule_opentrip_geonames.addComparator(new MovieDirectorComparatorJaccard());
        matchingRule_opentrip_geonames.addComparator(new MovieDirectorComparatorLevenshtein());
        matchingRule_opentrip_geonames.addComparator(new MovieDirectorComparatorLowerCaseJaccard());
        matchingRule_opentrip_geonames.addComparator(new MovieTitleComparatorLevenshtein());
        matchingRule_opentrip_geonames.addComparator(new MovieTitleComparatorJaccard());

        // create a matching rule for opentrip and wikidata
        WekaMatchingRule<Sight, Attribute> matchingRule_opentrip_wikidata = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_opentrip_wikidata.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsGEO_OPEN);

        // add comparators for opentrip and wikidata
        matchingRule_opentrip_wikidata.addComparator(new MovieTitleComparatorEqual());
        matchingRule_opentrip_wikidata.addComparator(new MovieDateComparator2Years());
        matchingRule_opentrip_wikidata.addComparator(new MovieDateComparator10Years());
        matchingRule_opentrip_wikidata.addComparator(new MovieDirectorComparatorJaccard());
        matchingRule_opentrip_wikidata.addComparator(new MovieDirectorComparatorLevenshtein());
        matchingRule_opentrip_wikidata.addComparator(new MovieDirectorComparatorLowerCaseJaccard());
        matchingRule_opentrip_wikidata.addComparator(new MovieTitleComparatorLevenshtein());
        matchingRule_opentrip_wikidata.addComparator(new MovieTitleComparatorJaccard());

        // create a matching rule for geonames and wikidata
        WekaMatchingRule<Sight, Attribute> matchingRule_geonames_wikidata = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_geonames_wikidata.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsGEO_OPEN);

        // add comparators for geonames and wikidata
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightNameComparatorEqual());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightNameComparatorJaccard());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightNameComparatorLevenshtein());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightNameComparatorNGramJaccard());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightNameComparatorLowercaseJaccard());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightNameComparatorLowercasePunctuationJaccard());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightLatitudeComparatorRound4());
        matchingRule_geonames_wikidata.addComparator(new Wiki_Geo_SightLongitudeComparatorAbsDiff());

        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Sight, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataGeonames, dataOpentripmap, null, matchingRule_geonames_wikidata, gsWIKI_GEO);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule_geonames_wikidata.getModelDescription()));

        // create a blocker (blocking strategy)
        StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByNameGenerator());
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Sight, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Sight, Attribute>> correspondences_wiki_geo = engine.runIdentityResolution(
                dataGeonames, dataOpentripmap, null, matchingRule_geonames_wikidata,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wiki_2_geo_correspondences.csv"), correspondences_wiki_geo);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/gs_academy_awards_2_actors_test.csv"));

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
