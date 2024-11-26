package sendDesk360.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sendDesk360.model.User;
import sendDesk360.model.User.FullName;
import sendDesk360.model.database.UserManager;

public class ProfileViewModel {

    private final UserManager userManager; // Handles database operations
    private final User currentUser; // Currently logged-in user

    // Properties for data binding with the UI
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty preferredName = new SimpleStringProperty();

    public ProfileViewModel(UserManager userManager, User currentUser) {
        this.userManager = userManager;
        this.currentUser = currentUser;

        // Load current user details into properties
        loadProfile();
    }

    /**
     * Loads the current user's profile details into the ViewModel properties.
     */
    public void loadProfile() {
        FullName name = currentUser.getName();
        firstName.set(name.getFirst());
        lastName.set(name.getLast());
        preferredName.set(name.getPref());
        email.set(currentUser.getEmail());
    }

    /**
     * Saves the updated profile details back to the database.
     * @throws Exception 
     */
    public void saveProfile() throws Exception {
        // Update the FullName object
        FullName updatedName = currentUser.getName();
        updatedName.setFirst(firstName.get());
        updatedName.setLast(lastName.get());
        updatedName.setPref(preferredName.get());

        // Update the email directly on the currentUser
        currentUser.setEmail(email.get());

        // Save the updated user to the database
        userManager.updateUser(currentUser);
    }

    // Property accessors for data binding
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty preferredNameProperty() {
        return preferredName;
    }
}