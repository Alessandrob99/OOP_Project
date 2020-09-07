package OOP_Project.application.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import OOP_Project.application.models.jsonError;
import OOP_Project.application.models.review;
import OOP_Project.application.models.statResponce;
import OOP_Project.application.models.user;
/**
 * 
 * @author Bedetta Alessandro
 * 
 * <p>
 * This class maps all the routes that the user can use.
 * each rout is linked to a specific method in the 'memory' class.
 * If the user hasn't had his credentials checked already,  he won't be able to access any rout
 * Any any error that may show up during runtime in this phase is handled by the 'errorHandler' class
 * @see OOP_Project.application.controller.memory
 * OOP_Project.application.handlers.errorHandler
 * </p>
 * 
 *
 */
@RestController
public class restController {
	
	/**
	 * This rout allows the user to have a list of all the reviews made on a specific file on a precise day
	 * 
	 * @param data Indicating the date
	 * @param file Indicating the file to analyze
	 * @return A String representing a JSONArray containing all the reviews.
	 */
	@PostMapping("/dailyRev/{file}")
	public String Daily_Revs(@RequestParam(name = "date") String date,@PathVariable String file) {
		if(user.isLOGGED_IN()) {
			String jsonOut = memory.getDailyRevs(date,file);
			return jsonOut;
		}else {
			return new jsonError("The user must authenticate his credentials before using any rout",400,"UserNotLoggedError").getJson();
		}
	}
	
	/**
	 * 
	 * Once the user gives a directory path and an access token, this rout allows the credentials authentication
	 * @param token Indicating the access token to the DropBox API
	 * @param path	Indicating the directory path (default = home folder)
	 * @return If the authentication process goes well, a JSON representing the certified user is returned, otherwise the application responds with an error
	 */
	
	@PostMapping("/check")
	public String Check_User(@RequestParam(name = "token") String token,@RequestParam(name = "path", defaultValue = "") String path) {
		user.checkUser(token, path);
		String jsonOut = "\r\n" + 
				"{\r\n" + 
				"    \"path\": \""+user.getPath()+"\",\r\n" + 
				"    \"token\": \""+user.getToken()+"\",\r\n" +
				"    \"LOGGED_IN\": "+user.isLOGGED_IN()+",\r\n" + 
				"}";
		return jsonOut;
	}
	
	/**
	 * 
	 * This is the basic rout that allows the user to list all the reviews made on a specific file
	 * @param Indicates the file we want to check
	 * @return A String representing a JSONArray filled with the list of all the file reviews
	 */
	
	@GetMapping("/listRev/{file}")
	public String List_Review(@PathVariable String file){
		if(user.isLOGGED_IN()) {
			String jsonOut = memory.listReview(file);
			return jsonOut;
		}else {
			return new jsonError("The user must authenticate his credentials before using any rout",400,"UserNotLoggedError").getJson();
		}
	}
	
	
//	@GetMapping("/getMeta")
//	public String Get_Metadata() {
//		if(user.isLOGGED_IN()) {
//			String jsonOut = memory.getMetaData();
//			return jsonOut;
//		}else {
//			return new jsonError("L'utente non ha ancora effettuato il log-in",400,"UserNotLoggedError").getJson();
//		}
//	}
	
	/**
	 * Once the user feeds a file name, this rout returns a set of statistics regarding timing between reviews
	 * @param file Indicating the file to analyze
	 * @return Returns a JSON String containing all the statistics made and the relative values
	 */
	@GetMapping("/stats/{file}")
	public String Stats(@PathVariable String file) {
		if(user.isLOGGED_IN()) {
			String jsonOut = memory.getStats(file);
			return jsonOut;
		}else {
			return new jsonError("The user must authenticate his credentials before using any rout",400,"UserNotLoggedError").getJson();
		}
	}
	/**
	 * This rout allows the user to access a quick guide to all of the application features 
	 * @return A String of text ( the guide ) 
	 */
	@GetMapping("/help")
	public String Help() {
		if(user.isLOGGED_IN()) {
			return "ROUTS:\n-GET /listRev/(file_name)\nSpecifying a file name in this rout will return a list \n"
					+ "of review linked to a set of information usefull to the identification\n"
					+ "-GET /stats/(file_name)\nSpecifing a file name in this rout will return a JSONObject containing\n"
					+ "statistics regarding the time lapse between every revision made\n"
					+ "standard delle reviews effettuate\nN.B. Nelle date MM=mesi DD=giorni hh=ore mm=minuti ss = secondi\n"
					+ "-POST /dailyRev/(nome_file)\nSpecificando il nome di un file ed aggiungendo l'attributo data l'app ritornerà\n"
					+ "una lista JSON contenente tutte le reviews (con relative informazioni) effettuate in quella data\n"
					+ "(N.B. La data passata deve rispettare il formato anno-mese-giorno (Fondamentale il separatore '-')\n"
					+ "-POST /check\nQuesta è la rotta riservata all'autenticazione dell'utente, bisogna passare come parametri\n"
					+ "l'access TOKEN per collegarsi all'API Dropbox e il PATH della cartella in cui si intende lavorare\n"
					+ "Quando l'utente sarà loggato potrà leggere nel JSON di ritorno il campo LOGGED_IN diventare true";
		}else {
			return new jsonError("L'utente non ha ancora effettuato il log-in",400,"UserNotLoggedError").getJson();
		}
		
		
	}
}
