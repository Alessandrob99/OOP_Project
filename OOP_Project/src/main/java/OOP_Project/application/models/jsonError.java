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
	/**
	 * A message describing the error
	 */
	private String message; 	
	/**
	 * A code to classify the error
	 */
	private int error_code;
	/**
	 * A name to identify the error
	 */
	private String name;
	public jsonError(String message, int error_code, String reason) {
		super();
		this.message = message;
		this.error_code = error_code;
		this.name = reason;
	}
	public jsonError() {
		super();
		this.message = "";
		this.error_code = 0;
		this.name = "";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getError_code() {
		return error_code;
	}
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
