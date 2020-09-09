package OOP_Project.application.models;
/**
 *<p>
 *This class allows to store all the information regarding the user credentials once the authentication procedure is completed successfully
 *</p> 
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class user {
	static private String token = "";
	static private String path = "";
	static private boolean LOGGED_IN=false;
	
	/**
	 * This method allows to check the access token by using a checkUser API request
	 * if the token is certified the setToken method is called and the program proceeds to check the folder path
	 * @param token Indicates the API access token given by the user.
	 */
	public static void checkToken(String token) {
		String url = "https://api.dropboxapi.com/2/check/user";				//To validate the token we use the checkUser request
		String jsonBody = "{\r\n" + 
				"    \"query\": \"OK\"\r\n" + 
				"}";
		HttpURLConnection openConnection;
		try {
			openConnection = (HttpURLConnection) new URL(url).openConnection(); //Opening connection and setting all the properties
			openConnection.setRequestMethod("POST");
			openConnection.setRequestProperty("Authorization",
					"Bearer "+token);
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
				BufferedReader buf = new BufferedReader(inR);
				data = buf.readLine();
			} finally {
				in.close();
			}
			try {
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
				appoggio = (String) obj.get("result");
				if(appoggio.compareTo("OK")==0) {
					user.setToken(token);
				}
			} catch (ParseException e) {
				
			} 
			
		}catch (IOException e) {			//If the token is not valid, sending the request throws an exception and so the token field is left blank
			
		}
		
	}
	
	/**
	 * If the checkToken methods confirms that the token is valid this method proceeds to check if the directory path.\
	 * specified by the user is existing.
	 * @param token The access token to check using checkToken method.
	 * @param path The folder path to check.
	 */
	public static void checkUser(String token,String path) {
		user.setPath("");		//
		user.setToken("");		// refreshing fields
		user.setLOGGED_IN(false);//
		user.checkToken(token);				//First we check if the given token is correct
		if(user.getToken().compareTo("")!=0) {//if the token is validated we proceed to check the path
			//CheckPath
			String url = "https://api.dropboxapi.com/2/files/list_folder";
			String jsonBody = "{\r\n" + 
					"    \"path\": \""+path+"\",\r\n" + 	//JSON body with the wanted path
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
						"Bearer "+token);
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
  				user.setPath(path);		// If the operation is concluded without any exception the path is correct
				user.setLOGGED_IN(true);
				
			}catch (IOException e) {
				user.setPath("InvalidPath");	//Otherwise an exception is thrown and the path field shows the error
			}
		}else {		//if the token is incorrect the token field shows the error 
			user.setToken("InvalidToken");
		}
	}


	public static String getToken() {
		return token;
	}


	public static void setToken(String token) {
		user.token = token;
	}


	public static String getPath() {
		return path;
	}


	public static void setPath(String path) {
		user.path = path;
	}


	public static boolean isLOGGED_IN() {
		return LOGGED_IN;
	}


	public static void setLOGGED_IN(boolean lOGGED_IN) {
		LOGGED_IN = lOGGED_IN;
	}
	

	
}
