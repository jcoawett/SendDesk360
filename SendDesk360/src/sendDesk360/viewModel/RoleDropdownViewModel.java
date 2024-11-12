package sendDesk360.viewModel;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sendDesk360.SendDesk360;
import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.model.database.UserManager;

public class RoleDropdownViewModel {

    private final SendDesk360 mainApp;
    private final User user; 
    private final UserManager userManager; 
    private final StringProperty selectedRole = new SimpleStringProperty();
    
 // Observable list for roles
    private Vector<Role> roles;

    public RoleDropdownViewModel(SendDesk360 mainApp, User user, UserManager userManager) {
    	this.mainApp = mainApp;
        this.userManager = userManager;
        this.user = userManager.getCurrentUser();
        
        
        roles = userManager.getCurrentUser().getRoles(); 
    }

    public StringProperty selectedRoleProperty() {
        return selectedRole;
    }

    public String getSelectedRole() {
        return selectedRole.get();
    }

    public void setSelectedRole(String role) {
        selectedRole.set(role);
    }

    public void handleLogin(String selectedRole) {
    	
    	//check if the user has this role
    	boolean proceed = hasRole(selectedRole); 
    	System.out.println(user.getUsername());
    	System.out.print(roles.toString());
    	if (selectedRole != null && proceed) {
            switch (selectedRole) {
                case "Admin":
                    mainApp.showDashboard();
                    break;
                case "Instructor":
                    mainApp.showDashboard();
                    break;
                case "User":
                    mainApp.showDashboard();
                    break;
                default:
                    System.out.println("Please select a valid role.");
            }
        } else if (proceed){
            System.out.println("No role selected. Please choose a role to log in.");
        }
        else {
        	System.out.println("You do not have this role. Please contact your administrator if you beleive this to be an error.");
        }
    }
    
    public void proceedToDashboard() {
    	
    }
    
    public boolean processSelection() {
    	return false; 
    }
    
    public boolean hasRole(String selected) {
    	//check the roles that the user ha
    	for (Role r: roles) {
    		if (r.getName().equalsIgnoreCase(selected)) {
    			return true; 
    		}
    	}
    	return false; 
    }
}
