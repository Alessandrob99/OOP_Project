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
import OOP_Project.application.models.statResponse;
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
			openConnection = (HttpURLConnection) new URL(url).openConnection();		//Opening connection and setting all the properties
			openConnection.setRequestMethod("POST");
			openConnection.setRequestProperty("Authorization",
					"Bearer "+user.getToken()+"");
			openConnection.setDoOutput(true);
			openConnection.setRequestProperty("Content-Type", "application/json");	
			OutputStream os = openConnection.getOutputStream();
			byte[] input = jsonBody.getBytes("utf-8");									//the request is written in bytes and sent
			os.write(input, 0, input.length);
			InputStream in = openConnection.getInputStream();

			String data = "";
			String app = "";
			try {
				InputStreamReader inR = new InputStreamReader(in);
				BufferedReader buf = new BufferedReader(inR);
				data = buf.readLine();		//Opening an input stream to catch the response from the API
			} finally {
				in.close();			
			}
			try {
				review r = new review();
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
				JSONArray arj = (JSONArray) obj.get("entries");
				ObjectMapper objMap = new ObjectMapper();
				reviews.removeAllElements();		//Before adding the reviews we refresh the vector, so we don't risk to duplicate any data
				for(Object rev:arj) {
					JSONObject obj1 = (JSONObject) rev;
					app = obj1.toString();		
					r = objMap.readValue(app,review.class);
					reviews.add(r);					//adding the reviews in the 'local memory'
				}
				return arj.toJSONString(); 			//The JSONArray vector is returned in string
			} catch (ParseException e) {
				return new jsonError("An error occurred during json parsing",500,"JSONParsingError").getJson();

			}
		}catch (IOException e) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError").getJson();
		}			//If the request fails it only means that the file is not in the directory, since the folder path has already been checked

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
		memory.listReview(file); 		//'Downloading' all the reviews linked to the file to analyze
		if(reviews.isEmpty()) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError").getJson();
		}		//if there are no reviews it means that the file hasn't been uploaded and so it's not in the folder
		
		statResponse sr = new statResponse(); // instance of the request's response ( statResponse.class ) 
		
		if(reviews.size()==1) {		//If there's only 1 review all the values are set to 0 ( there are no time lapses between reviews ) 
			sr.setAvarage(dateFormatHandler.toString(0));
			sr.setMax_time(dateFormatHandler.toString(0));
			sr.setMin_time(dateFormatHandler.toString(0));
			sr.setStdDev(dateFormatHandler.toString(0));
		}else {
			String date1="";
			String date2="";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //The date format to read dates from the JSON
			long difference=0;
			long totalTime = 0;
			long maxDifference = 0;
			long minDifference = Long.MAX_VALUE;
			long avarage=0;
			Date d1;
			Date d2;
			for(int i =0;(i<reviews.size()-1);i++) {//CALCULATING THE MINIMUM, MAXIMUM AND AVARAGE TIME BETWEEN 2 REVIEWS
				//This 2 commands allow to parse the dates coming from the review , as in their basic form are not easy to elaborate 
				date1=(reviews.elementAt(i).getServer_modified().substring(0,reviews.elementAt(i).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.elementAt(i).getServer_modified().substring(0,reviews.elementAt(i).getServer_modified().length()-1)).substring(11);
				date2=(reviews.elementAt(i+1).getServer_modified().substring(0,reviews.elementAt(i+1).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.elementAt(i+1).getServer_modified().substring(0,reviews.elementAt(i+1).getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(date1);  //
					d2 = sdf.parse(date2);  //Converting the parsed String dates to Date instances
					difference = d1.getTime()-d2.getTime();
					totalTime+=difference;
					if(difference<minDifference) minDifference = difference;
					if(difference>maxDifference) maxDifference = difference;
					
				} catch (java.text.ParseException e) {
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError").getJson();
				}
			}
			
			avarage = totalTime/(reviews.size()-1); 
			
			//CALCULATING THE standard deviation
			totalTime = 0;
			for(review r : reviews) { // This for allows to calculate the average date on which the revisions were made
				date1 = r.getServer_modified().substring(0,r.getServer_modified().length()-1).substring(0,10)+" "+(r.getServer_modified().substring(0,r.getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(date1);
					totalTime += d1.getTime(); 
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError").getJson();
				}
			}
			sr.setAvarage(dateFormatHandler.toString(avarage));
			avarage = totalTime/(reviews.size());	// average date , this information is necessary to calculate the standard deviation
			totalTime = 0; 
			for(review r : reviews) {
				date1 = r.getServer_modified().substring(0,r.getServer_modified().length()-1).substring(0,10)+" "+(r.getServer_modified().substring(0,r.getServer_modified().length()-1)).substring(11);
				try {
					d1 = sdf.parse(date1);
					totalTime =(long) Math.pow((d1.getTime()-avarage),2);// Total time here acts as numerator for the deviation formula
				} catch (java.text.ParseException e) {
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError").getJson();
				}
				
			}
			long devStandard = (long) (Math.sqrt((totalTime))/reviews.size());  //Final formula for the deviation
			
			sr.setMax_time(dateFormatHandler.toString(maxDifference));
			sr.setMin_time(dateFormatHandler.toString(minDifference)); // All the response's field ate filled in;
			sr.setStdDev(dateFormatHandler.toString(devStandard));
			
		}
		return sr.toString(); // the response is returned
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
		String correctDate = dateFormatHandler.checkFormat(date); //the date is sent to the dateFormatHandler for checking
		if(correctDate.compareTo("")==0) {//If the date is invalid an error is returned
			return new jsonError("The specified date is not correct (respect the yyyy-mm-dd form)",400,"InvalidParametherError").getJson(); 
		}
		reviews.removeAllElements();
		memory.listReview(file); // The reviews vector is refreshed
		if(reviews.isEmpty()) {	// If the download fails an error is returned
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError").getJson();
		}
		JSONArray arj = new JSONArray(); //The array that will contain the non-filtered reviews
		String modifiedAt[];
		
		for(review r : reviews) {
			modifiedAt = r.getServer_modified().split("T");
			if (modifiedAt[0].compareTo(correctDate)==0) {	//If the date on the review is the same date specified by the user the instance is added to the array
				arj.add(r);
			}
		}
		if(arj==null) return "";	//If there are no matching reviews the method returns an empty String.
		return arj.toJSONString(); // The String representing the JSONArray is returned.
	}
	
	
	
	
//	public static String getMetaData() {
//		
//	}
}


