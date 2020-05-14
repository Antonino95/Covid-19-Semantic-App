
package ontology;
/**
 * This Class needs to manage values of Region into project.
 */
public class dataRegion {
	private int cod;
	private int population;
	private double latitude;
	private double longitude;
	/**
	 * 
	 * @param id: It rapresents code region
	 * @param pop: It rapresents population of region
	 * @param lat: It rapresents latitude region
	 * @param lon: It rapresents longitude region
	 */
	public dataRegion(int id,int pop,double lat,double lon) {
		this.cod = id;
		this.population = pop;
		this.latitude = lat;
		this.longitude = lon;
	}
	/**
	 * 
	 * @return code region
	 */
	public int getCode() {
		return this.cod;
	}
	/**
	 * 
	 * @return population
	 */
	public int getPopulation() {
		return this.population;
	}
	/**
	 * 
	 * @return latitude
	 */
	public double getLatitude() {
		return this.latitude;
	}
	/**
	 * 
	 * @return longitude
	 */
	public double getLongitude() {
		return this.longitude;
	}
	@Override
    public String toString() 
    { 
        return "\n----------\nCode --> "+ this.cod+"\n Lat --> "+this.latitude+"\n Long --> "+this.longitude+"\n Population --> "+this.population;
    }
}
