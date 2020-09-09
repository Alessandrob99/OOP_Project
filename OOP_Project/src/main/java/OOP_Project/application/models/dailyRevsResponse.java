package OOP_Project.application.models;

import java.util.ArrayList;

public class dailyRevsResponse {
	private int counter;
	private ArrayList<review> revs;
	public dailyRevsResponse(ArrayList<review> revs) {
		super();
		this.revs = revs;
		this.counter = revs.size();
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public ArrayList<review> getRevs() {
		return revs;
	}
	public void setRevs(ArrayList<review> revs) {
		this.revs = revs;
	}
	
}
