package OOP_Project.application.handlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;



public class dateFormatHandler {
	private static String format;
	public dateFormatHandler() {
		format = "yyyy-MM-dd HH:mm:ss";
	}
	
	public static String getFormat(long milliSecs) {
		if(milliSecs==0) format = "";
		if(milliSecs>=1000) format = "ss";
		if(milliSecs>=60000) format = "mm";	
		if(milliSecs>=3600000) format = "hh";	
		if(milliSecs>=86400000) format = "D";	
		if(milliSecs>=Long.parseLong("2629800000")) format = "M";	
		if(milliSecs>=Long.parseLong("31557600000")) format = "Y";
		return format;
	}
	
	public static String toString(long millis) {
		switch(dateFormatHandler.getFormat(millis)) {
			case "":
				return "0 ss";
				
			case "ss":
				return (millis/1000)+"ss";
				
			case "mm":
				return (millis/60000)+"mm "
						+ ((millis%60000)/1000)+"ss ";
				
			case "hh": 
				return (int)(millis/3600000)+"hh "+(int)((millis%3600000)/60000)+"mm "+ (int)((((millis%3600000)%60000))/1000)+"ss";
				
			case "D":
				return (int)(millis/86400000)+"DD "+(int)((millis%86400000)/3600000)+"hh " + (int)(((millis%86400000)%3600000)/60000)+"mm "+(int)((((millis%86400000)%3600000)%60000)/1000)+"ss ";                    
				
			case "M":
				return (int)(millis/Long.parseLong("2629800000"))+"MM "+(int)((millis%Long.parseLong("2629800000"))/86400000)+"DD "+(int)(((millis%Long.parseLong("2629800000"))%86400000)/3600000)+"hh "+(int)((((millis%Long.parseLong("2629800000"))%86400000)%3600000)/60000)+"mm "+(int)(((((millis%Long.parseLong("2629800000"))%86400000)%3600000)%60000)/1000)+"ss";
			
		}
		return "Errore durante la conversione di una data";
	}
	
	public static void setFormat(String format) {
		dateFormatHandler.format = format;
	}
	
	public static String checkFormat(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date test;
		
		try {
			test = sdf.parse(dateString);
		} catch (ParseException e) {
			return "";
		}
		
		String[] dateParse = dateString.split("-");
		if((Integer.parseInt(dateParse[2])>31)||(Integer.parseInt(dateParse[1])>12)||(Integer.parseInt(dateParse[2])<1)||(Integer.parseInt(dateParse[1])<1)||(Integer.parseInt(dateParse[0])<1970)) return "";
		
		if((Integer.parseInt(dateParse[1])<10)&&(!dateParse[2].contains("0"))){
			dateParse[1] = "0"+dateParse[1];
		}
		if((Integer.parseInt(dateParse[2])<10)&&(!dateParse[2].contains("0"))){
			dateParse[2] = "0"+dateParse[2];
		}
		if(test.after(new Date(System.currentTimeMillis()))) return "";
		return dateParse[0]+"-"+dateParse[1]+"-"+dateParse[2];
		
	}
	
}

