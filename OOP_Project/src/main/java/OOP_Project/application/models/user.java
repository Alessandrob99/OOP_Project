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

import OOP_Project.application.handlers.requestHandler;
import OOP_Project.application.models.exceptions.invalidPathException;
import OOP_Project.application.models.exceptions.invalidTokenException;

public class user {
	/**
	 * The DropBox API access token
	 */
	static private String token = "";
	/**
	 * The path indicating the directory we want to work with
	 */
	static private String path = "";
	/**
	 * A boolean indicating whether the user is logged in or not
	 */
	static private boolean LOGGED_IN=false;
	
	/**
	 * This method allows to check the access token by using a checkUser API request
	 * if the token is certified the setToken method is called and the program proceeds to check the folder path
	 * @param token Indicates the API access token given by the user.
	 */
	public static void checkToken(String token) {
		user.setToken(token);
		String url = "https://api.dropboxapi.com/2/check/user";				//To validate the token we use the checkUser request
		String jsonBody = "{\r\n" + 
				"    \"query\": \"OK\"\r\n" + 
				"}";
		String appoggio = "";
		
		requestHandler rh = new requestHandler();
		Object o = rh.sendRequest(jsonBody, "POST", url);
		try {
			if(o instanceof jsonError) {
				if(((jsonError) o).getError_code()==404) throw new invalidTokenException();
				if(((jsonError) o).getError_code()==500) throw new invalidTokenException();
			}
			String data = (String)o;
			try {
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
				appoggio = (String) obj.get("result");
				if(appoggio.compareTo("OK")==0) {
					user.setToken(token);
				}
			} catch (ParseException e) {
				
			}
		}catch (invalidTokenException e) {			//If the token is not valid, sending the request throws an exception and so the token field is left blank
			user.setToken("");
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
			requestHandler rh = new requestHandler();
			Object o = rh.sendRequest(jsonBody, "POST", url);
			try {
				if(o instanceof jsonError) {
					if(((jsonError) o).getError_code()==404) throw new invalidPathException();
					if(((jsonError) o).getError_code()==500) throw new invalidPathException();
				}
  				user.setPath(path);		// If the operation is concluded without any exception the path is correct
				user.setLOGGED_IN(true);
			}catch(invalidPathException e) {
				user.setToken("InvalidPath");
			}
				
		}else {		//if the token is incorrect the token field shows the error 
			user.setToken("InvalidToken");
		}
	}

	/**
	 * 
	 * @return token
	 */
	public static String getToken() {
		return token;
	}

	/**
	 * 
	 * @param token
	 */
	public static void setToken(String token) {
		user.token = token;
	}

	/**
	 * 
	 * @return path
	 */
	public static String getPath() {
		return path;
	}

	/**
	 * 
	 * @param path
	 */
	public static void setPath(String path) {
		user.path = path;
	}

	/**
	 * 
	 * @return LOGGED_IN
	 */
	public static boolean isLOGGED_IN() {
		return LOGGED_IN;
	}

	/**
	 * 
	 * @param lOGGED_IN
	 */
	public static void setLOGGED_IN(boolean lOGGED_IN) {
		LOGGED_IN = lOGGED_IN;
	}
	

	
}
