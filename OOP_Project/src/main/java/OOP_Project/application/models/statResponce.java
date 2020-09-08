package OOP_Project.application.models;
/**
 * 	
 * @author Bedetta Alessandro
 * 
 * <p>
 * This class is used to store the information coming from the getStats method in the memory class
 * It has all the getters and setters methods and also an overridden toString method.
 * </p>
 *
 */
public class statResponce {
	private String max_time=null;
	private String min_time=null;
	private String avarage=null;
	private String stdDev=null;
	public statResponce() {
		super();
		this.max_time = null;
		this.min_time = null;
		this.avarage = null;
		this.stdDev = null;
	}
	public statResponce(String max_time, String min_time, String avarage, String stdDev) {
		super();
		this.max_time = max_time;
		this.min_time = min_time;
		this.avarage = avarage;
		this.stdDev = stdDev;
	}
	public String getMax_time() {
		return max_time;
	}
	public void setMax_time(String max_time) {
		this.max_time = max_time;
	}
	public String getMin_time() {
		return min_time;
	}
	public void setMin_time(String min_time) {
		this.min_time = min_time;
	}
	public String getAvarage() {
		return avarage;
	}
	public void setAvarage(String avarage) {
		this.avarage = avarage;
	}
	public String getStdDev() {
		return stdDev;
	}
	public void setStdDev(String stdDev) {
		this.stdDev = stdDev;
	}
	/**
	 * This is the overridden toString method that returns the String representation of the 
	 * JSON Object containing the statResponce instance.
	 */
	public String toString() {
		return "{\r\n" + 
				"    \"max_time\" : \""+this.getMax_time()+"\",\r\n" + 
				"    \"min_time\" : \""+this.getMin_time()+"\",\r\n" + 
				"    \"avarage\" : \""+this.getAvarage()+"\",\r\n" + 
				"    \"devStd\" : \""+this.getStdDev()+"\",\r\n" + 
				"}";
	}
}
