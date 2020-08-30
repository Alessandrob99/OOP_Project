package Controller;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;


/**
 * 
 * Questa classe dovrà contenere tutti i metodi per lanciare le richieste a dropbox
 * 
 * @author Utente
 *
 */


public class controller {
	
	private HttpURLConnection openConnection;
	private String token;
	private String path;
	public controller(String token, String path) {
		super();
		this.token = token;
		this.path = path;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

	/**
	 * 
	 * @param r Indica la generica richiesta r
	 * 
	 * Il compito di imppstare gli headers e' lasciato ai metodi specifici
	 * 
	 */
	
	
	public boolean openConnection(request r) {
		try {
			openConnection = (HttpURLConnection) new URL(r.getUrl_r()).openConnection();
			openConnection.setRequestMethod(r.getType_r());
			openConnection.setRequestProperty("Authorization",
					"Bearer "+r.getToken_r());
			//openConnection.setRequestProperty(r.getHead1(),r.getValue1());
			openConnection.setDoOutput(true);
		}catch(MalformedURLException e) {
			System.out.println("l'URL fornito non è valido...");
			return false;
		}catch(IOException e) {
			System.out.println("Errore durante l'inserimento...");
			return false;
		}catch(Exception e) {
			System.out.println("Errore generico");
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Metodi utili al fine li lanciare richieste
	 */
	
	
	public boolean listFolderRequest() throws IOException {
		
		String body = "{\r\n" + 
				"    \"path\": \""+this.getPath()+"\",\r\n" + 
				"    \"recursive\": false,\r\n" + 
				"    \"include_media_info\": false,\r\n" + 
				"    \"include_deleted\": false,\r\n" + 
				"    \"include_has_explicit_shared_members\": false,\r\n" + 
				"    \"include_mounted_folders\": true,\r\n" + 
				"    \"include_non_downloadable_files\": true\r\n" + 
				"}";
		
		request r = new request("https://api.dropboxapi.com/2/files/list_folder",body,this.getToken(),"POST");
		boolean flag = this.openConnection(r);
		if(flag == false) {
			return false;		// connessione NON andata a buon fine
		}else { 				// connessione andata a buon fine
			
			openConnection.setRequestProperty("Content-Type", "application/json"); //Non necessario per il list folder
			
			//Lancio richiesta
			
			try (OutputStream os = openConnection.getOutputStream()) {
				byte[] in = r.getBody_r().getBytes("utf-8");
				os.write(in, 0, in.length);
				
			}catch(UnsupportedEncodingException e) {
				System.out.println("Errore durante il lancio della richiesta");
			}catch(IOException e) {
				System.out.println("Errore durante il lancio della richiesta");
			}
			InputStream input = openConnection.getInputStream();


			String data = "";
			String line = "";
			try {
				InputStreamReader inr = new InputStreamReader(input);
				BufferedReader buf = new BufferedReader(inr);

				while ((line = buf.readLine()) != null) {
					data += line;
					System.out.println(line);
				}
			} finally {
				input.close();
			}
			try {
				JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Errore in lettura");
				e.printStackTrace();
			}
			return true;
		}
		
	}
	
}
