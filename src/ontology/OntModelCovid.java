package ontology;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.apache.jena.vocabulary.XSD;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;


/**
 * This class rappresents the ontology of project.
 * 
 */

/**
 * @author Antonino Durazzo
 *
 */
public class OntModelCovid {
	private final String CovidURI = "http://www.covidapp.org";
	private final String SOURCE = CovidURI + "/ontology";
	private final String NS = SOURCE + "#";
	private final String BaseProperty = NS + "has";
	private final String pathCSV = "./src/csv/dpc-covid19-ita-regioni.csv";
	private String LastDate;
	private OntModel OntCovid = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
	private OntClass Country=createClass("Country");
	private OntClass Region=createClass("Region");
	private OntClass Report=createClass("Report");
	
	public OntModelCovid() {
		System.out.println("START PROCESS TO CREATE ONTOLOGY MODEL");
		System.out.println("START CREATING SCHEMA");
		this.OntCovid.createOntology(SOURCE);
		createPropertyRegion();
		createPropertyCountry();
		createPropertyReport();
		System.out.println("FINISH CREATING SCHEMA");
		System.out.println("START CREATING INDIVIDUALS");
		createResourceCountry("Italy");
		createResourceRegion();
		System.out.println("TRY TO DOWNLOAD UPDATED CSV FROM GITHUB REPOSITORY");
		try (BufferedInputStream in = new BufferedInputStream(new URL("https://raw.githubusercontent.com/pcm-dpc/COVID-19/master/dati-regioni/dpc-covid19-ita-regioni.csv").openStream());
				  FileOutputStream fileOutputStream = new FileOutputStream("./src/csv/dpc-covid19-ita-regioni.csv")) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
		        fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			System.out.println("ATTEMPT WAS SUCCESSFUL, CSV UPDATED");
			System.out.println("READING NEW CSV");
			createResourceReport_region(pathCSV);
			System.out.println("FINISHED READING");
		} catch (IOException e) {
			System.out.println("ATTEMPT FAILED, CSV NO UPDATED");
			System.out.println("READING OLD CSV");
			createResourceReport_region(pathCSV);
			System.out.println("FINISHED READING");
		}
		System.out.println("FINISH CREATING INDIVIDUALS");
		System.out.println("FINISH PROCESS");
	}
	
	/**
	 * This method returns OntModel
	 * @return OntModel
	 */
	public OntModel getOnt() {
		return this.OntCovid;
	}
	
	/**
	 * This method returns NS string
	 * @return String
	 */
	public String getNS() {
		return this.NS;
	}
	
	/**
	 * This method returns source string
	 * @return String
	 */
	public String getSource() {
		return this.SOURCE;
	}
	
	/**
	 * This method returns BaseProperty string
	 * @return String
	 */
	public String getBaseProperty() {
		return this.BaseProperty;
	}
	
	public String getUpdatedDate_ontology() {
		return this.LastDate;
	}
	
	/**
	 * This method creates class in OntModel with
	 * @param name
	 * and
	 * @return OntClas
	 */
	private OntClass createClass(String name) {
		return this.OntCovid.createClass(NS + name);
	}
	
	/**
	 * This method creates class Region in OntCovide ontology with datetype property:
	 * 	Latitude
	 * 	Longitude
	 * 	Name
	 * 	Population
	 * and with object property:
	 * 	hasReport:
	 * 		domain: Class Region
	 * 		range: Class Report
	 * 
	 */
	private void createPropertyRegion() {
		// DATETYPE PROPERTY
		//latitudine 
		DatatypeProperty latitude = this.OntCovid.createDatatypeProperty( BaseProperty + "Latitude" );
		latitude.addDomain( Region );
		latitude.addRange( XSD.xdouble );
		//longitudine
		DatatypeProperty longitude = this.OntCovid.createDatatypeProperty( BaseProperty + "Longitude" );
		longitude.addDomain( Region );
		longitude.addRange( XSD.xdouble );
		//name
		DatatypeProperty Name = this.OntCovid.createDatatypeProperty( BaseProperty + "Name" );
		Name.addDomain( Region );
		Name.addRange( XSD.xstring );
		//population
		DatatypeProperty population = this.OntCovid.createDatatypeProperty( BaseProperty + "Population" );
		population.addDomain( Region );
		population.addRange( XSD.unsignedInt );
		//codice regione
		
		DatatypeProperty CodRegion = this.OntCovid.createDatatypeProperty( BaseProperty + "Id", true );
		CodRegion.addDomain( Region );
		CodRegion.addRange( XSD.unsignedInt );
		
		// --------------
		// CLASS PROPERTY
		ObjectProperty isPartOf = this.OntCovid.createObjectProperty( NS + "isPartOf");
		isPartOf.addDomain( Region );
		isPartOf.addRange( Country);
	}
	
	/**
	 * This method creates class Country in OntCovide ontology with datetype property:
	 * 	Name
	 * 	Population
	 * and with object property:
	 * 	hasReport:
	 * 		domain: Class Country
	 * 		range: Class Report 
	 * 	hasRegion:
	 * 		domain: Class Country
	 * 		range: Class Region 
	 */
	private void createPropertyCountry() {
		//name
		DatatypeProperty Name = this.OntCovid.createDatatypeProperty( BaseProperty + "Name" );
		Name.addDomain( Country );
		Name.addRange( XSD.xstring );
		//population
		DatatypeProperty population = this.OntCovid.createDatatypeProperty( BaseProperty + "Population" );
		population.addDomain( Country );
		population.addRange( XSD.unsignedInt );
	}
	
	/**
	 * This method creates class Report in OntCovide ontology with datetype property:
	 * 	Date
	 * 	Deceased
	 *  DischargedHealed
	 *  HomeIsolation
	 *  IntensiveCare
	 *  RecoveredWithSymptoms
	 *  Swabs
	 *  TestedCases
	 *  TotalCases
	 *  TotalHospitalized
	 *  TotalPositves
	 */
	private void createPropertyReport() {
		//date 
		DatatypeProperty date = this.OntCovid.createDatatypeProperty( BaseProperty + "Date" );
		date.addDomain( Report );
		date.addRange( XSD.date );
		//Deceased
		DatatypeProperty deceased = this.OntCovid.createDatatypeProperty( BaseProperty + "Deceased" );
		deceased.addDomain( Report );
		deceased.addRange( XSD.unsignedInt );
		//DischargedHealed
		DatatypeProperty dischargedHealed = this.OntCovid.createDatatypeProperty( BaseProperty + "DischargedHealed" );
		dischargedHealed.addDomain( Report );
		dischargedHealed.addRange(XSD.unsignedInt );
		//HomeIsolation
		DatatypeProperty HomeIsolation = this.OntCovid.createDatatypeProperty( BaseProperty + "HomeIsolation" );
		HomeIsolation.addDomain( Report );
		HomeIsolation.addRange( XSD.unsignedInt );
		//IntensiveCare
		DatatypeProperty IntensiveCare = this.OntCovid.createDatatypeProperty( BaseProperty + "IntensiveCare" );
		IntensiveCare.addDomain( Report );
		IntensiveCare.addRange( XSD.unsignedInt );
		//RecoveredWithSymptoms
		DatatypeProperty RecoveredWithSymptoms = this.OntCovid.createDatatypeProperty( BaseProperty + "RecoveredWithSymptoms" );
		RecoveredWithSymptoms.addDomain( Report );
		RecoveredWithSymptoms.addRange( XSD.unsignedInt );
		//Swabs
		DatatypeProperty Swabs = this.OntCovid.createDatatypeProperty( BaseProperty + "Swabs" );
		Swabs.addDomain( Report );
		Swabs.addRange(XSD.unsignedInt );
		//TestedCases
		DatatypeProperty TestedCases = this.OntCovid.createDatatypeProperty( BaseProperty + "TestedCases" );
		TestedCases.addDomain( Report );
		TestedCases.addRange( XSD.unsignedInt );
		//TotalCases
		DatatypeProperty TotalCases = this.OntCovid.createDatatypeProperty( BaseProperty + "TotalCases" );
		TotalCases.addDomain( Report );
		TotalCases.addRange( XSD.unsignedInt );
		//TotalHospitalized
		DatatypeProperty TotalHospitalized = this.OntCovid.createDatatypeProperty( BaseProperty + "TotalHospitalized" );
		TotalHospitalized.addDomain( Report );
		TotalHospitalized.addRange( XSD.unsignedInt );
		//TotalPositves
		DatatypeProperty TotalPositves = this.OntCovid.createDatatypeProperty( BaseProperty + "TotalPositves" );
		TotalPositves.addDomain( Report );
		TotalPositves.addRange( XSD.unsignedInt);
		
		// --------------
		// CLASS PROPERTY
		ObjectProperty belongsTo = this.OntCovid.createObjectProperty(NS + "belongsTo", true);
		belongsTo.addDomain( Report );
		belongsTo.addRange(Country);
		belongsTo.addRange(Region);
	}
	
	
	/**
	 * 
	 * @param csvFile
	 */
	private void createResourceReport_region(String csvFile) {
		parseCSV parse = new parseCSV();
		try {
			HashMap<Integer,HashMap<String,String>> regionMap = parse.getMapCSV_region(csvFile);
			String tmpdate = regionMap.get(1).get("Date").substring(0, 10);
			String dateIT=tmpdate;
			int deceasedIT=0;
			int dischargedIT=0;
			int hisolationIT=0;
			int intensivecareIT=0;
			int recoveredIT=0;
			int swabsIT=0;
			int totcaseIT=0;
			int totHospitIT=0;
			int topositiveIT=0;
			int testedcaseIT=0;
			int i=0;
			for(Integer key : regionMap.keySet()) {
				Individual src = this.OntCovid.createIndividual( NS + regionMap.get(key).get("Date").substring(0, 10) + "-"+regionMap.get(key).get("Region"), Report);
				
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "Date"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("Date").substring(0, 10), XSDDatatype.XSDdate));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "Deceased"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("Deceased"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "DischargedHealed"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("DischargedHealed"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "HomeIsolation"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("HomeIsolation"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "IntensiveCare"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("IntensiveCare"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "RecoveredWithSymptoms"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("RecoveredWithSymptoms"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "Swabs"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("Swabs"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TotalCases"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("TotalCases"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TotalHospitalized"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("TotalHospitalized"), XSDDatatype.XSDunsignedInt));
				this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TotalPositves"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("TotalPositves"), XSDDatatype.XSDunsignedInt));
				
				if (regionMap.get(key).get("TestedCases").isEmpty()) {
					this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TestedCases"), ResourceFactory.createTypedLiteral( "0", XSDDatatype.XSDunsignedInt));
				} else {
					this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TestedCases"), ResourceFactory.createTypedLiteral( regionMap.get(key).get("TestedCases"), XSDDatatype.XSDunsignedInt));
				}
				
				Individual range = this.OntCovid.getIndividual(NS + regionMap.get(key).get("Region"));
				ObjectProperty belongsTo = this.OntCovid.getObjectProperty(NS + "belongsTo" );
				this.OntCovid.add(src,belongsTo,range);
				
				if(tmpdate.equals(regionMap.get(key).get("Date").substring(0, 10))) {
					deceasedIT= deceasedIT +Integer.parseInt(regionMap.get(key).get("Deceased"));
					dischargedIT=dischargedIT+Integer.parseInt(regionMap.get(key).get("DischargedHealed"));
					hisolationIT=hisolationIT+Integer.parseInt(regionMap.get(key).get("HomeIsolation"));
					intensivecareIT=intensivecareIT+Integer.parseInt(regionMap.get(key).get("IntensiveCare"));
					recoveredIT=recoveredIT+Integer.parseInt(regionMap.get(key).get("RecoveredWithSymptoms"));
					swabsIT=swabsIT+Integer.parseInt(regionMap.get(key).get("Swabs"));
					totcaseIT=totcaseIT+Integer.parseInt(regionMap.get(key).get("TotalCases"));
					totHospitIT=totHospitIT+Integer.parseInt(regionMap.get(key).get("TotalHospitalized"));
					topositiveIT=topositiveIT+Integer.parseInt(regionMap.get(key).get("TotalPositves"));
					if (!regionMap.get(key).get("TestedCases").isEmpty()) {
						testedcaseIT=testedcaseIT+Integer.parseInt(regionMap.get(key).get("TestedCases"));
					}
				}else {
					Individual srcIT = this.OntCovid.createIndividual( NS + dateIT + "-"+"Italy", Report);
					
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "Date"), ResourceFactory.createTypedLiteral( dateIT, XSDDatatype.XSDdate));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "Deceased"), ResourceFactory.createTypedLiteral( Integer.toString(deceasedIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "DischargedHealed"), ResourceFactory.createTypedLiteral( Integer.toString(dischargedIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "HomeIsolation"), ResourceFactory.createTypedLiteral( Integer.toString(hisolationIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "IntensiveCare"), ResourceFactory.createTypedLiteral( Integer.toString(intensivecareIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "RecoveredWithSymptoms"), ResourceFactory.createTypedLiteral( Integer.toString(recoveredIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "Swabs"), ResourceFactory.createTypedLiteral( Integer.toString(swabsIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "TotalCases"), ResourceFactory.createTypedLiteral( Integer.toString(totcaseIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "TotalHospitalized"), ResourceFactory.createTypedLiteral( Integer.toString(totHospitIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "TotalPositves"), ResourceFactory.createTypedLiteral( Integer.toString(topositiveIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TestedCases"), ResourceFactory.createTypedLiteral( Integer.toString(testedcaseIT), XSDDatatype.XSDunsignedInt));
					
					Individual rangeIT = this.OntCovid.getIndividual(NS + "Italy");
					ObjectProperty belongsToIT = this.OntCovid.getObjectProperty(NS + "belongsTo" );
					this.OntCovid.add(srcIT,belongsToIT,rangeIT);
					
					deceasedIT=0;
					dischargedIT=0;
					hisolationIT=0;
					intensivecareIT=0;
					recoveredIT=0;
					swabsIT=0;
					totcaseIT=0;
					totHospitIT=0;
					topositiveIT=0;
					testedcaseIT=0;
					tmpdate = regionMap.get(key).get("Date").substring(0, 10);
					dateIT = tmpdate;
					
				}
				
				if (i==regionMap.size()-1) {
					Individual srcIT = this.OntCovid.createIndividual( NS + dateIT + "-"+"Italy", Report);
					
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "Date"), ResourceFactory.createTypedLiteral( dateIT, XSDDatatype.XSDdate));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "Deceased"), ResourceFactory.createTypedLiteral( Integer.toString(deceasedIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "DischargedHealed"), ResourceFactory.createTypedLiteral( Integer.toString(dischargedIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "HomeIsolation"), ResourceFactory.createTypedLiteral( Integer.toString(hisolationIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "IntensiveCare"), ResourceFactory.createTypedLiteral( Integer.toString(intensivecareIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "RecoveredWithSymptoms"), ResourceFactory.createTypedLiteral( Integer.toString(recoveredIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "Swabs"), ResourceFactory.createTypedLiteral( Integer.toString(swabsIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "TotalCases"), ResourceFactory.createTypedLiteral( Integer.toString(totcaseIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "TotalHospitalized"), ResourceFactory.createTypedLiteral( Integer.toString(totHospitIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(srcIT, this.OntCovid.getProperty(BaseProperty + "TotalPositves"), ResourceFactory.createTypedLiteral( Integer.toString(topositiveIT), XSDDatatype.XSDunsignedInt));
					this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "TestedCases"), ResourceFactory.createTypedLiteral( Integer.toString(testedcaseIT), XSDDatatype.XSDunsignedInt));
					
					Individual rangeIT = this.OntCovid.getIndividual(NS + "Italy");
					ObjectProperty belongsToIT = this.OntCovid.getObjectProperty(NS + "belongsTo" );
					this.OntCovid.add(srcIT,belongsToIT,rangeIT);
					this.LastDate = dateIT;
				}
				i +=1;
				
		    }
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * This method creates a Map in the following way:
	 * 	Key set:
	 * 		The set of Region name
	 * 	Value set:
	 * 		The set of object dataRegion in which there are the Code Region, Population, Latitude and Longitude associeted with Region key
	 * @return HashMap<String,dataRegion>
	 */
	private HashMap<String,dataRegion> createRegionMap() {
		HashMap<String,dataRegion> regionMap = new HashMap<String,dataRegion>();
		
        dataRegion region13 = new dataRegion(13, 1311580, 42.35122196, 13.39843823);
		regionMap.put("Abruzzo", region13);
		
		dataRegion region17 = new dataRegion(17, 562869, 40.63947052, 15.80514834);
		regionMap.put("Basilicata", region17);
		
		dataRegion region18 = new dataRegion(18, 1947131, 38.90597598, 16.59440194);
		regionMap.put("Calabria", region18);
		
		dataRegion region15 = new dataRegion(15, 5801692, 40.83956555, 14.25084984 );
		regionMap.put("Campania", region15);
		
		dataRegion region8 = new dataRegion(8, 4459477 , 44.49436681, 11.3417208);
		regionMap.put("Emilia-Romagna", region8);
		
		dataRegion region6 = new dataRegion(6, 1215220, 45.6494354, 13.76813649 );
		regionMap.put("Friuli Venezia Giulia", region6);
		
		dataRegion region12 = new dataRegion(12, 5879082, 41.89277044, 12.48366722 );
		regionMap.put("Lazio", region12);
		
		dataRegion region7 = new dataRegion(7, 1550640, 44.41149315, 8.9326992 );
		regionMap.put("Liguria", region7);
		
		dataRegion region3 = new dataRegion(3, 10060574, 45.46679409, 9.190347404 );
		regionMap.put("Lombardia", region3);
		
		dataRegion region11 = new dataRegion(11, 1525271, 43.61675973, 13.5188753 );
		regionMap.put("Marche", region11);
		
		dataRegion region14 = new dataRegion(14, 305617, 41.55774754, 14.65916051 );
		regionMap.put("Molise", region14);
		
		dataRegion region1 = new dataRegion(11, 4356406, 45.0732745, 7.680687483);
		regionMap.put("Piemonte", region1);
		
		dataRegion region16 = new dataRegion(16, 4029053, 41.12559576,16.86736689 );
		regionMap.put("Puglia", region16);
		
		dataRegion region20 = new dataRegion(20, 1639591, 39.21531192, 9.110616306 );
		regionMap.put("Sardegna", region20);
		
		dataRegion region19 = new dataRegion(19, 4999891, 38.11569725, 13.3623567 );
		regionMap.put("Sicilia", region19);
		
		dataRegion region9 = new dataRegion(9, 3729641, 43.76923077, 11.25588885 );
		regionMap.put("Toscana", region9);
		
		dataRegion region10 = new dataRegion(10, 882015, 43.10675841, 12.38824698 );
		regionMap.put("Umbria", region10);
		
		dataRegion region2 = new dataRegion(2, 125666, 45.73750286, 7.320149366 );
		regionMap.put("Valle d'Aosta", region2);
		
		dataRegion region5 = new dataRegion(5, 4905854, 45.43490485, 12.33845213);
		regionMap.put("Veneto", region5);
		
		dataRegion region4 = new dataRegion(4, 541380, 46.06893511, 11.12123097 );
		regionMap.put("P.A. Trento", region4);
		
		dataRegion region42 = new dataRegion(4, 533050, 46.49933453, 11.35662422);
		regionMap.put("P.A. Bolzano", region42);
		
		return regionMap;
	}
	
	/**
	 *  For each region of italy, this method creates a resource as OntClass Region
	 */
	private void createResourceRegion() {
		HashMap<String,dataRegion> regionMap = createRegionMap();
		for(String key : regionMap.keySet()) {
			Individual src = this.OntCovid.createIndividual( NS + key, Region);
			this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "Name"), ResourceFactory.createTypedLiteral( key, XSDDatatype.XSDstring));
			this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "Id"), ResourceFactory.createTypedLiteral( Integer.toString(regionMap.get(key).getCode()), XSDDatatype.XSDunsignedInt));
			this.OntCovid.add(src, this.OntCovid.getProperty(BaseProperty + "Population"), ResourceFactory.createTypedLiteral( Integer.toString(regionMap.get(key).getPopulation()), XSDDatatype.XSDunsignedInt));
			src.addLiteral(this.OntCovid.getProperty( BaseProperty + "Latitude"  ), regionMap.get(key).getLatitude());
			src.addLiteral(this.OntCovid.getProperty( BaseProperty + "Longitude"  ), regionMap.get(key).getLongitude());
			
			Individual range = this.OntCovid.getIndividual(NS + "Italy");
			ObjectProperty isPartOf = this.OntCovid.getObjectProperty( NS + "isPartOf" );
			this.OntCovid.add(src,isPartOf,range);
	    }
	}
	
	/**
	 * This method creates a resource as OntClass Country based on  
	 * @param str
	 */
	private void createResourceCountry(String str) {
		Individual italy = this.OntCovid.createIndividual( NS + str, Country);
		this.OntCovid.add(italy, this.OntCovid.getProperty(BaseProperty + "Name"), ResourceFactory.createTypedLiteral( str, XSDDatatype.XSDstring));
		this.OntCovid.add(italy, this.OntCovid.getProperty(BaseProperty + "Population"), ResourceFactory.createTypedLiteral( Integer.toString(60483973), XSDDatatype.XSDunsignedInt));
	}
	
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void writeOntologyOnFileSystem(String path, String type) throws IOException {
		FileWriter out = null;
		try {
		  out = new FileWriter(path);
		  this.OntCovid.write( out, type );
		}
		finally {
		  if (out != null) {
		    try {out.close();} catch (IOException ignore) {}
		  }
		}
	}
	
}
