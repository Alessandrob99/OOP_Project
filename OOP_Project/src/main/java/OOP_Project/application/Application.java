package OOP_Project.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 
 * 
 * 
 * @author Bedetta Alessandro
 * 
 * <p>
 *<em>This class starts the web rest application</em>
 *@version 1.5
 *</p>
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args){
		SpringApplication.run(Application.class, args);  //Starts the web rest app
	}

}
