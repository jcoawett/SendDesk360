package sendDesk360;

import java.util.Vector;

import sendDesk360.User.Role;

public class User {
	
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
	

}
