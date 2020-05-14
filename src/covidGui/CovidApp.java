package covidGui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;


import java.awt.Color;
import java.awt.Button;

import javax.swing.JLabel;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;

import com.toedter.calendar.JCalendar;

import ontology.OntModelCovid;

public class CovidApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static OntModelCovid OntModelCovid19;
	private static OntModel OntCovid;
	private int FLAG_EVENT=0;
	private static CovidApp frame;
	int xx,xy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CovidApp frame = new CovidApp();
					frame.setUndecorated(true);
					frame.setVisible(true);
					frame.setIconImage(ImageIO.read(new File("./src/images/Logo CovidApp.png")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public CovidApp() throws IOException {
		OntModelCovid19 = new OntModelCovid();
		OntCovid = OntModelCovid19.getOnt();
		OntModelCovid19.writeOntologyOnFileSystem("./src/owl/ontologycovid.owl", "RDF/XML");
		
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 729, 476);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 346, 490);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Covid-19 App");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setForeground(new Color(240, 248, 255));
		lblNewLabel.setBounds(106, 305, 144, 27);
		panel.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				 xx = e.getX();
			     xy = e.getY();
			}
		});
		label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				
				int x = arg0.getXOnScreen();
	            int y = arg0.getYOnScreen();
	            CovidApp.this.setLocation(x - xx, y - xy);  
			}
		});
		label.setBounds(-44, 0, 420, 275);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setIcon(new ImageIcon("./src/images/coronavirus.jpg"));
		panel.add(label);
		
		JLabel lblWeGotYou = new JLabel("....Antonino Durazzo....");
		lblWeGotYou.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeGotYou.setForeground(new Color(240, 248, 255));
		lblWeGotYou.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblWeGotYou.setBounds(111, 343, 141, 27);
		panel.add(lblWeGotYou);
		
		JLabel lblfrancescoDePertis = new JLabel("....Francesco de Pertis....");
		lblfrancescoDePertis.setHorizontalAlignment(SwingConstants.CENTER);
		lblfrancescoDePertis.setForeground(new Color(240, 248, 255));
		lblfrancescoDePertis.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblfrancescoDePertis.setBounds(111, 377, 141, 27);
		panel.add(lblfrancescoDePertis);
		
		JLabel lbl_home_region = new JLabel("SELECT AN AREA");
		lbl_home_region.setBounds(395, 66, 114, 14);
		contentPane.add(lbl_home_region);
		
		JLabel lbl_home_date = new JLabel("SELECT A DATE");
		lbl_home_date.setBounds(395, 142, 143, 14);
		contentPane.add(lbl_home_date);
		
		
		JLabel lbl_trend_trends = new JLabel("SELECT A TREND");
		lbl_trend_trends.setBounds(395, 222, 96, 14);
		lbl_trend_trends.setVisible(false);
		contentPane.add(lbl_trend_trends);
		
		JLabel lbl_close = new JLabel("X");
		lbl_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				System.exit(0);
			}
		});
		lbl_close.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_close.setForeground(new Color(241, 57, 83));
		lbl_close.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_close.setBounds(691, 0, 37, 27);
		contentPane.add(lbl_close);
		
		JComboBox combo_region_1 = new JComboBox();
		combo_region_1.setModel(new DefaultComboBoxModel(new String[] {"Abruzzo", "Basilicata", "Calabria", "Campania", "Emilia-Romagna", "Friuli Venezia Giulia", "Lazio", "Liguria", "Lombardia", "Marche", "Molise", "Piemonte", "Puglia", "Sardegna", "Sicilia", "Toscana", "Umbria", "Valle d'Aosta", "Veneto", "P.A. Trento", "P.A. Bolzano", "Italy"}));
		combo_region_1.setBackground(new Color(255, 255, 255));
		combo_region_1.setForeground(Color.BLACK);
		combo_region_1.setBounds(395, 83, 283, 44);
		contentPane.add(combo_region_1);
		
		JCalendar calendar_combo = new JCalendar();
		calendar_combo.setBounds(395, 162, 283, 179);
		calendar_combo.setBackground(new Color(255, 255, 255));
		contentPane.add(calendar_combo);
		
		JComboBox combo_region_2 = new JComboBox();
		combo_region_2.setModel(new DefaultComboBoxModel(new String[] {"", "Abruzzo", "Basilicata", "Calabria", "Campania", "Emilia-Romagna", "Friuli Venezia Giulia", "Lazio", "Liguria", "Lombardia", "Marche", "Molise", "Piemonte", "Puglia", "Sardegna", "Sicilia", "Toscana", "Umbria", "Valle d'Aosta", "Veneto", "P.A. Trento", "P.A. Bolzano", "Italy"}));
		combo_region_2.setForeground(Color.BLACK);
		combo_region_2.setBackground(Color.WHITE);
		combo_region_2.setBounds(395, 162, 283, 44);
		combo_region_2.setVisible(false);
		contentPane.add(combo_region_2);
		
		JComboBox combo_class = new JComboBox();
		combo_class.setModel(new DefaultComboBoxModel(new String[] {"Deceased", "DischargedHealed", "HomeIsolation", "IntensiveCare", "RecoveredWithSymptoms", "Swabs", "TestedCases", "TotalCases", "TotalHospitalized", "TotalPositves", "PercentOfCases"}));
		combo_class.setForeground(Color.BLACK);
		combo_class.setBackground(Color.WHITE);
		combo_class.setBounds(395, 162, 283, 44);
		combo_class.setVisible(false);
		contentPane.add(combo_class);
		
		JComboBox combo_trend = new JComboBox();
		combo_trend.setModel(new DefaultComboBoxModel(new String[] {"Deceased", "DischargedHealed", "HomeIsolation", "IntensiveCare", "RecoveredWithSymptoms", "Swabs", "TestedCases", "TotalCases", "TotalHospitalized", "TotalPositves", "Var Deceased", "Var DischargedHealed", "Var HomeIsolation", "Var IntensiveCare", "Var RecoveredWithSymptoms", "Var Swabs", "Var TestedCases", "Var TotalCases", "Var TotalHospitalized", "Var TotalPositves"}));
		combo_trend.setForeground(Color.BLACK);
		combo_trend.setBackground(Color.WHITE);
		combo_trend.setBounds(395, 243, 283, 44);
		combo_trend.setVisible(false);
		contentPane.add(combo_trend);
		
		//trend
		Button search_trend = new Button("Search");
		search_trend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					if (combo_region_2.getSelectedItem().toString().isEmpty() || combo_region_1.getSelectedItem().toString().equals(combo_region_2.getSelectedItem().toString()) ) {
						queryOnt_TREND(combo_region_1.getSelectedItem().toString(),combo_trend.getSelectedItem().toString());
					}else {
						queryOnt_TREND(combo_region_1.getSelectedItem().toString(),combo_region_2.getSelectedItem().toString(),combo_trend.getSelectedItem().toString());
					}
					
				} catch (InvocationTargetException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		search_trend.setForeground(Color.WHITE);
		search_trend.setBackground(new Color(241, 57, 83));
		search_trend.setBounds(395, 316, 283, 36);
		search_trend.setVisible(false);
		contentPane.add(search_trend);
		
		//home
		Button search_home = new Button("Search");
		search_home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					queryOnt_DATA(combo_region_1.getSelectedItem().toString(), calendar_combo.getCalendar().get(1),calendar_combo.getCalendar().get(2)+1,calendar_combo.getCalendar().get(5));
				} catch (InvocationTargetException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		search_home.setForeground(Color.WHITE);
		search_home.setBackground(new Color(241, 57, 83));
		search_home.setBounds(395, 358, 283, 36);
		contentPane.add(search_home);
		
		//category
		Button search_class = new Button("Search");
		search_class.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					queryOnt_class(combo_class.getSelectedItem().toString());
				} catch (InvocationTargetException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		search_class.setForeground(Color.WHITE);
		search_class.setBackground(new Color(241, 57, 83));
		search_class.setBounds(395, 242, 283, 36);
		search_class.setVisible(false);
		contentPane.add(search_class);
		
		JLabel lbl_close_next = new JLabel("      Trends");
		JLabel lbl_close_previous = new JLabel("Home");
		lbl_close_next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (FLAG_EVENT == 0) {
					lbl_close_next.setText("Comparison");
					lbl_home_date.setText("SELECT AN AREA");
					search_home.setVisible(false);
					calendar_combo.setVisible(false);
					lbl_close_previous.setVisible(true);
					search_trend.setVisible(true);
					combo_trend.setVisible(true);
					combo_region_2.setVisible(true);
					lbl_trend_trends.setVisible(true);
				}else if (FLAG_EVENT == 1) {
					lbl_close_previous.setText(" Trends");
					lbl_home_date.setText("SELECT A CATEGORY");
					lbl_close_next.setVisible(false);
					lbl_home_region.setVisible(false);
					lbl_trend_trends.setVisible(false);
					combo_region_1.setVisible(false);
					combo_region_2.setVisible(false);
					combo_trend.setVisible(false);
					search_trend.setVisible(false);
					search_class.setVisible(true);
					combo_class.setVisible(true);
				}
				FLAG_EVENT += 1;
				
			}
		});
		lbl_close_next.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_close_next.setForeground(new Color(241, 57, 83));
		lbl_close_next.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_close_next.setBounds(582, 26, 97, 27);
		contentPane.add(lbl_close_next);
		
		
		lbl_close_previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (FLAG_EVENT == 1) {
					lbl_close_next.setText("       Trends");
					lbl_home_date.setText("SELECT A DATE");
					search_trend.setVisible(false);
					combo_trend.setVisible(false);
					lbl_trend_trends.setVisible(false);
					lbl_close_previous.setVisible(false);
					combo_region_2.setVisible(false);
					search_home.setVisible(true);
					calendar_combo.setVisible(true);
					
				}else if (FLAG_EVENT == 2) {
					lbl_close_previous.setText("Home");
					lbl_home_date.setText("SELECT AN AREA");
					search_class.setVisible(false);
					combo_class.setVisible(false);
					lbl_close_next.setVisible(true);
					lbl_home_region.setVisible(true);
					lbl_trend_trends.setVisible(true);
					combo_region_1.setVisible(true);
					combo_region_2.setVisible(true);
					combo_trend.setVisible(true);
					search_trend.setVisible(true);
				}
				FLAG_EVENT -= 1;
				
			}
		});
		lbl_close_previous.setVisible(false);
		lbl_close_previous.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_close_previous.setForeground(new Color(241, 57, 83));
		lbl_close_previous.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_close_previous.setBounds(384, 26, 66, 27);
		lbl_close_previous.setVisible(false);
		contentPane.add(lbl_close_previous);
		
		
		JLabel lbl_update_csv = new JLabel("Ontology updated to " + OntModelCovid19.getUpdatedDate_ontology());
		lbl_update_csv.setBounds(547, 462, 186, 14);
		contentPane.add(lbl_update_csv);
		
		
	}
	

	/**
	 * This methods queries the ontology to visualize the current situation of each areas for indicated data.
	 * @param dataToPlot
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private static void queryOnt_class(String dataToPlot) throws InvocationTargetException, InterruptedException {
		String dateString = OntModelCovid19.getUpdatedDate_ontology();
		String data = "";
		if (dataToPlot.equals("PercentOfCases")) {
			data = "TotalCases";
		}else {
			data = dataToPlot;
		}
		
		String queryString =
				"PREFIX NS: <http://www.covidapp.org/ontology#> "+
				"SELECT * " +
				"WHERE { ?Report NS:hasDate \"" + dateString + "\"^^<http://www.w3.org/2001/XMLSchema#date> ."
						+ "?Report NS:has"+data+" ?Data .}" +
				        "ORDER BY DESC(?Data) ";
		SwingUtilities.invokeLater(() -> {
		      BarChart tmp=new BarChart("Bar chart ",OntCovid,queryString,dataToPlot);
		      tmp.setSize(800, 400);
		      tmp.setLocationRelativeTo(null);
		      tmp.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		      tmp.setVisible(true);
		      try {
				  tmp.setIconImage(ImageIO.read(new File("./src/images/Logo CovidApp.png")));
		      } catch (IOException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
		      }
		    });
	}
	
	/**
	 * This methods queries the ontology to display the trend of each required value for indicated area.
	 * @param region
	 * @param dataToPlot
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private static void queryOnt_TREND(String region,String dataToPlot) throws InvocationTargetException, InterruptedException {
		String queryString =
				"PREFIX NS: <http://www.covidapp.org/ontology#> " +
				"SELECT * " +
				"WHERE { ?Report NS:belongsTo NS:"+ region +" . " +
				        "?Report NS:hasDate ?date . }" +
				        "ORDER BY ASC(?date) ";
		
		SwingUtilities.invokeLater(() -> {
		      TimeSeriesChart tmp = new TimeSeriesChart("Time Series Chart",OntCovid,queryString,region,dataToPlot);
		      tmp.setSize(800, 400);
		      tmp.setLocationRelativeTo(null);
		      tmp.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		      tmp.setVisible(true);
		      try {
				  tmp.setIconImage(ImageIO.read(new File("./src/images/Logo CovidApp.png")));
		      } catch (IOException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
		      }
		    });
	}
	
	/**
	 * This methods queries the ontology to display the trend of each required value for indicated areas.
	 * @param region1
	 * @param region2
	 * @param dataToPlot
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private static void queryOnt_TREND(String region1, String region2, String dataToPlot) throws InvocationTargetException, InterruptedException {
		String queryString1 =
				"PREFIX NS: <http://www.covidapp.org/ontology#> " +
				"SELECT * " +
				"WHERE { ?Report NS:belongsTo NS:"+ region1 +" . " +
				        "?Report NS:hasDate ?date . }" +       
			    "ORDER BY ASC(?date) ";
		String queryString2 =
				"PREFIX NS: <http://www.covidapp.org/ontology#> " +
				"SELECT * " +
				"WHERE { ?Report NS:belongsTo NS:"+ region2 +" . " +
				        "?Report NS:hasDate ?date . }" + 
			    "ORDER BY ASC(?date) ";
		SwingUtilities.invokeLater(() -> {
		      TimeSeriesChart tmp = new TimeSeriesChart("Time Series Chart",OntCovid,queryString1,queryString2,region1,region2,dataToPlot);
		      tmp.setSize(800, 400);
		      tmp.setLocationRelativeTo(null);
		      tmp.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		      tmp.setVisible(true);
		      try {
				  tmp.setIconImage(ImageIO.read(new File("./src/images/Logo CovidApp.png")));
		      } catch (IOException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
		      }
		      
		    });
	}
	
	/**
	 * This methods queries the ontology to display the required data for the indicated date on bar chart.
	 * @param region
	 * @param year
	 * @param month
	 * @param day
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private static void queryOnt_DATA(String region, int year, int month, int day) throws InvocationTargetException, InterruptedException {
	    String dateString = "" + year;
		if (month < 10) {
			dateString += "-0" + month;
		}else {
			dateString += "-"+month;
		}
		if (day < 10) {
			dateString += "-0" + day;
		}else {
			dateString += "-"+day;
		}
		String queryString =
				"PREFIX NS: <http://www.covidapp.org/ontology#> "+
				"SELECT * " +
				"WHERE { ?Report NS:hasDate \"" + dateString + "\"^^<http://www.w3.org/2001/XMLSchema#date> ."
					+ " ?Report NS:belongsTo NS:" + region + " .}";
		Query query = QueryFactory.create(queryString) ;
		try (QueryExecution qexec = QueryExecutionFactory.create(query, OntCovid)) {
			ResultSet results = qexec.execSelect() ;
			if (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource r = soln.getResource("Report");
				String datestr=dateString;
				SwingUtilities.invokeLater(()->{
				    BarChart tmp=new BarChart(region+" data for "+datestr,OntCovid,r);
				    tmp.setSize(800, 400);
				    tmp.setLocationRelativeTo(null);
				    tmp.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				    tmp.setVisible(true);
				    try {
						tmp.setIconImage(ImageIO.read(new File("./src/images/Logo CovidApp.png")));
				    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				    }
				  });
			}
			else{
				SwingUtilities.invokeLater(()->{
					
					JFrame page_404 = new JFrame();
				    page_404.setSize(800, 400);
				    page_404.setLocationRelativeTo(null);
				    page_404.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				    page_404.setVisible(true);
				    page_404.setContentPane(new JLabel(new ImageIcon("./src/images/404_data.png")));
				    try {
				    	page_404.setIconImage(ImageIO.read(new File("./src/images/Logo CovidApp.png")));
				    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				    }
				    
				  });
			}
		}
	}
}
