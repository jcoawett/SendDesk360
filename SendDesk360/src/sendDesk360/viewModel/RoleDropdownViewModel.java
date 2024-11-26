package sendDesk360.viewModel;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Role> roles;

    private ObservableList<String> roleNames = FXCollections.observableArrayList();

    public RoleDropdownViewModel(SendDesk360 mainApp, UserManager userManager) {
        this.mainApp = mainApp;
        this.userManager = userManager;
        this.user = userManager.getCurrentUser();

        try {
            roles = userManager.getRolesForUser(user.getUserID());
            roleNames.addAll(roles.stream().map(Role::getName).collect(Collectors.toList()));
            System.out.println("Number of roles fetched: " + roles.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getRoleNames() {
        return roleNames;
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


    public boolean hasRole(String selected) {
        for (Role r : roles) {
            System.out.println("Checking role: " + r.getName() + " against selected: " + selected);
            if (r.getName().equalsIgnoreCase(selected)) {
                return true;
            }
        }
        System.out.println("Role not found: " + selected);
        return false;
    }
    
    public void handleLogin(String selectedRole) {
        System.out.println("Looking for selected role: " + selectedRole);

        if (hasRole(selectedRole)) {
            try {
                mainApp.showDashboard();
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown role. Please select a valid role.");
            }
        } else {
            System.out.println("You do not have this role. Please contact your administrator.");
        }
    }
}