package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import Controller.*;

/**
 * 
 * 
 * <p>
 * <b>Main</b> per la gestione del progetto
 * 
 * @version 1.1
 * 
 * @author Bedetta Alessandro
 *
 */

public class main {

	public static void main(String[] args) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		boolean flag1,flag2=true;
		Scanner input = new Scanner(System.in);
		/**
		 * Indica la posizione della cartella in cui lavoreremo
		 */
		do {
			System.out.println("Inserisce path Cartella");
			String path = input.nextLine();		
			System.out.println("Inserire Token di accesso :");
			String token = input.nextLine();
			controller cc = new controller(token,path);
			flag1 = cc.userCheckRequest();
			if(!flag1) {
				System.out.println("Il Token fornito potrebbe essere scaduto o invalido...Riprovare");
			}else {
				flag2 = cc.listFolderRequest();
				if(flag2)	System.out.println("Utente certificato");
				else 	System.out.println("Il Path specificato non è valido");

			}
		}while((!flag1)||(!flag2));
		// User Check request ----> Se l'autenticazione non va a buon fine ripetere l'inserimento
		
		//PROVA RICHIESTA LIST FOLDER
		
		
		
	}

}
