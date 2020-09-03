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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ProgettoPO.ProgettoProgrammazione.handlers.errorHandler;
import ProgettoPO.ProgettoProgrammazione.models.review;
import ProgettoPO.ProgettoProgrammazione.models.statResponce;
import ProgettoPO.ProgettoProgrammazione.models.user;


@RestController
public class restController {
	

	
	@PostMapping("/listRev/{file}")
	public JSONArray List_Review(@PathVariable String file){
		if(user.isLOGGED_IN()) {
			JSONArray outPut = memory.listReview(file);
			return outPut;
		}else {
			//Utente non loggato exception
			return null;
		}
	}
	
	@PostMapping("/check")
	public String Check_User(@RequestParam(name = "token") String token,@RequestParam(name = "path", defaultValue = "") String path) {
		user.checkUser(token, path);
		String jsonOut = "\r\n" + 
				"{\r\n" + 
				"    \"path\": \"/"+user.getPath()+"\",\r\n" + 
				"    \"token\": \"/"+user.getToken()+"\",\r\n" +
				"    \"LOGGED_IN\": "+user.isLOGGED_IN()+",\r\n" + 
				"}";
		return jsonOut;
	}
	
	@GetMapping("/stats/{file}")
	public String Stats(@PathVariable String file) {
		if(user.isLOGGED_IN()) {
			ObjectMapper objMap = new ObjectMapper();
			statResponce sr = memory.getStats(file);
			String jsonOut;
			try {
				jsonOut = objMap.writeValueAsString(sr);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				return "Errore durante  la conversione del json";
			}
			return jsonOut;
		}else {
			return "Prima di svolgere qualsiasi operazione è necessario specificare le credenziali:\n-Token di accesso all'API DropBox\n-Path della cartella in cui si intende lavorare\nUsare la rotta /check con parametri:\n -token\n -path";
		}
	}
	
}
