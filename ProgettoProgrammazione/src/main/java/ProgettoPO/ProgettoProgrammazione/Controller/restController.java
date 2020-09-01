package ProgettoPO.ProgettoProgrammazione.Controller;

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


import com.fasterxml.jackson.databind.ObjectMapper;


import ProgettoPO.ProgettoProgrammazione.models.review;
import ProgettoPO.ProgettoProgrammazione.models.user;
import ProgettoPO.ProgettoProgrammazione.myErrors.errorHandler;



@RestController
public class restController {
	
	private static boolean LOGGED_IN = false;
	
	@PostMapping("/listRev/{file}")
	public String List_Review(@PathVariable String file){
		if(LOGGED_IN) {
			String outPut = memory.listReview(file);
			return outPut;
		}else {
			return "Prima di svolgere qualsiasi operazione è necessario specificare le credenziali:\n-Token di accesso all'API DropBox\n-Path della cartella in cui si intende lavorare\nUsare la rotta /check con parametri:\n -token\n -path";
		}
	}
	
	@PostMapping("/check")
	public String Check_User(@RequestParam(name = "token") String token,@RequestParam(name = "path", defaultValue = "") String path) {
		boolean checkt;
		String checkp;

		checkt = user.checkToken(token);
		if(!checkt) {
			return "Il token fornito potrebbe essere scaduto o non valido";
		}else {
			checkp = user.checkPath(token, path);
			if(checkp.compareTo("error")==0) return "Il path fornito non è valido";
		}	
		LOGGED_IN = true;	
		return checkp;
	}
	
	
}
