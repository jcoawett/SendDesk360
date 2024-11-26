package sendDesk360.viewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
	
	public void addAccessGroup(String groupName, Role role) {
	    try {
	        userManager.createAccessTag(groupName, role.getName());
	    } catch (SQLException e) {
	        System.err.println("Error creating access tag: " + e.getMessage());
	    }
	}
	
	public void removeAccessGroup() {
		
	}
	
	public List<String> getAllAccessGroups() {
		if(isAdmin() || isInstructor(currentUser)) {
			try {
				List<String> accessGroups = userManager.getAllAccessTags();
				return accessGroups; 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null; 
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
	
	public boolean isUserInAccessGroup(User user, String group, Vector<Role> roles) {
		try {
			for (Role r : roles) {
				if (userManager.userHasAccessTag(user.getUserID(), group, r)){
					return true; 
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false; 
	}
	
	public void addUserToAccessGroup(User user, String group) {
			try {
				if (isUserInAccessGroup(user,group,user.getRoles())) {
					System.out.println("User determined to already be in access group");
					return; 
				}
				else for (Role r : user.getRoles()) {
					if (userManager.getAccessTagsForRole(r).contains(group)) {
						userManager.addAccessTagForUser(group, user, r);
						System.out.println("Added access tag " + group + " for user in role: " + r.getName());
						isFirstInstructorToAccessGroup(group, user, r); //check to see if this is the first instructor 
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public List<User> getAllUsers(){
		try {
			return userManager.getAllUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null; 
	}
	
	public void removeUserFromAccessGroup(User user, String group) {
		
		try {
			for (Role r : userManager.getUserByUsername(user.getUsername()).getRoles()) {
				if (userManager.userHasAccessTag(user.getUserID(), group, r)
					&& userManager.getAccessTagsForRole(r).contains(group)) 
				{
					userManager.removeAccessTagForUser(group, user, r);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isFirstInstructorToAccessGroup(String group, User user, Role r){
		System.out.println("Checking if " +user.getName().getFirst() + "is the first instructor in acccess group " + group + " with role " + r.getName()); 
		if (r.getName().equalsIgnoreCase("instructor")) {
			try {
				List<User> usersWithRole = userManager.getUsersWithAccessTag(group);
				System.out.println("there is " + usersWithRole.size() + " user in group: " + group);
				for (User tempUser : usersWithRole) {
					if (isInstructor(tempUser)) {
						System.out.println("Comparing " + tempUser.getUsername() + " with " + user.getUsername()); 
						if (tempUser.getUsername().equalsIgnoreCase(user.getUsername())) {
							System.out.println("This instructor was the first instructor in access group: " + group); 
							userManager.addAccessTagForUser("\"view-article-bodies\"", user, r);
							userManager.addAccessTagForUser("\"admin-rights\"", user, r);
						}
					}
					else {
						System.out.println("This user was found to not be an instructor"); 
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return false; 
	}
	
	
	
	public boolean isFirstInstructorToAccessGroup(String group, User user){ 
		for (Role r: user.getRoles()) {
			if (r.getName().equalsIgnoreCase("instructor"))
			{
				return isFirstInstructorToAccessGroup(group, user, r);
			}
		
		}
		return false; 
	}
	
	public boolean isInstructor(User user) {
        return user.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("instructor"));
	}
	
	public boolean isAdmin() {
        User currentUser = userManager.getCurrentUser();
        return currentUser.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }
}