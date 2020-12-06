----------------------------------------------------------------------------------
This readme describes the folder structure and the files contained in the submission
file for the WDI-Project "Integrated Sightseeing" by Group 7.
----------------------------------------------------------------------------------
1. SchemaMatchingSights: contains the files/data created for Schema Matching

	a. ../Target_Schema: contains the .xsd-target schema as well as an 
			     .xml-example
	b. ../MapForceMatching_Files: contains all .mfd files for translating the
				      source data sources into the target schema 
	b. ../XML_Output_Files: contains our data sources in .xml-format as 
			        resulting from the schema matching step
--------
2. IdentityResolutionSights: Java Maven project which contains the files/data 
			     created for Identity Resolution

	a. ../data/input: contains the deduplicated data sources in .xml-format 
			  created from the XML_Output_Files from SchemaMatching 
			  by applying deduplication measures
	b. ../data/goldstandard: contains a train- & a test-split for each of our
				 three dataset-combinations
	c. ../data/output: contains the .csv-files that result from the identity 
			   resolution (i.e. the correspondences, debugResultsBlocking 
			   and debugResultsMatching)
--------
3. DataFusionSights: Java Maven project which contains the files/data 
	             created for Data Fusion

	a. ../data/correspondences: contains the correspondences .csv-files as generated in Identity resolution
	b. ../data/goldstandard: contains our gold standard (?)
	c. ../data/input: contains the same files as 2.a.
	d. ../data/output: contains the .csv-files that result from the data fusion
--------
4. FinalReport: contains the final report in .pdf-format
