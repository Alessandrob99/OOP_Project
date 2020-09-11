package OOP_Project.application.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import OOP_Project.application.models.jsonError;
import OOP_Project.application.models.user;
/**
 * 
 * This class' purpose is to handle the connection to the DropBox API and send the specified requests.
 * The class also reads the response from the API and sends it back to the other methods
 * @author Bedetta Alessandro
 *
 */
public class requestHandler {
	/**
	 * This method handles the connection to the DropBox API
	 * @param method Request-method (GET - POST - DELETE..)
	 * @param url The ulr to the request we want to make
	 * @return A HttpURLConnection object if the connection is established, otherwise it returns an error.
	 */
	public Object establish(String method,String url) {
		try {
			HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();		//Opening connection and setting all the properties
			openConnection.setRequestMethod(method);
			openConnection.setRequestProperty("Authorization",
					"Bearer "+user.getToken()+"");
			openConnection.setDoOutput(true);
			openConnection.setRequestProperty("Content-Type", "application/json");
			return openConnection;	
		} catch (ProtocolException e) {
			return new jsonError("An error occurred during the connection to the API",500,"InternalServerError");
		}catch(IOException e) {
			return new jsonError("An error occurred during the connection to the API",500,"InternalServerError");
		}
	}
	/**
	 * 
	 * This method allows to send a request trough the HttpURLConnection object created with 'establish'
	 * @param body The request body 
	 * @param method Request-method (GET - POST - DELETE..) to pass to 'establish'.
	 * @param url The URL to the request we want to make to pass to 'establish'.
	 * @return The String containing the response or a JsonError
	 */
	public Object sendRequest(String body,String method, String url) {
		try {
			if(this.establish(method, url) instanceof jsonError)return new jsonError("An error occurred during the connection to the API",500,"InternalServerError");
			HttpURLConnection oc = (HttpURLConnection) this.establish(method, url);
			OutputStream os = oc.getOutputStream();
			byte[] input = body.getBytes("utf-8");									//the request is written in bytes and sent
			os.write(input, 0, input.length);
			InputStream in = oc.getInputStream();
			String data = "";
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(inR);
			data = buf.readLine();		//Opening an input stream to catch the response from the API
			in.close();
			return data;
	
		}catch(IOException e) {
			return new jsonError("The file hasn't been found in the current directory",404,"InvalidPathError");  //If an exception is thrown it surely means that the path givn by the user is not correct
		}
	}
}
