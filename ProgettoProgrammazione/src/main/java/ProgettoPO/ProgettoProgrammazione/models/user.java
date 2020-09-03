package ProgettoPO.ProgettoProgrammazione.models;

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
	static private String token;
	static private String path;
	static private boolean LOGGED_IN=false;
	
	
	public static boolean checkToken(String token) {
		String url = "https://api.dropboxapi.com/2/check/user";
		String jsonBody = "{\r\n" + 
				"    \"query\": \"OK\"\r\n" + 
				"}";
		HttpURLConnection openConnection;
		try {
			openConnection = (HttpURLConnection) new URL(url).openConnection();
			openConnection.setRequestMethod("POST");
			openConnection.setRequestProperty("Authorization",
					"Bearer "+token);
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
				appoggio = (String) obj.get("result");
				if(appoggio.compareTo("OK")==0) {
					user.setToken(token);
					return true;
				}else {
					return false;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//eccezione token incorretto
				return false;
			} 
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			//eccezione connessione mal eseguita
			return false;
		}
		
	}
	
	
	public static void checkUser(String token,String path) {
		boolean checktoken = user.checkToken(token);
		if(checktoken) {
			//CheckPath
			String url = "https://api.dropboxapi.com/2/files/list_folder";
			String jsonBody = "{\r\n" + 
					"    \"path\": \""+path+"\",\r\n" + 
					"    \"recursive\": false,\r\n" + 
					"    \"include_media_info\": false,\r\n" + 
					"    \"include_deleted\": false,\r\n" + 
					"    \"include_has_explicit_shared_members\": false,\r\n" + 
					"    \"include_mounted_folders\": true,\r\n" + 
					"    \"include_non_downloadable_files\": true\r\n" + 
					"}";
			HttpURLConnection openConnection;
			try {
				openConnection = (HttpURLConnection) new URL(url).openConnection();
				openConnection.setRequestMethod("POST");
				openConnection.setRequestProperty("Authorization",
						"Bearer "+token);
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
				appoggio ="Cartella trovata\nAl suo interno sono presenti i seguenti file :\n";
				user.setPath(path);
				try {
					JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
					JSONArray arj = (JSONArray) obj.get("entries");
					for(Object o : arj) {
						obj = (JSONObject)o;
						appoggio = appoggio+"\t"+obj.get("name")+" "+obj.get("size")+"bytes\n";
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//Path incorretto exception
				}
				
				user.setLOGGED_IN(true);
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				//eccezione connesione mal eseguita;
			}
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
