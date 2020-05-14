package covidGui;


import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;

public class BarChart extends JFrame {

  private static final long serialVersionUID = 1L;
  /**
   * 
   * @param appTitle
   * @param m
   * @param r
   */
  public BarChart(String appTitle,OntModel m,Resource r) {
    super(appTitle);

    // Create Dataset
    CategoryDataset dataset = createDataset(m,r);
    
    //Create chart
    JFreeChart chart=ChartFactory.createBarChart(
        appTitle, //Chart Title
        "Data", // Category axis
        "Number of people", // Value axis
        dataset,
        PlotOrientation.HORIZONTAL,
        true,true,false
       );

    ChartPanel panel=new ChartPanel(chart);
    setContentPane(panel);
  }
  /**
   * 
   * @param appTitle
   * @param m
   * @param queryString
   * @param dataToPlot
   */
  public BarChart(String appTitle,OntModel m,String queryString,String dataToPlot) {
	    super(appTitle);

	    // Create Dataset
	    CategoryDataset dataset = createDataset(m,queryString,dataToPlot);
	    
	    //Create chart
	    JFreeChart chart=ChartFactory.createBarChart(
	        appTitle, //Chart Title
	        "State", // Category axis
	        dataToPlot, // Value axis
	        dataset,
	        PlotOrientation.HORIZONTAL,
	        true,true,false
	       );

	    ChartPanel panel=new ChartPanel(chart);
	    setContentPane(panel);
	  }
  /**
   * 
   * @param OntCovid
   * @param queryString
   * @param dataToPlot
   * @return
   */
  private CategoryDataset createDataset(OntModel OntCovid,String queryString,String dataToPlot) {
	  
	  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	  Query query = QueryFactory.create(queryString);
	  try (QueryExecution qexec = QueryExecutionFactory.create(query, OntCovid)) {
			ResultSet results = qexec.execSelect() ;
			for (; results.hasNext();) {
				
				QuerySolution soln = results.nextSolution();
				Resource r = soln.getResource("Report");
				if (dataToPlot.equals("PercentOfCases")) { 
					dataset.addValue((Float.parseFloat(r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasTotalCases")).getLiteral().getValue().toString())
							/Float.parseFloat(r.getProperty(OntCovid.getObjectProperty("http://www.covidapp.org/ontology#belongsTo")).getResource().getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasPopulation")).getLiteral().getValue().toString()))*100, 
							r.getProperty(OntCovid.getObjectProperty("http://www.covidapp.org/ontology#belongsTo")).getResource().getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasName")).getLiteral().getValue().toString(),
							r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasDate")).getLiteral().getValue().toString());
				}else {
					dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#has"+dataToPlot)).getLiteral().getValue().toString()), 
							r.getProperty(OntCovid.getObjectProperty("http://www.covidapp.org/ontology#belongsTo")).getResource()
								.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasName")).getLiteral().getValue().toString(),
							r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasDate")).getLiteral().getValue().toString());
				}
			}
		}
	    return dataset;
	  }
  /**
   * 
   * @param OntCovid
   * @param r
   * @return
   */
  private CategoryDataset createDataset(OntModel OntCovid, Resource r) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    String BaseProperty = "http://www.covidapp.org/ontology#has";
    String date = r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"Date")).getLiteral().getValue().toString();
    //Integer Deceased=Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"Deceased")).getLiteral().getValue().toString());
    
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"Swabs")).getLiteral().getValue().toString()), "Swabs", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"TotalCases")).getLiteral().getValue().toString()), "TotalCases", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"TotalPositves")).getLiteral().getValue().toString()), "TotalPositves", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"DischargedHealed")).getLiteral().getValue().toString()), "DischargedHealed", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"Deceased")).getLiteral().getValue().toString()), "Deceased", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"TotalCases")).getLiteral().getValue().toString()), "TotalCases", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"TotalHospitalized")).getLiteral().getValue().toString()), "TotalHospitalized", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"RecoveredWithSymptoms")).getLiteral().getValue().toString()), "RecoveredWithSymptoms", date);
    dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"IntensiveCare")).getLiteral().getValue().toString()), "IntensiveCare", date);
    //dataset.addValue(Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty(BaseProperty+"TestedCases")).getLiteral().getValue().toString()), "TestedCases", date);
    
    return dataset;
  }
}
