package sendDesk360.viewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sendDesk360.SendDesk360;
import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.model.database.UserManager;

public class AccessGroupViewModel{
	private final UserManager userManager; 
	private final User currentUser; 
	
	public AccessGroupViewModel(UserManager userManager, User currentUser) {
		this.userManager = userManager;
		this.currentUser = currentUser; 
	}
	
	public void addAccessGroup() {
		
	}
	
	public void removeAccessGroup() {
		
	}
	
	public List<String> getAllAccessGroups() {
		List<String> tempGroups = new ArrayList<String>(); 
		
		tempGroups.add("Operating Systems");
		tempGroups.add("Intro to Programming"); 
		
		return tempGroups;
	} 
	
	public List<User> getAllUsersInAccessGroup(String accessGroup) {
		try {
			return userManager.getUsersWithAccessTag(accessGroup);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null; 
	}
	
	public void isUserInAccessGroup() {
		
	}
	
	public void addUserToAccessGroup() {
		
	}
	
	public void removeUserFromAccessGroup() {
		
	}
	
	public void listUsersWithAdminRights() {
		
	}
	
	public void listUsersWithViewingRights() {
		
	}
	
	public boolean isAdmin() {
        User currentUser = userManager.getCurrentUser();
        return currentUser.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }
}