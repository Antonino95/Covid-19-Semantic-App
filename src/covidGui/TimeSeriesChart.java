package covidGui;
import java.awt.Color;

import javax.swing.JFrame;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * @author imssbora
 *
 */
public class TimeSeriesChart extends JFrame {

	private static final long serialVersionUID = 1L;
	private XYDataset dataset;
	private TimeSeriesCollection dataset_tmp = new TimeSeriesCollection();
	private String dataToPlot;
	/**
	 * 
	 * @param title
	 * @param m
	 * @param query
	 * @param region
	 * @param data
	 */
	public TimeSeriesChart(String title,OntModel m,String query,String region, String data) {
		super(title);
		String str = data;
		// Create dataset
		if (data.substring(0, 3).equals("Var")) {
			this.dataToPlot = data.substring(4, data.length());
			createDataset(m,query,region,1);
			str = "Variation of " + this.dataToPlot;
	    }else {
	    	this.dataToPlot = data;
	    	createDataset(m,query,region,0);
	    }
		
		this.dataset = this.dataset_tmp;
		// Create chart
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Chart "+ region + " for " + str, // Chart
				"Date", // X-Axis Label
				"Number", // Y-Axis Label
				dataset);

		//Changes background color
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255,228,196));
    
		ChartPanel panel = new ChartPanel(chart);
    	setContentPane(panel);
	}
	
	/**
	 * 
	 * @param title
	 * @param m
	 * @param query1
	 * @param query2
	 * @param region1
	 * @param region2
	 * @param data
	 */
	public TimeSeriesChart(String title,OntModel m,String query1,String query2,String region1,String region2,String data) {
	    super(title);
	    // Create dataset
	    String str = data;
	    if (data.substring(0, 3).equals("Var")) {
	    	this.dataToPlot = data.substring(4, data.length());
		    createDataset(m,query1,query2,region1,region2,1);
		    str = "Variation of " + this.dataToPlot;
	    }else {
	    	this.dataToPlot = data;
		    createDataset(m,query1,query2,region1,region2,0);
	    }
	    this.dataset = this.dataset_tmp;
	    // Create chart
	    JFreeChart chart = ChartFactory.createTimeSeriesChart(
	        "Charts "+ region1 + " VS "+ region2 + " for "+ str, // Chart
	        "Date", // X-Axis Label
	        "Number", // Y-Axis Label
	        dataset);

	    //Changes background color
	    XYPlot plot = (XYPlot)chart.getPlot();
	    plot.setBackgroundPaint(new Color(255,228,196));
	    
	    ChartPanel panel = new ChartPanel(chart);
	    
	    setContentPane(panel);
	}
  
  /**
   * 
   * @param OntCovid
   * @param queryString
   * @param region
   */
	private void createDataset(OntModel OntCovid, String queryString, String region, int FLAG) {
		TimeSeries series = new TimeSeries("Series "+ region);
		Query query = QueryFactory.create(queryString) ;
		try (QueryExecution qexec = QueryExecutionFactory.create(query, OntCovid)) {
			ResultSet res = qexec.execSelect();
			Integer n=0;
			for (; res.hasNext();) {
				QuerySolution soln = res.nextSolution();
				Resource r = soln.getResource("Report");
				String date =r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#hasDate")).getLiteral().getValue().toString();
				if (FLAG == 0) {
					series.add(new Day(Integer.parseInt(date.substring(8, 10)), 
							Integer.parseInt(date.substring(5, 7)), 
							Integer.parseInt(date.substring(0, 4))), 
							Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#has"+dataToPlot)).getLiteral().getValue().toString()));
					
				} else {
					
					series.add(new Day(Integer.parseInt(date.substring(8, 10)), 
							Integer.parseInt(date.substring(5, 7)), 
							Integer.parseInt(date.substring(0, 4))), 
							Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#has"+dataToPlot)).getLiteral().getValue().toString())-n);
					n=Integer.parseInt(r.getProperty(OntCovid.getDatatypeProperty("http://www.covidapp.org/ontology#has"+dataToPlot)).getLiteral().getValue().toString());
				}
				
			}
			this.dataset_tmp.addSeries(series);  
	  }  
    }
  
	/**
	 * 
	 * @param OntCovid
	 * @param queryString1
	 * @param queryString2
	 * @param region1
	 * @param region2
	 */
	private void createDataset(OntModel OntCovid, String queryString1,String queryString2,String region1,String region2, int FLAG) {
		createDataset(OntCovid,queryString1,region1,FLAG);
		createDataset(OntCovid,queryString2,region2,FLAG);
    }
  
}
