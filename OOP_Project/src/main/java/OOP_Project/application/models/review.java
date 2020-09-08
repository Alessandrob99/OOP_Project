package OOP_Project.application.models;

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

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath_lower() {
		return path_lower;
	}

	public void setPath_lower(String path_lower) {
		this.path_lower = path_lower;
	}

	public String getPath_display() {
		return path_display;
	}

	public void setPath_display(String path_display) {
		this.path_display = path_display;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getClient_modified() {
		return client_modified;
	}



	public void setClient_modified(String client_modified) {
		this.client_modified = client_modified;
	}



	public String getServer_modified() {
		return server_modified;
	}



	public void setServer_modified(String server_modified) {
		this.server_modified = server_modified;
	}



	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public String getContent_hash() {
		return content_hash;
	}

	public void setContent_hash(String content_hash) {
		this.content_hash = content_hash;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isIs_downloadable() {
		return is_downloadable;
	}

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
