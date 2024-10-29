package sendDesk360.model;
import java.util.Vector;

import sendDesk360.model.User.Role;

public class Classroom {
	private Vector<User> classroom = new Vector<User>();
	
	public Vector<User> getClassroom()
	{
		return classroom; 
	}
	
	public void addToClassroom(User user)
	{
		classroom.add(user); 
	}
	
	public void removeFromClassroom(User user)
	{
		classroom.remove(user); 
	}
	
	public void newFullClassroom()
	{
		//create dummy role 
		User.Role role = new User.Role(); 
		Vector<User.Role> roles = new Vector<User.Role>(); 
		roles.add(role); 
		
		//Crreate the users 
		User user1 = new User("Lilli-Elizabeth-Seebold-Liz", "lseebold", "lseebold@asu.edu", "*******", false, roles);
	    User user2 = new User("Bryce-Middlename-Jackson-Bry", "bryceJackson", "bryce@gmail.com", "*********", false, roles);
		User user3 = new User("Jason-H.-Coawette", "Jasoncoawette", "jason@gmail.com", "**********", false, roles); 
		User user4 = new User("Jonathon-H.-McGhee", "JohnathonMchee", "John@gmail.com", "***********", false, roles); 
		User user5 = new User("Alex-middlename-Palangian", "AlexP", "Alexp@gmail,com", "***********", false, roles); 
		
		//add them to the classroom
		classroom.add(user1);
		classroom.add(user2); 
		classroom.add(user3); 
		classroom.add(user4); 
		classroom.add(user5); 
		
				
	}
}
