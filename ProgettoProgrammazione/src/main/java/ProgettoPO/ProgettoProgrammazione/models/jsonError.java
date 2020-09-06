package ProgettoPO.ProgettoProgrammazione.models;

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
	public String getJson() {
		return "{\r\n" + 
				"    \"name\": \""+this.name+"\",\r\n" + 
				"    \"description\": \""+this.message+"\",\r\n" + 
				"    \"error_code\": "+this.error_code+"\r\n" + 
				"}";
	}
}
