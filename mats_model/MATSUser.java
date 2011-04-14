package mats_model;

import java.io.Serializable;

/**
 * @author boconno3, elee, rparames, asaini
 *
 * Defines user objects 
 */
public class MATSUser implements Serializable {
	
	/**
	 * The user's username 
	 */
	private String name;
	
	/**
	 * The user's password
	 */
	private String password;

	/**
	 * @author boconno3, rparames, asaini
	 * @param User's username
	 * @param User's password
	 * 
	 * Instantiates the username, password and the application model.
	 */
	public MATSUser(String name, String password) {

		this.name = name;
		this.password = password;
	}
	
	/**
	 * @author boconno3
	 * @return The password of the user so it can be verified by the user database
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @author rparames, asaini
	 * @return The username of the user to display in database.
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * @author elee3
	 * @return the name of the user as a String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @author asaini, boconno3
	 * @return A string representation of the MATSUser object 
	 */
	public String toXML() {
		
		String messageXML = "\r\n<MATSUser>";
		messageXML += "\r\n<name>" + name + "</name>";
		messageXML += "\r\n<password>" + password + "</password>";
		messageXML += "\r\n</MATSUser>";
		return messageXML;
		
	}
	
}
