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




@RestController
public class restController {
	
	
	@PostMapping("/listRev/{file}")
	public String List_Review(@PathVariable String file){
		//memory.listReview(file);
		Vector<review> reviews = new Vector();
		review r = new review();
		
		String url = "https://api.dropboxapi.com/2/files/list_revisions";
		String jsonBody = "{\r\n" + 
				"    \"path\": \"/Cartella1/"+file+"\",\r\n" + 
				"    \"mode\": \"path\",\r\n" + 
				"    \"limit\": 10\r\n" + 
				"}";
		HttpURLConnection openConnection;
		try {
			openConnection = (HttpURLConnection) new URL(url).openConnection();
			openConnection.setRequestMethod("POST");
			openConnection.setRequestProperty("Authorization",
					"Bearer fayx-PTVhVQAAAAAAAAAAWMhqd6cVTWq7ceaB66i2Bs_w1vKQftfONFjSA7r0fhc");
			openConnection.setDoOutput(true);
			openConnection.setRequestProperty("Content-Type", "application/json");
			OutputStream os = openConnection.getOutputStream();
			byte[] input = jsonBody.getBytes("utf-8");
			os.write(input, 0, input.length);
			InputStream in = openConnection.getInputStream();

			String data = "";
			String appoggio = "";
			try {
				InputStreamReader inR = new InputStreamReader(in);
				BufferedReader buf = new BufferedReader(inR);
				data = buf.readLine();
			} finally {
				in.close();
			}
			try {
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
				JSONArray arj = (JSONArray) obj.get("entries");
				ObjectMapper objMap = new ObjectMapper();
				for(Object rev:arj) {
					JSONObject obj1 = (JSONObject) rev;
					appoggio = obj1.toString();
					r = objMap.readValue(appoggio,review.class);
					reviews.add(r);
				}
				appoggio = "";
				for(int i =0;i<reviews.size();i++) {
					appoggio+="Review n° "+(reviews.size()-i)+"\n";
					appoggio+= reviews.elementAt(i).toString()+"\n";
				}
				return appoggio;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return "Errore durante l'acquisizione delle review";
			} 
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			return "Errore indirizzo invalido";
		}
		
	}
}
