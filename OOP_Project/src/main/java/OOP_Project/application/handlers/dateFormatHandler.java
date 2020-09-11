package OOP_Project.application.handlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import OOP_Project.application.models.jsonError;


/**
 * 
 *
 * @author Alessandro Bedetta
 * 
 * <p>
 * This class has been implemented to resolve all the problems regarding the dates.
 * The methods in this class are all finalized to check,modify and adjust the dates' format.
 * </p>
 * 
 *
 */
public class dateFormatHandler {
	/**
	 * String representing the wanted date format.
	 */
	private static String format;
	/**
	 * The class constructor sets the default date format ("yyyy-MM-dd HH:mm:ss").
	 */
	public dateFormatHandler() {
		format = "yyyy-MM-dd HH:mm:ss";
	}
	/**
	 * This method allows the toString method to know which is the correct format to use.
	 * @param milliSecs Represents the time lapse measured in milliseconds
	 * @return A string representing the matching format for the specified time.
	 */
	public static String getFormat(long milliSecs) {	
		if(milliSecs==0) format = "";
		if(milliSecs>=1000) format = "ss";	//If the time in milliseconds is > of 1000 we are talking about seconds
		if(milliSecs>=60000) format = "mm";	//If the time in milliseconds is > of 60000 we are talking about seconds
		if(milliSecs>=3600000) format = "hh";	//If the time in milliseconds is > of 3600000 we are talking about seconds
		if(milliSecs>=86400000) format = "D";	//If the time in milliseconds is > of 86400000 we are talking about seconds
		if(milliSecs>=Long.parseLong("2629800000")) format = "M";	//If the time in milliseconds is > of 2629800000 we are talking about seconds
		if(milliSecs>=Long.parseLong("31557600000")) format = "Y";	//If the time in milliseconds is > of 31557600000 we are talking about seconds
		return format;
	}
	/**
	 * This method returns an easy-to-read string representation of a time measure.
	 * @param millis Represents the time lapse measured in milliseconds
	 * @return The String representing the time with the correct format
	 */
	public static String toString(long millis) {
		switch(dateFormatHandler.getFormat(millis)) {  // This switch case allows to compose a string matching the correct format
			case "":
				return "0 ss";
				
			case "ss":
				return (millis/1000)+"ss";
				
			case "mm":
				return (millis/60000)+"mm"
						+ ((millis%60000)/1000)+"ss";
				
			case "hh": 
				return (int)(millis/3600000)+"hh"+(int)((millis%3600000)/60000)+"mm"+ (int)((((millis%3600000)%60000))/1000)+"ss";
				
			case "D":
				return (int)(millis/86400000)+"DD"+(int)((millis%86400000)/3600000)+"hh" + (int)(((millis%86400000)%3600000)/60000)+"mm"+(int)((((millis%86400000)%3600000)%60000)/1000)+"ss ";                    
				
			case "M":
				return (int)(millis/Long.parseLong("2629800000"))+"MM "+(int)((millis%Long.parseLong("2629800000"))/86400000)+"DD "+(int)(((millis%Long.parseLong("2629800000"))%86400000)/3600000)+"hh "+(int)((((millis%Long.parseLong("2629800000"))%86400000)%3600000)/60000)+"mm "+(int)(((((millis%Long.parseLong("2629800000"))%86400000)%3600000)%60000)/1000)+"ss";
			
		}
		return new jsonError("An error occurred during the date parsing",500,"DateParsingError").toString();
	}
	
	public static void setFormat(String format) {
		dateFormatHandler.format = format;
	}
	/**
	 * This method allows to check the validity of a date .
	 * It also checks if the date is written in the correct form, if it's not, the method corrects it.
	 * @param dateString The String representing the date to check.
	 * @return A String containing the validated date.
	 */
	public static String checkFormat(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date test;
		
		try {
			test = sdf.parse(dateString); 		//if the parse of the date thrown an exception it means that it's not been written correctly
		} catch (ParseException e) {
			return "";							//if the date is invalid a blank String is returned
		}
		
		String[] dateParse = dateString.split("-");// these 3 controls allow to check if our date is 100% matching the date format on the listReviews response JSON
		if((Integer.parseInt(dateParse[2])>31)||(Integer.parseInt(dateParse[1])>12)||(Integer.parseInt(dateParse[2])<1)||(Integer.parseInt(dateParse[1])<1)||(Integer.parseInt(dateParse[0])<1970)) return "";
		
		if((Integer.parseInt(dateParse[1])<10)&&(!dateParse[2].contains("0"))){  // changes 2020/9/08 in 2020/09/08
			dateParse[1] = "0"+dateParse[1];
		}
		if((Integer.parseInt(dateParse[2])<10)&&(!dateParse[2].contains("0"))){	 // changes 2020/09/8 in 2020/09/08
			dateParse[2] = "0"+dateParse[2];
		}
		if(test.after(new Date(System.currentTimeMillis()))) return "";
		return dateParse[0]+"-"+dateParse[1]+"-"+dateParse[2]; 					//The corrected string is reassembled and returned
		
	}
	
}

