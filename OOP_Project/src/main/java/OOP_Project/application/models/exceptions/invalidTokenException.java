package OOP_Project.application.models.exceptions;

public class invalidTokenException extends Exception{

	public invalidTokenException() {
		super("The given token is invalid");
	}
	
}
