
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import evaluation.*;
import model.Sight;
import model.SightXMLFormatter;
import model.SightXMLReader;
import org.apache.logging.log4j.Logger;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class SightFusion_Main {
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

    private static final Logger logger = WinterLogManager.activateLogger("traceFile");

    public static void main( String[] args ) throws Exception
    {
        // Load the Data into FusibleDataSet
        System.out.println("*\n*\tLoading datasets\n*");
        FusibleDataSet<Sight, Attribute> ds1 = new FusibleHashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/geonames.xml"), "/sights/sight", ds1);
        ds1.printDataSetDensityReport();

        FusibleDataSet<Sight, Attribute> ds2 = new FusibleHashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/opentripmap.xml"), "/sights/sight", ds2);
        ds2.printDataSetDensityReport();

        FusibleDataSet<Sight, Attribute> ds3 = new FusibleHashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/input/wikidata.xml"), "/sights/sight", ds3);
        ds3.printDataSetDensityReport();

        // Maintain Provenance
        // Scores (e.g. from rating)
        ds1.setScore(1.0);
        ds2.setScore(2.0);
        ds3.setScore(3.0);

        // Date (e.g. last update)
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter(Locale.ENGLISH);

        ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
        ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
        ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));

        // load correspondences
        System.out.println("*\n*\tLoading correspondences\n*");
        CorrespondenceSet<Sight, Attribute> correspondences = new CorrespondenceSet<>();
        correspondences.loadCorrespondences(new File("data/correspondences/GoldStandard_Opentripmap_Geonames.csv"),ds1, ds2);
        correspondences.loadCorrespondences(new File("data/correspondences/GoldStandard_Wikidata_Geonames.csv"),ds1, ds3);

        // write group size distribution
        correspondences.printGroupSizeDistribution();

        // load the gold standards
        System.out.println("*\n*\tEvaluating results\n*");
        DataSet<Sight, Attribute> gs = new FusibleHashedDataSet<>();
        new SightXMLReader().loadFromXML(new File("data/goldstandard/gold.xml"), "/sights/sight", gs);

        for(Sight s : gs.get()) {
            System.out.println(String.format("gs: %s", s.getIdentifier()));
        }

        // define the fusion strategy
        DataFusionStrategy<Sight, Attribute> strategy = new DataFusionStrategy<>(new SightXMLReader());
        // write debug results to file
        strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        // add attribute fusers
        // !!! INSERT OUR FUSERS !!!
        strategy.addAttributeFuser(Sight.NAME, new TitleFuserShortestString(),new NameEvaluationRule());
        strategy.addAttributeFuser(Sight.CITY,new ActorsFuserUnion(),new CityEvaluationRule());
        strategy.addAttributeFuser(Sight.COUNTRY,new ActorsFuserUnion(),new CountryEvaluationRule());
        strategy.addAttributeFuser(Sight.LONGITUDE,new ActorsFuserUnion(),new LongitudeEvaluationRule());
        strategy.addAttributeFuser(Sight.LATITUDE,new ActorsFuserUnion(),new LatitudeEvaluationRule());

        // create the fusion engine
        DataFusionEngine<Sight, Attribute> engine = new DataFusionEngine<>(strategy);

        // print consistency report
        engine.printClusterConsistencyReport(correspondences, null);

        // print record groups sorted by consistency
        engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

        // run the fusion
        System.out.println("*\n*\tRunning data fusion\n*");
        FusibleDataSet<Sight, Attribute> fusedDataSet = engine.run(correspondences, null);

        // write the result
        new SightXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

        // evaluate
        DataFusionEvaluator<Sight, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Sight, Attribute>());

        double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

        System.out.println(String.format("Accuracy: %.2f", accuracy));
    }


}