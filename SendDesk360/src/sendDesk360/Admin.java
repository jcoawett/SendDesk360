package sendDesk360;

import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;

import sendDesk360.User.Role;

public class Admin extends User {
	Instructor teacher = new Instructor(); 
	
	static Vector<User> allUsers = new Vector<User>(); 
	
	/**
	 * This method takes an email and role to assign in order to invite a user to the class  
	 *
	 * @param String email: The email to send the one time passcode to.
	 * @param Role userRole: The role to specify the new user will be given, once they sign up using the one time passcode. 
	 * @return SimpleEntry<Boolean, Integer> : a pair (similar to a tuple) with a boolean indicating the success of the invite, and the Integer representing the one time passcode for the user
	 */
	private static SimpleEntry<Boolean, Integer> inviteToClass(String email, Role userRole) {
		SimpleEntry<Boolean, Integer> result = new SimpleEntry<>(true, 000000); 
		return result;
	}
	
	/**
	 * A one-time password and an expiration date and time is set.  The next time the user tries to log in, they must use the one-time password, and the only 
	 * action possible is to set up a new password.  Before being given access to set up a new password, the system checks to see if the date and time are 
	 * proper given the deadline.  Once the new password has been set, the user is directed back to the login page. Logging in with the one-time password 
	 * resets the flag so it can't be used again.
	 *
	 * @param User user: The user account that will be reset
	 * @return boolean: A boolean indicating the success of the User account reset. 
	 */
	private static boolean resetUserAccount(User user) {
		return true; 
	}
	
	/**
	 * Delete a user account.  An "Are you sure?" message must be answered with "Yes" to do the delete. After the "Yes" this method will be called. 
	 * This method will set all of the user parameters to null, and relinquish the memory consumed by the user, removing it from the class list.  
	 *
	 * @param User user: the user account to be deleted. 
	 * @return boolean : indicated the success of the user delete. 
	 */
	private static boolean deleteUserAccount(User user) {
		return true; 
	}
	
	/**
	 * List the user accounts. A list of all the user accounts with the user name, the individual's name, and a set of codes for the roles is displayed. 
	 * This method will be responsible for calling the toString method of each user to get all of the info from a class. It will then take the result of each toString, 
	 * and add it to a Vector of Strings that the UI can access. 
	 *
	 * @return SimpleEntry<Boolean, Vector<String>> : a pair (similar to a tuple) with a boolean indicating the success of the list, and a vector of strings with 
	 * the information of the users listed. 
	 * 
	 */
	private static SimpleEntry<Boolean, Vector<String>> listUsers(){
		
		//TODO: create toString method for the User class and iterated through the full list of Users, adding them to the vector 
		Vector<String> userInfo = new Vector<String>(); 
		userInfo.add("Lilli S, email@email.com, role"); 
		userInfo.add("Alex P. email@email.com, role"); 
		
		SimpleEntry<Boolean, Vector<String>> result = new SimpleEntry<>(true, userInfo); 
		return result; 
	}
	
	/**
	 * This method evaluated the validity of a user entered password by evaluating every character until all of the requirements are met. 
	 * This works by setting all requirements flags = false, and manually setting them to true one-by-one once a character fits a required category. 
	 * If all requirements flags are true once the last character is evaluated, then this method returns an empty error string. 
	 *
	 * @param input The String input received by the user, representing a password to be tested 
	 */
	public static boolean addOrRemoveRole(User user, Role role, boolean addOrRemove){
		if (addOrRemove) 
		{
			user.roles.add(role); 
		}
		else
		{
			user.roles.removeElement(role);
		}
		return true; 
	}
	
	//TODO: implement this method for Admin class.
	
	@Override
	public String toString() {
		return "Username, name, roles,"; 
	}
}
