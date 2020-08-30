package Controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class request {
	private String url_r = "";
	private String body_r = "";
	private String token_r = "";
	private String type_r = "";
	
	public request(String url,String body,String token,String type) {
		url_r = url;
		body_r = body;
		token_r = token;
		type_r = type;
		
	}

	public String getUrl_r() {
		return url_r;
	}

	public void setUrl_r(String url_r) {
		this.url_r = url_r;
	}

	public String getBody_r() {
		return body_r;
	}

	public void setBody_r(String body_r) {
		this.body_r = body_r;
	}

	public String getToken_r() {
		return token_r;
	}

	public void setToken_r(String token_r) {
		this.token_r = token_r;
	}

	public String getType_r() {
		return type_r;
	}

	public void setType_r(String type_r) {
		this.type_r = type_r;
	}
	
	
}
