package OOP_Project.application.models;

import java.util.Date;

/**
 * 	
 * @author Bedetta Alessandro
 * 
 * <p>
 * This class is used to store the information contained in a single review
 * It has all the getters and setters methods and also an overridden toString method.
 * </p>
 *
 */
public class review {
	private String name;
	private String path_lower;
	private String path_display;
	private String id;
	private String client_modified;
	private String server_modified;
	private String rev;
	private String content_hash;
	private long size;
	private boolean is_downloadable;
	
	public review() {
		this.name = "";
		this.path_lower = "";
		this.path_display = "";
		this.id = "";
		this.client_modified = null;
		this.server_modified = null;
		this.rev = "";
		this.content_hash = "";
		this.size = 0;
		this.is_downloadable = false;
	}

	
	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return path_lower
	 */
	public String getPath_lower() {
		return path_lower;
	}
	/**
	 * 
	 * @param path_lower
	 */
	public void setPath_lower(String path_lower) {
		this.path_lower = path_lower;
	}
	/**
	 * 
	 * @return path_display
	 */
	public String getPath_display() {
		return path_display;
	}
	/**
	 * 
	 * @param path_display
	 */
	public void setPath_display(String path_display) {
		this.path_display = path_display;
	}
	/**
	 *  
	 * @return id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return client_modified
	 */

	public String getClient_modified() {
		return client_modified;
	}
	/**
	 * 
	 * @param client_modified
	 */

	public void setClient_modified(String client_modified) {
		this.client_modified = client_modified;
	}
	/**
	 * 
	 * @return server_modified
	 */
	public String getServer_modified() {
		return server_modified;
	}
	/**
	 * 
	 * @param server_modified
	 */
	public void setServer_modified(String server_modified) {
		this.server_modified = server_modified;
	}
	/**
	 * 
	 * @return rev
	 */
	public String getRev() {
		return rev;
	}
	/**
	 * 
	 * @param rev
	 */
	public void setRev(String rev) {
		this.rev = rev;
	}
	/**
	 * 
	 * @return content_hash
	 */
	public String getContent_hash() {
		return content_hash;
	}
	/**
	 * 
	 * @param content_hash
	 */
	public void setContent_hash(String content_hash) {
		this.content_hash = content_hash;
	}
	/**
	 *
	 * @return size
	 */
	public long getSize() {
		return size;
	}
	/**
	 * 
	 * @param size
	 */
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * 
	 * @return is_downloadable
	 */
	public boolean isIs_downloadable() {
		return is_downloadable;
	}	
	/**
	 * 
	 * @param is_downloadable
	 */
	public void setIs_downloadable(boolean is_downloadable) {
		this.is_downloadable = is_downloadable;
	}
	/**
	 * This is the overridden toString method that returns the String representation of the 
	 * JSON Object containing the review instance.
	 * 
	 */
	public String toString() {
		return "{\r\n" + 
				"        \"path_display\": \""+this.getPath_display()+"\",\r\n" + 
				"        \"rev\": \""+this.getRev()+"\",\r\n" + 
				"        \"size\": "+this.getSize()+",\r\n" + 
				"        \"path_lower\": \""+this.getPath_lower()+"\",\r\n" + 
				"        \"server_modified\": \""+this.getServer_modified()+"\",\r\n" + 
				"        \"is_downloadable\": "+this.isIs_downloadable()+",\r\n" + 
				"        \"name\": \""+this.getName()+"\",\r\n" + 
				"        \"id\": \""+this.getId()+"\",\r\n" + 
				"        \"content_hash\": \""+this.getContent_hash()+"\",\r\n" + 
				"        \"client_modified\": \""+this.getClient_modified()+"\"\r\n" + 
				"    }";
	}
}
