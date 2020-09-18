package OOP_Project.application.models;

import java.util.ArrayList;
/**
 * 
 * This class allows to store the information coming from the 'getDailyRev' and 'getWeeklyRevs' requests
 * @see OOP_Project.application.controller.memory
 * OOP_Project.application.controller.memory.getWeeklyRevs
 * @author Utente
 *
 */
public class dailyRevsResponse {
	/**
	 * This counter allows to see immediately how many requests have been made in that time period
	 */
	private int counter;
	/**
	 * This is the collection containing all the reviews made in that time period
	 */
	private ArrayList<review> revs;	//
	public dailyRevsResponse(ArrayList<review> revs) {
		super();
		this.revs = revs;
		this.counter = revs.size();
	}
	/**
	 * 
	 * @return counter
	 */
	public int getCounter() {
		return counter;
	}
	/**
	 * 
	 * @param counter
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}
	/**
	 * 
	 * @return revs
	 */
	public ArrayList<review> getRevs() {
		return revs;
	}
	/**
	 * 
	 * @param revs
	 */
	public void setRevs(ArrayList<review> revs) {
		this.revs = revs;
	}
	
}
