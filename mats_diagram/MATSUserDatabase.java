package mats_model;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * 
 * @author elee, boconno3, rparames, asaini 
 * 
 * The database containing all of the users of the particulat MATS system. 
 *
 */
public class MATSUserDatabase implements Serializable {
	
	/**
	 * List of all the users on the database.
	 */
	private List<MATSUser> userDatabase;
	
	
	/**
	 * @author boconno3, elee3, rparames, asaini
	 * 
	 * Initializes an empty ArrayList to store users of the application model
	 *
	 */
	public MATSUserDatabase() {
		userDatabase = new ArrayList<MATSUser>();
		
	}
	
	/**
	 * 
	 * @param index to user in database
	 * @return The located user 
	 */
	public MATSUser get(int index) {
		return userDatabase.get(index);
	}
	
	/**
	 * 
	 * @return Size of user database 
	 */
	public int size() {
		return userDatabase.size();
	}
	
	/**
	 * @author boconno3, elee3, asaini, rparames
	 * @param User's username 
	 * @param User's password
	 * 
	 * Adds a new user to the database, initilizing the user's username, password and
	 * application model. 
	 */
	public boolean addUser(String name, String password) {
		
		if (findUser(name)==null) { 
			MATSUser newUser = new MATSUser(name, password);
			userDatabase.add(newUser);
			return true;
			
		}
		return false;
		
		
	}
	
	/**
	 * @author rparames, boconno3, elee3, asaini
	 * @param name to search for 
	 * @return User with username 
	 * 
	 * Searhes user database to check if specifed user exists in the database.
	 */
	public MATSUser findUser(String name) {
		
		for (MATSUser user : userDatabase) {		
			if (user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	/**
	 * @author rparames, boconno3, elee3, asaini
	 * @param User's username
	 * @param User's password
	 * @return Reference to user 
	 * 
	 * Verifiees if a user exists in the database. If they do, returns a reference to the user. If
	 * not, returns null
	 */
	public MATSUser verifyUser(String user, String password) {
		
		MATSUser current = findUser(user);
		
		
		if (current!=null) {
			
			if (current.getPassword().equals(password)) {
				
				return current;	
			}
		}
	
		return null;
	}
	
	/**
	 * @author rparames, boconno3, elee3, asaini
	 * @return The names of the users in the databse in a string\
	 */
	public String findAll() {
		
		String users = "The users of this database are: ";
		for (MATSUser user: userDatabase) {
			users+= (user.getName())+ "\n";
		}
		return users;
	}
	
	/**
	 * @author asaini, boconno3, elee3, rparames
	 * 
	 * @return An array list containng the names of the users 
	 */
	public List<String> getAllUsers() {
		
		List<String> allUsers = new ArrayList<String>();
		for (MATSUser user: userDatabase) {
			allUsers.add( user.getName());
		}
		return allUsers;
	}
	
	/**
	 * @author asaini, boconno3
	 * 
	 * @return an XML String representation of the object 
	 */
	public String toXML() {
		
		String messageXML = "\r\n<MATSUserDatabase>";
		messageXML += "\r\n<userDatabase>";

		for ( int i = 0; i < userDatabase.size(); i++ ) {
			messageXML += userDatabase.get(i).toXML();
		}
		messageXML += "\r\n</userDatabase>";
		messageXML += "\r\n</MATSUserDatabase>";
		return messageXML;
	}

    /**
     * @link aggregation 
     */
    private MATSUser lnkMATSUser;
}