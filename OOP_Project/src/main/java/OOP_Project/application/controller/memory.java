package OOP_Project.application.controller;

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
import java.nio.file.InvalidPathException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import OOP_Project.application.handlers.dateFormatHandler;
import OOP_Project.application.models.jsonError;
import OOP_Project.application.models.review;
import OOP_Project.application.models.statResponce;
import OOP_Project.application.models.user;

/**
 * @author Bedetta Alessandro
 * 
 * <p>
 * <em>CLASS</em> which represents the real working center of the program.
 * It has all the necessary methods which respond to the requests made by the user.
 * It also acts as memory, indeed, each time a listReview request is made, 
 * all the reviews are stored in a vector; later on they will be processed by the application.
 * </p>
 * 
 *
 */

public class memory {
	
	/**
	 * This is the vector in which all the data coming from the listReviews are stored
	 * it is surely the most important variable of the entire program
	 */
	static Vector<review> reviews = new Vector();


	/**
	 * <b>Method</b> needed to send the listReviews request and store all the data in the 'reviews' vector.
	 * if the operation goes well, the 'reviews' vector is filled up, otherwise an error is returned.
	 * @param file The file we are working with.
	 * @return A String representing a JSONArray containing all the reviews made on that file.
	 */
	public static String listReview(String file){
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
			String app = "";
			try {
				InputStreamReader inR = new InputStreamReader(in);
				BufferedReader buf = new BufferedReader(inR);
				data = buf.readLine();
			} finally {
				in.close();
			}
			try {
				review r = new review();
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
				JSONArray arj = (JSONArray) obj.get("entries");
				ObjectMapper objMap = new ObjectMapper();
				reviews.removeAllElements();
				for(Object rev:arj) {
					JSONObject obj1 = (JSONObject) rev;
					app = obj1.toString();
					r = objMap.readValue(app,review.class);
					reviews.add(r);
				}
				app = "";
				for(int i =0;i<reviews.size();i++) {
					app+="Review n° "+(reviews.size()-i)+"\n";
					app+= reviews.elementAt(i).toString()+"\n";
				}
				return arj.toJSONString();
			} catch (ParseException e) {
				return new jsonError("An error occurred during json parsing",500,"JSONParsingError").getJson();

			}
		}catch (IOException e) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError").getJson();
		}

	}
	
	
	
	
	/**
	 * Once the user specified a file name, the <b>Method</b> shows statistics made on the file, 
	 * regarding the time lapses between all the requests made on it.
	 * At the beginning the 'listReview' method is called and so the 'reviews' vector is updated
	 * The method relies on 'dateFormatHandler' class to handle the date format.
	 * The method also relies on 'statResponce' class to combine all the statistics in a single JSONObject.
	 * @see OOP_Project.application.handlers.dateFormatHandler
	 * OOP_Project.application.models.statResponce
	 * @param file That file which has been specified
	 * @return A String representing a JSONObject containing all the statistics.
	 */
	public static String getStats(String file){
		reviews.removeAllElements();
		memory.listReview(file); 
		if(reviews.isEmpty()) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError").getJson();
		}
		statResponce sr = new statResponce();
		if(reviews.size()==1) {
			sr.setAvarage(dateFormatHandler.toString(0));
			sr.setMax_time(dateFormatHandler.toString(0));
			sr.setMin_time(dateFormatHandler.toString(0));
			sr.setStdDev(dateFormatHandler.toString(0));
		}else {
			String date1="";
			String date2="";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			long difference=0;
			long totalTime = 0;
			long maxDifference = 0;
			long minDifference = Long.MAX_VALUE;
			long avarage=0;
			Date d1;
			Date d2;
			for(int i =0;(i<reviews.size()-1);i++) {
				date1=(reviews.elementAt(i).getServer_modified().substring(0,reviews.elementAt(i).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.elementAt(i).getServer_modified().substring(0,reviews.elementAt(i).getServer_modified().length()-1)).substring(11);
				date2=(reviews.elementAt(i+1).getServer_modified().substring(0,reviews.elementAt(i+1).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.elementAt(i+1).getServer_modified().substring(0,reviews.elementAt(i+1).getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(date1);
					d2 = sdf.parse(date2);
					difference = d1.getTime()-d2.getTime();
					totalTime+=difference;
					if(difference<minDifference) minDifference = difference;
					if(difference>maxDifference) maxDifference = difference;
					
				} catch (java.text.ParseException e) {
					return new jsonError("An error occurred during the date parsing",500,"JSONParsingError").getJson();
				}
			}
			
			avarage = totalTime/(reviews.size()-1);
			
			//Standard Dev
			totalTime = 0;
			for(review r : reviews) {
				date1 = r.getServer_modified().substring(0,r.getServer_modified().length()-1).substring(0,10)+" "+(r.getServer_modified().substring(0,r.getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(date1);
					totalTime += d1.getTime();
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			sr.setAvarage(dateFormatHandler.toString(avarage));
			avarage = totalTime/(reviews.size());	
			totalTime = 0; 
			for(review r : reviews) {
				date1 = r.getServer_modified().substring(0,r.getServer_modified().length()-1).substring(0,10)+" "+(r.getServer_modified().substring(0,r.getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(date1);
					totalTime =(long) Math.pow((d1.getTime()-avarage),2);
				} catch (java.text.ParseException e) {
					return new jsonError("An error occurred during the date parsing",500,"JSONParsingError").getJson();
				}
				
			}
			long devStandard = (long) (Math.sqrt((totalTime))/reviews.size()); 
			
			sr.setMax_time(dateFormatHandler.toString(maxDifference));
			sr.setMin_time(dateFormatHandler.toString(minDifference));
			sr.setStdDev(dateFormatHandler.toString(devStandard));
			
		}
		return sr.toString();
	}
	
	
	
	
	/**
	 * Once the user specifies a date and a file name, the <b>method</b> returns the list
	 * of reviews made on the specified file on that day.
	 * At the beginning the 'listReview' method is called and so the 'reviews' vector is updated
	 * The method also checks the validity of the date using the 'dateFormatHandler' class
	 * @see OOP_Project.application.handlers.dateFormatHandler
	 * @param date The specified date
	 * @param file The specified file
	 * @return A String representing a JSONArray containing all the reviews the haven't been filtered.
	 */
	public static String getDailyRevs(String date,String file) {
		String correctDate = dateFormatHandler.checkFormat(date);
		if(correctDate.compareTo("")==0) {
			return new jsonError(" ",400,"InvalidParametherError").getJson(); 
		}
		reviews.removeAllElements();
		memory.listReview(file); 
		if(reviews.isEmpty()) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError").getJson();
		}
		JSONArray arj = new JSONArray();;
		String modifiedAt[];
		
		for(review r : reviews) {
			modifiedAt = r.getServer_modified().split("T");
			if (modifiedAt[0].compareTo(correctDate)==0) {
				arj.add(r);
			}
		}
		if(arj==null) return "";
		return arj.toJSONString();
	}
	
	
	
	
//	public static String getMetaData() {
//		
//	}
}


