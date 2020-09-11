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
public class statResponse {
	/**
	 * Maximum time between 2 reviews
	 */
	private String max_time=null;
	/**
	 * Minimum time between 2 reviews
	 */
	private String min_time=null;
	/**
	 * Average time between 2 reviews
	 */
	private String avarage=null;
	/**
	 * Standard deviation made on all the reviews
	 */
	private String stdDev=null;
	
	public statResponse() {			//Default Constructor 
		super();
		this.max_time = null;
		this.min_time = null;
		this.avarage = null;
		this.stdDev = null;
	}
	public statResponse(String max_time, String min_time, String avarage, String stdDev) {		//Constructor with parameters
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

}
