package sendDesk360;

import java.util.Vector;

import sendDesk360.User.Role;

public abstract class User {
	
	public static class FullName
	{
		String first; 
		String middle; 
		String last; 
		String pref; 
	}
	
	/*Struct to represent Roles 
	 *Roles have a name, either student, admin, or instructor
	 *The privilege level will indicate a user's access to the system, and what type of user they are constructed as.*/
	public static class Role
	{
		String name; 
		int priveledge; 
	}
	
	FullName name = new FullName(); 
	Vector<Role> roles = new Vector<Role>();
	
	String username, email, password; 
	boolean flag; 
	long expireTime; 
	
	/* Default Constructor */
	public User()
	{
		this.username = ""; 
		this.email = "";
		this.password = "";
		this.flag = true;
		this.expireTime = System.currentTimeMillis() + (60000*5); 
	}
	
	/* Overloaded Constructor */ 
	public User(String nameID, String username, String email, String password, boolean flag, Vector<Role> roles) 
	{
		//split the nameID which should be in form "<first>-<middle>-<last>-<pref>"
		System.out.println(nameID + username); 
		String[] parts = nameID.split("-");
		
		name.first = parts[0];
		name.middle = parts[1]; 
		name.last = parts[2]; 
		name.pref = parts[3]; 
		
		//add all of the roles from this new Users list to it's list of roles 
		for (Role r : roles)
		{
			this.roles.add(r); 
			System.out.printf("Added role %s, with priveledge %i", r.name, r.priveledge); 
		}
		
		//set other variables 
		this.username = username; 
		this.email = email; 
		this.password = password; 
		this.flag = flag;  
		
		//set the expireTime to the current System time + 5 minutes (User gets 5 mins to enter their one time password. 
		this.expireTime = System.currentTimeMillis() + (60000*5); 
		
		System.out.println("New User Created/n"
				+ this.name.first + " " + this.name.middle + " " + this.name.last + " " + this.name.pref + "/n"
				+ "Email : " + this.email 
				+ "/nUsername: " + this.username);
		
		if (this.flag) {System.out.printf("This IS a onetime password and it will expire in %d minutes", this.expireTime/60000); }
	}
	
	/*Special Constructor for the first User in the System*/
	public User(String username, String email, Role r)
	{
		this.username = username; 
		this.email = email; 
		this.roles.add(r); 
	}
	
	/**
	 * This method returns a string representing the information about all Users in a class 
	 *
	 * @return: A String formated as <username> , <name> , <roles> 
	 */
	
	//TODO: implement the toString method in each subclass 
	public abstract String toString();
	
	

	/**
	 * This method evaluated the validity of a user entered password by evaluating every character until all of the requirements are met. 
	 * This works by setting all requirements flags = false, and manually setting them to true one-by-one once a character fits a required category. 
	 * If all requirements flags are true once the last character is evaluated, then this method returns an empty error string. 
	 *
	 * @param input The String input received by the user, representing a password to be tested 
	 */
	public String evaluatePassword(String input) {
		
		int passwordIndexofError = 0; //initialize the index in which the entered password broke on an error to be 0
		String inputLine = input; //get the input from the user
		int currentCharNdx = 0; //start at character 0
		
		//a password cannot be empty, so check if it has a length less than or equal to 0 before continuing. This avoids out-of-bounds errors.
		if(input.length() <= 0) return "*** Error *** The password is empty!";
		
		char currentChar = input.charAt(0);		// The current character from the above indexed position

		//Set all of the requirements flags to be false, as we have not found the requirements before checking any of the characters.
		String passwordInput = input; 
		boolean foundUpperCase = false;
		boolean foundLowerCase = false;	
		boolean foundNumericDigit = false;
		boolean foundSpecialChar = false;
		boolean foundLongEnough = false;
		boolean running = true;

		while (running) {
			//check for upercase letter
			if (currentChar >= 'A' && currentChar <= 'Z') {
				foundUpperCase = true; //set the flag for if we found an upercase letter to be true.
				
			//check for lowercase letter
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				foundLowerCase = true; //set the flag for if we found a lowercase letter to be true.
			
			//check for digit
			} else if (currentChar >= '0' && currentChar <= '9') {
				foundNumericDigit = true;
				
			//check for special character 
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				foundSpecialChar = true; //set the flag for if we found a special character to be true.
				
			//If the character did not fit into a category above, it is invalid
			} else {
				passwordIndexofError = currentCharNdx;
				return "*** Error *** An invalid character has been found!";
			}
			
			//Check if the password index is 7 or greater to determine if we have found at least 8 characters
			if (currentCharNdx >= 7) {
				System.out.println("At least 8 characters found"); 
				foundLongEnough = true;//set the flag for if the password is long enough to be true
			}
			currentCharNdx++; //go to the next character but first check ...
			if (currentCharNdx >= inputLine.length())
				running = false; //if we have reached an index that is greater or equal to the length, then there are no more characters. Stop running
			else
				currentChar = input.charAt(currentCharNdx); //however, if there are more characters, move on to the next character in the password and
															//re-run the while loop
			System.out.println();
		}
		
		//format the error message 
		String errMessage = "";
		if (!foundUpperCase) //if we did not find an uppercase letter, i.e foundUpperCase is false; 
			errMessage += "Upper case; ";
		
		if (!foundLowerCase) //if we did not find a lowercase letter, i.e foundLowerCase is false; 
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit) //if we did not find a digit, i.e foundNumericDigit is false; 
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar) //if we did not find a special character, i.e foundSpecialChar is false; 
			errMessage += "Special character; ";
			
		if (!foundLongEnough) //if the password was less than 8 characters, i.e foundLongEnough is false; 
			errMessage += "Long Enough; ";
		
		if (errMessage == "")//if the error message is still empty, all of the above conditions must have been satisfied, return no error message. 
			return "";
		
		passwordIndexofError = currentCharNdx;
		return errMessage + "conditions were not satisfied";

		}
}
	

