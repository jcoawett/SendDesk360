package sendDesk360.model;

import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;

import sendDesk360.model.User;

public class Admin extends User {
	Instructor teacher = new Instructor(); 
	
	static Vector<User> allUsers = new Vector<User>(); 
	
	private static boolean inviteToClass(String email, String oneTimePass) {
		return true; 
	}
	
	private static boolean resetUserAccount(User user) {
		return true; 
	}
	
	private static boolean deleteUserAccount(User user) {
		return true; 
	}
	
	private static SimpleEntry<Boolean, Vector<User>> listUsers(){
		SimpleEntry<Boolean, Vector<User>> result = new SimpleEntry<>(true, allUsers); 
		return result; 
	}
	
	public static boolean addOrRemoveRole(User user, Role role, boolean addOrRemove){
		if (addOrRemove) 
		{
//			user.getRole().add(role); 
		}
		else
		{
//			user.roles.removeElement(role);
		}
		return true; 
	}
}
