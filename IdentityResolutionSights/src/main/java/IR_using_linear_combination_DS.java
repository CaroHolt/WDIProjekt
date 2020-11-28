import Blocking.SightBlockingKeyByCountryGenerator;
import Blocking.SightBlockingKeyByNameGenerator;
import Comparators.SightCityComparator;
import Comparators.SightLatitudeComparatorAbsDiff;
import Comparators.SightLatitudeComparatorAbsDiff4Decimals;
import Comparators.SightLongitudeComparatorAbsDiff;
import Comparators.SightLongitudeComparatorAbsDiff4Decimals;
import Comparators.SightNameComparatorEqual;
import Comparators.SightNameComparatorJaccard;
import Comparators.SightNameComparatorLevenshtein;
import Comparators.SightNameComparatorLowercaseJaccard;
import Comparators.SightNameComparatorLowercasePunctuationJaccard;
import Comparators.SightNameComparatorNGramJaccard;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
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

public class IR_using_linear_combination_DS {

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
    	// Add time measuring
    	long startTime = System.nanoTime();
    	
    	// loading data
        System.out.println("*\n*\tLoading datasets\n*");
        //HashedDataSet<Sight, Attribute> dataGeonames = new HashedDataSet<>();
        //new SightXMLReader().loadFromXML(new File("data/input/geonames.xml"), "/sights/sight", dataGeonames);
        HashedDataSet<Sight, Attribute> dataOpentripmap = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/opentripmap_deduplicated.xml"), "/sights/sight", dataOpentripmap);
        HashedDataSet<Sight, Attribute> dataWikidata = new HashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/wikidata_deduplicated.xml"), "/sights/sight", dataWikidata);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File("data/goldstandard/gs_wikidata_opentripmap_train.csv"));

        // create a matching rule
        LinearCombinationMatchingRule<Sight, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule_.csv", 1000, gsTest);

        // add comparators
                // FOR NAME
        //matchingRule.addComparator(new SightNameComparatorEqual(), 0.2);
        //matchingRule.addComparator(new SightNameComparatorJaccard(), 0.5);
        //matchingRule.addComparator(new SightNameComparatorLevenshtein(), 0.2);
        //matchingRule.addComparator(new SightNameComparatorLowercaseJaccard(), 0.5);
        //matchingRule.addComparator(new SightNameComparatorLowercasePunctuationJaccard(), 0.2);
        //matchingRule.addComparator(new SightNameComparatorNGramJaccard(), 1);
        
        	// FOR COORDINATES    
        //matchingRule.addComparator(new SightLongitudeComparatorAbsDiff(), 0.5);
        //matchingRule.addComparator(new SightLongitudeComparatorAbsDiff4Decimals(), 1);
        //matchingRule.addComparator(new SightLatitudeComparatorAbsDiff(), 0.4);
        //matchingRule.addComparator(new SightLatitudeComparatorAbsDiff4Decimals(), 0.4);
        matchingRule.addComparator(new SightCityComparator(), 1);
        
        
        // create a blocker (blocking strategy)
        StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByNameGenerator());
        
//      StandardRecordBlocker<Sight, Attribute> blocker = new StandardRecordBlocker<Sight, Attribute>(new SightBlockingKeyByCountryGenerator());
//		NoBlocker<Sight, Attribute> blocker = new NoBlocker<>();
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByTitleGenerator(), 1);      
        blocker.setMeasureBlockSizes(true);
        //Write debug results to file:
        blocker.collectBlockSizeData("data/output/debugResultsBlocking_wikidata_2_otm.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Sight, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Sight, Attribute>> correspondences = engine.runIdentityResolution(
                dataWikidata, dataOpentripmap, null, matchingRule,
                blocker);

        // Create a top-1 global matching
//		  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

//		 Alternative: Create a maximum-weight, bipartite matching
//		 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//		 maxWeight.run();
//		 correspondences = maxWeight.getResult();

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/wikidata_2_otm_correspondences.csv"), correspondences);



        System.out.println("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Sight, Attribute> evaluator = new MatchingEvaluator<Sight, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        System.out.println("wikiData <-> OTM");
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
}
