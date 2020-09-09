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
import OOP_Project.application.models.dailyRevsResponse;
import OOP_Project.application.models.jsonError;
import OOP_Project.application.models.review;
import OOP_Project.application.models.statResponse;
import OOP_Project.application.models.user;

/**
 * @author Bedetta Alessandro
 * 
 * <p>
 * <b>CLASS</b> which represents the real working center of the program.
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
	static ArrayList<review> reviews = new ArrayList();


	/**
	 * <b>Method</b> needed to send the listReviews request and store all the data in the 'reviews' vector.
	 * if the operation goes well, the 'reviews' vector is filled up, otherwise an error is returned.
	 * @param file The file we are working with.
	 * @return A String representing a JSONArray containing all the reviews made on that file.
	 */
	public static Object listReview(String file){
		String url = "https://api.dropboxapi.com/2/files/list_revisions"; 		
		String jsonBody = "{\r\n" + 
				"    \"path\": \""+user.getPath()+"/"+file+"\",\r\n" + 
				"    \"mode\": \"path\",\r\n" + 
				"    \"limit\": 99\r\n" + 
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
				reviews.removeAll(reviews);		//Before adding the reviews we refresh the vector, so we don't risk to duplicate any data
				for(Object rev:arj) {
					JSONObject obj1 = (JSONObject) rev;
					app = obj1.toString();		
					r = objMap.readValue(app,review.class);
					reviews.add(r);					//adding the reviews in the 'local memory'
				}
				return reviews;			//The array list  is returned
			} catch (ParseException e) {
				return new jsonError("An error occurred during json parsing",500,"JSONParsingError");

			}
		}catch (IOException e) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError");
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
	 * @return A JSONObject containing all the statistics.
	 */
	public static Object getStats(String file){
		reviews.removeAll(reviews);
		memory.listReview(file); 		//'Downloading' all the reviews linked to the file to analyze
		if(reviews.isEmpty()) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError");
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
				//This 2 commands allow to parse the dates coming from the review , as in their ISO form are not easy to elaborate 
				date1=(reviews.get(i).getServer_modified().substring(0,reviews.get(i).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.get(i).getServer_modified().substring(0,reviews.get(i).getServer_modified().length()-1)).substring(11);
				date2=(reviews.get(i+1).getServer_modified().substring(0,reviews.get(i+1).getServer_modified().length()-1)).substring(0,10)+" "+(reviews.get(i+1).getServer_modified().substring(0,reviews.get(i+1).getServer_modified().length()-1)).substring(11);

				try {
					d1 = sdf.parse(date1);  //
					d2 = sdf.parse(date2);  //Converting the parsed String dates to Date instances
					difference = d1.getTime()-d2.getTime();
					totalTime+=difference;
					if(difference<minDifference) minDifference = difference;
					if(difference>maxDifference) maxDifference = difference;
					
				} catch (java.text.ParseException e) {
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
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
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
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
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
				}
				
			}
			long devStandard = (long) (Math.sqrt((totalTime))/reviews.size());  //Final formula for the deviation
			
			sr.setMax_time(dateFormatHandler.toString(maxDifference));
			sr.setMin_time(dateFormatHandler.toString(minDifference)); // All the response's field ate filled in;
			sr.setStdDev(dateFormatHandler.toString(devStandard));
			
		}
		return sr; // the response is returned
	}
	
	
	
	
	/**
	 * Once the user specifies a date and a file name, the <b>method</b> returns the list
	 * of reviews made on the specified file on that day.
	 * At the beginning the 'listReview' method is called and so the 'reviews' vector is updated
	 * The method also checks the validity of the date using the 'dateFormatHandler' class
	 * @see OOP_Project.application.handlers.dateFormatHandler
	 * @param date The specified date
	 * @param file The specified file
	 * @return JSONArray containing all the reviews the haven't been filtered.
	 */
	public static Object getDailyRevs(String date,String file) {
		String correctDate = dateFormatHandler.checkFormat(date); //the date is sent to the dateFormatHandler for checking
		if(correctDate.compareTo("")==0) {//If the date is invalid an error is returned
			return new jsonError("The specified date is not correct (respect the yyyy-mm-dd form)",400,"InvalidParametherError");
		}
		ArrayList<review> dailyReviews = new ArrayList();
		String app;
		reviews.removeAll(reviews);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		
		Object test = memory.listReview(file); // The reviews vector is refreshed
		if(test instanceof jsonError) return new jsonError("An error occurred during the listReview call (check file name)",500,"InternalServerError");
		for(review r : reviews) {
			app = r.getClient_modified().substring(0,10);
			if(app.compareTo(correctDate)==0) dailyReviews.add(r); 
		}
		return new dailyRevsResponse(dailyReviews);
	}



	/**
	 * 
	 * This method returns all the review made in a specific day
	 * @param date The specified date 
	 * @return An ArrayList with all the reviews made on that day on all the file in the directory
	 */
	public static Object getAllDailyRevs(String date) {
		String correctDate = dateFormatHandler.checkFormat(date); //the date is sent to the dateFormatHandler for checking
		if(correctDate.compareTo("")==0) {//If the date is invalid an error is returned
			return new jsonError("The specified date is not correct (respect the yyyy-mm-dd form)",400,"InvalidParametherError");
		}
		ArrayList<review> dailyReviews = new ArrayList();
		String app;
		reviews.removeAll(reviews);
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		ArrayList<String> files = (ArrayList<String>) memory.listFolder();  // we use the listFolder method to get all the file names in the directory
		
		for(String name: files) {		//For each file we search for the wanted reviews
			Object test = memory.listReview(name); // The reviews vector is refreshed
			for(review r : reviews) {
				app = r.getClient_modified().substring(0,10);
				if(app.compareTo(correctDate)==0) dailyReviews.add(r); 
			}
		
		}	
		return new dailyRevsResponse(dailyReviews);
	}
	/**
	 * 
	 * This method needs to help the getAllDailyRevs method getting all the file names in the folder
	 * @return An Array list containing all the names (String)
	 */
	public static Object listFolder() {
		ArrayList<String> files = new ArrayList();
		String url = "https://api.dropboxapi.com/2/files/list_folder";
		String jsonBody = "{\r\n" + 
				"    \"path\": \""+user.getPath()+"\",\r\n" + 	//JSON body with the wanted path
				"    \"recursive\": false,\r\n" + 
				"    \"include_media_info\": false,\r\n" + 
				"    \"include_deleted\": false,\r\n" + 
				"    \"include_has_explicit_shared_members\": false,\r\n" + 
				"    \"include_mounted_folders\": true,\r\n" + 
				"    \"include_non_downloadable_files\": true\r\n" + 
				"}";
		HttpURLConnection openConnection;
		try {
			openConnection = (HttpURLConnection) new URL(url).openConnection(); //Opening connection and setting all the properties
			openConnection.setRequestMethod("POST");
			openConnection.setRequestProperty("Authorization",
					"Bearer "+user.getToken());
			openConnection.setDoOutput(true);
			openConnection.setRequestProperty("Content-Type", "application/json");
			OutputStream os = openConnection.getOutputStream();
			byte[] input = jsonBody.getBytes("utf-8");
			os.write(input, 0, input.length);									//the request is written in bytes and sent
			InputStream in = openConnection.getInputStream();

			String data = "";
			String appoggio = "";
			try {
				InputStreamReader inR = new InputStreamReader(in);
				BufferedReader buf = new BufferedReader(inR);	//Opening an input stream to catch the response from the API
				data = buf.readLine();
			} finally {
				in.close();
			}
			JSONObject obj;
			try {
				obj = (JSONObject) JSONValue.parseWithException(data);
				JSONArray arj = (JSONArray) obj.get("entries");
				for(Object o : arj) {
					obj = (JSONObject)o;
					files.add((String) obj.get("name"));  // Here we are reading all the files name in the folder
				}
			} catch (ParseException e) {
				return new jsonError("An error occurred during json parsing",500,"JSONParsingError");
			}
			return files;								// Sending back the names to the main method
		}catch (IOException e) {
			return new jsonError("An error occurred during the listFolder request",500,"RequestError");	//Otherwise an exception is thrown and the path field shows the error
		}
	}



	/**
	 * 
	 * Once the user specified a date and a file name this method returns all the reviews made in the week containing that date on the given file
	 * @param date The day of the week we want to analyze
	 * @param file The file we want to check
	 * @return An ArrayList containing all the reviews
	 */
	public static Object getWeeklyRevs(String date, String file) {
		String correctDate = dateFormatHandler.checkFormat(date); //the date is sent to the dateFormatHandler for checking
		if(correctDate.compareTo("")==0) {//If the date is invalid an error is returned
			return new jsonError("The specified date is not correct (respect the yyyy-mm-dd form)",400,"InvalidParametherError");
		}
		ArrayList<review> weeklyRevs = new ArrayList();
		//Now we are going to find the week corresponding to that date
		Calendar cal = Calendar.getInstance(Locale.ITALY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateToCheck;
		try {
			dateToCheck = sdf.parse(correctDate);
		} catch (java.text.ParseException e) {
			return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
		}
		cal.setTime(dateToCheck);		//We are using the Calendar class to get the week number
		int weekToCheck = cal.get(Calendar.WEEK_OF_YEAR);// Week of the given date
		
		String app;
		reviews.removeAll(reviews);
		
		Object test = memory.listReview(file); // The reviews vector is refreshed
		if(test instanceof jsonError) return new jsonError("An error occurred during the listReview call (check file name)",500,"InternalServerError");
		for(review r : reviews) {
			app = r.getClient_modified().substring(0,10);
			try {
				cal.setTime(sdf.parse(app));
			} catch (java.text.ParseException e) {
				return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
			}	
			if(cal.get(Calendar.WEEK_OF_YEAR)==weekToCheck) weeklyRevs.add(r); 
		}
		return new dailyRevsResponse(weeklyRevs);

	}




	/**
	 * Once the user specified a date ,this method returns all the reviews made in the week containing that date
	 * @param date The day of the week we want to analyze
	 * @return An ArrayList containing all the reviews
	 */
	public static Object getAllWeeklyRevs(String date) {
		String correctDate = dateFormatHandler.checkFormat(date); //the date is sent to the dateFormatHandler for checking
		if(correctDate.compareTo("")==0) {//If the date is invalid an error is returned
			return new jsonError("The specified date is not correct (respect the yyyy-mm-dd form)",400,"InvalidParametherError");
		}
		ArrayList<review> weeklyRevs = new ArrayList();
		//Now we are going to find the week corresponding to that date
		Calendar cal = Calendar.getInstance(Locale.ITALY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateToCheck;
		try {
			dateToCheck = sdf.parse(correctDate);
		} catch (java.text.ParseException e) {
			return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
		}
		cal.setTime(dateToCheck);		//We are using the Calendar class to get the week number
		int weekToCheck = cal.get(Calendar.WEEK_OF_YEAR);// Week of the given date
		
		String app;
		reviews.removeAll(reviews);
		ArrayList<String> files = (ArrayList<String>) memory.listFolder(); // we use listFolder to obtain the list of files
		
		for(String name: files) {											//we search for reviews in each file
			memory.listReview(name); // The reviews vector is refreshed
			for(review r : reviews) {
				app = r.getClient_modified().substring(0,10);
				try {
					cal.setTime(sdf.parse(app));							//we get the week number of the review we want to check
				} catch (java.text.ParseException e) {
					return new jsonError("An error occurred during the date parsing",500,"DateParsingError");
				}	
				if(cal.get(Calendar.WEEK_OF_YEAR)==weekToCheck) weeklyRevs.add(r); 
			}
			
		}
		return new dailyRevsResponse(weeklyRevs);
	}
	
	
}


