package sendDesk360.model;

import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;
import java.security.SecureRandom;

public class Admin extends User {
	Instructor teacher = new Instructor(); 
	
	Classroom classroom = new Classroom();  
	
	/**
	 * This method takes an email and role to assign in order to invite a user to the class  
	 *
	 * @param String email: The email to send the one time passcode to.
	 * @param Role userRole: The role to specify the new user will be given, once they sign up using the one time passcode. 
	 * @return SimpleEntry<Integer, Role> : a pair (similar to a tuple) with  Integer representing the one time passcode invitation for the user and the Role of that new User
	 */
	private static SimpleEntry<Integer, Role> inviteToClass(String email, Role userRole) {
		
		SecureRandom securePassGen = new SecureRandom(); 
		int pass = 100000 + securePassGen.nextInt(900000); 
		SimpleEntry<Integer, Role> result = new SimpleEntry<>(pass, userRole); 
		System.out.println("Send invite code to " + email); 
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
		
		//characters to choose from in the one time password 
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
		
		
		SecureRandom securePassGen = new SecureRandom(); //use secure encrypted java library to generate integers
		StringBuilder stringGen = new StringBuilder(); 
		
		
		//new password will be 16 characters long, composed of random characters from the CHARACTERS string. 
		for (int i = 0; i < 16; i ++)
		{
			int indexOfNextChar = securePassGen.nextInt(CHARACTERS.length()); 
			stringGen.append(CHARACTERS.charAt(indexOfNextChar)); 
			
		}
		
		user.setPassword(stringGen.toString()); //reset the user password to the one time password 
		user.setFlag(true); //change the users flag to denote that this password is a one time password 
		long expireTime = System.currentTimeMillis() + (60000*60); //set the expiration time to be one hour from now
		user.setExpireTime(expireTime);//reset the users expire time as well. 
		
		
		System.out.println("Successfully generated new pasword for User " + user.getUsername()); 
		return true; 
	}
	
	/**
	 * Delete a user account.  An "Are you sure?" message must be answered with "Yes" to do the delete. After the "Yes" this method will be called. 
	 * This method will set all of the user parameters to null, and relinquish the memory consumed by the user, removing it from the class list.  
	 *
	 * @param User user: the user account to be deleted. 
	 * @return boolean : indicated the success of the user delete. 
	 */
	private boolean deleteUserAccount(User user) {
		user.setEmail(null);
		user.setExpireTime(0);
		user.setFlag(false);
		user.setPassword(null);
		user.setRoles(null);
		user.setName(null);
		user.setUsername(null);
		
		classroom.removeFromClassroom(user); 
		
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
	private SimpleEntry<Boolean, String> listUsers(){
		
		classroom.newFullClassroom(); //create the populated class 
		
		Vector<User> currentUsers = classroom.getClassroom(); 
		
		StringBuilder stringGen = new StringBuilder(); 
		for (User u : currentUsers)
		{
			stringGen.append(u.toString()); 
		}
		
		SimpleEntry<Boolean, String> result = new SimpleEntry<>(true, stringGen.toString()); 
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
			user.getRoles().add(role);
		}
		else
		{
			user.getRoles().remove(role);
		}
		return true; 
	}
}
