package ProgettoPO.ProgettoProgrammazione.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ProgettoPO.ProgettoProgrammazione.handlers.dateFormatHandler;
import ProgettoPO.ProgettoProgrammazione.models.review;
import ProgettoPO.ProgettoProgrammazione.models.statResponce;
import ProgettoPO.ProgettoProgrammazione.models.user;


/**
 * 
 * <b>Classe</b> che serve a mantenere le review relative ad uno specifico file in memoria
 * @author Utente
 *
 */

public class memory {
	static Vector<review> reviews = new Vector();


	static review r = new review();
	
	public static JSONArray listReview(String file) {
		String url = "https://api.dropboxapi.com/2/files/list_revisions";
		String jsonBody = "{\r\n" + 
				"    \"path\": \""+user.getPath()+"/"+file+"\",\r\n" + 
				"    \"mode\": \"path\",\r\n" + 
				"    \"limit\": 20\r\n" + 
				"}";
		HttpURLConnection openConnection;
		try {
			openConnection = (HttpURLConnection) new URL(url).openConnection();
			openConnection.setRequestMethod("POST");
			openConnection.setRequestProperty("Authorization",
					"Bearer "+user.getToken()+"");
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
				reviews.removeAllElements();
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
				return arj;
			} catch (ParseException e) {
				// lettura mal eseguita
				return null;
			} 
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			//errore path invalido
			return null;
		}

	}
	
	
	public static statResponce getStats(String file) {
		reviews.removeAllElements();
		memory.listReview(file); //popoliamo il vector con le review interessate
		if(reviews.isEmpty()) {
			return null;
		}
		String appoggio = "";
		statResponce sr = new statResponce();
		if(reviews.size()==1) {
			return null; 
		}else {
			String data1="";
			String data2="";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			long difference=0;
			long totalTime = 0;
			long maxDifference = 0;
			long minDifference = Long.MAX_VALUE;
			long avarage=0;
			Date d1;
			Date d2;
			for(int i =0;(i<reviews.size()-1);i++) {
				data1=(reviews.elementAt(i).getServer_modified().substring(0,reviews.elementAt(i).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.elementAt(i).getServer_modified().substring(0,reviews.elementAt(i).getServer_modified().length()-1)).substring(11);
				data2=(reviews.elementAt(i+1).getServer_modified().substring(0,reviews.elementAt(i+1).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.elementAt(i+1).getServer_modified().substring(0,reviews.elementAt(i+1).getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(data1);
					d2 = sdf.parse(data2);
					difference = d1.getTime()-d2.getTime();
					totalTime+=difference;
					if(difference<minDifference) minDifference = difference;
					if(difference>maxDifference) maxDifference = difference;
					
				} catch (java.text.ParseException e) {
					// eccezione lettura date
					return null;
				}
			}
			
			avarage = totalTime/(reviews.size()-1);
			
			//Deviazione standard
			totalTime = 0;
			for(review r : reviews) {
				data1 = r.getServer_modified().substring(0,r.getServer_modified().length()-1).substring(0,10)+" "+(r.getServer_modified().substring(0,r.getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(data1);
					totalTime += d1.getTime();
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			
			totalTime = 0; //La variabile qui verrà usata come numeratore della deviazione standard
			d2 = new Date(avarage);	//media delle date in cui sono state effettuate delle revisions
			//Calcolo deviazione effettiva
			for(review r : reviews) {
				data1 = r.getServer_modified().substring(0,r.getServer_modified().length()-1).substring(0,10)+" "+(r.getServer_modified().substring(0,r.getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(data1);
					totalTime =(long) Math.pow((d1.getTime()-avarage),2);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					return null;
				}
				
			}
			long deviazioneStandard = (long) (Math.sqrt((totalTime))/reviews.size()); 
			sr.setAvarage(dateFormatHandler.toString(avarage));
			sr.setMax_time(dateFormatHandler.toString(maxDifference));
			sr.setMin_time(dateFormatHandler.toString(minDifference));
			sr.setStdDev(dateFormatHandler.toString(deviazioneStandard));
			return sr;
		}
	}
}


