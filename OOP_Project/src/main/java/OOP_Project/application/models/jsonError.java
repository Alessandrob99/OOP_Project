package OOP_Project.application.models;

/**
 * 
 * @author Bedetta Alessandro
 * 
 * <p>
 * This class allows to represent the json error object that is returned
 * every time ,during runtime, an exception shows up.
 * </p>
 *
 */
public class jsonError {
	
	private String message;
	private int error_code;
	private String name;
	public jsonError(String message, int error_code, String reason) {
		super();
		this.message = message;
		this.error_code = error_code;
		this.name = reason;
	}
	/**
	 * 
	 * @return A String containing the json error object
	 */
	public String getJson() {
		return "{\r\n" + 
				"    \"name\": \""+this.name+"\",\r\n" + 
				"    \"description\": \""+this.message+"\",\r\n" + 
				"    \"error_code\": "+this.error_code+"\r\n" + 
				"}";
	}
}
