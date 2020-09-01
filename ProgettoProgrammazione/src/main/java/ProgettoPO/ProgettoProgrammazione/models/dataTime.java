package ProgettoPO.ProgettoProgrammazione.models;

public class dataTime {
	private int year;
	private int month;
	private int day;
	 private int hour;
	 private int minute;
	 private int seconds;
	 
	 
	 
	 public dataTime() {
		super();
		this.year = 0;
		this.month = 0;
		this.day = 0;
		this.hour = 0;
		this.minute = 0;
		this.seconds = 0;
	}

	public void setDataTime(String jsonin) {
		 String[] DateAndTime = jsonin.split("T");
		 String[] Date = DateAndTime[0].split("-");
		 String[] Time = DateAndTime[1].split(":");
		 Time[2].substring(0,1);
		 this.year = Integer.parseInt(Date[0]);
		 this.month = Integer.parseInt(Date[1]);
		 this.day = Integer.parseInt(Date[2]);
		 this.hour = Integer.parseInt(Time[0]);
		 this.minute = Integer.parseInt(Time[1]);
		 this.seconds = Integer.parseInt(Time[2]);
		 
	 }
	 
	 public String toString() {
		 return year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+seconds;
	 }

}
