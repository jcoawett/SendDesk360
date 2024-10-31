package sendDesk360.viewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.*;
import sendDesk360.model.User;
import sendDesk360.model.User.Role;

public class UserViewModel {
    private final User user;

    // Properties for data binding in JavaFX
    private StringProperty username = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private BooleanProperty flag = new SimpleBooleanProperty();
    private LongProperty expireTime = new SimpleLongProperty();
    private ObservableList<Role> roles = FXCollections.observableArrayList();
    private User currentUser;


    // FullName properties
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty middleName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty preferredName = new SimpleStringProperty();

    public UserViewModel(User user) {
        this.user = user;
        
        // Initialize ViewModel properties with values from the User model
        username.set(user.getUsername());
        email.set(user.getEmail());
        password.set(user.getPassword());
        flag.set(user.isFlag());
        expireTime.set(user.getExpireTime());

        firstName.set(user.getName().getFirst());
        middleName.set(user.getName().getMiddle());
        lastName.set(user.getName().getLast());
        preferredName.set(user.getName().getPref());
        

        // Set up bidirectional binding with the User model (optional)
        setupBindings();
    }

    // Method to bind the ViewModel properties to the User model
    private void setupBindings() {
        username.addListener((observable, oldValue, newValue) -> user.setUsername(newValue));
        email.addListener((observable, oldValue, newValue) -> user.setEmail(newValue));
        password.addListener((observable, oldValue, newValue) -> user.setPassword(newValue));
        flag.addListener((observable, oldValue, newValue) -> user.setFlag(newValue));
        expireTime.addListener((observable, oldValue, newValue) -> user.setExpireTime(newValue.longValue()));

        firstName.addListener((observable, oldValue, newValue) -> user.getName().setFirst(newValue));
        middleName.addListener((observable, oldValue, newValue) -> user.getName().setMiddle(newValue));
        lastName.addListener((observable, oldValue, newValue) -> user.getName().setLast(newValue));
        preferredName.addListener((observable, oldValue, newValue) -> user.getName().setPref(newValue));
    }

    // GETTERS AND SETTERS
    // ---------------------------------------- //
    
    // FIRST NAME
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
        user.getName().setFirst(firstName); // Update the model
    }
    
    
    // MIDDLE NAME
    public StringProperty middleNameProperty() {
        return middleName;
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
        user.getName().setMiddle(middleName); // Update the model
    }
    
    
    // LASTNAME
    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
        user.getName().setLast(lastName); // Update the model
    }

    
    // PREFERRED NAME
    public StringProperty preferredNameProperty() {
        return preferredName;
    }

    public String getPreferredName() {
        return preferredName.get();
    }

    public void setPreferredName(String preferredName) {
        this.preferredName.set(preferredName);
        user.getName().setPref(preferredName); // Update the model
    }
    
    
    // ROLES
    public ObservableList<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        roles.add(role);
        user.getRoles().add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        user.getRoles().remove(role);
    }
    
    
    // USERNAME
    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
        user.setUsername(username); // Update the model
    }

    
    // EMAIL 
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
        user.setEmail(email); // Update the model
    }

    
    // PASSWORD
    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
        user.setPassword(password); // Update the model
    }
    
    
    // FLAG (for one time code)
    public BooleanProperty flagProperty() {
        return flag;
    }
    
	public User getCurrentUser() {
		
		return currentUser;
	}

    public boolean isFlag() {
        return flag.get();
    }

    public void setFlag(boolean flag) {
        this.flag.set(flag);
        user.setFlag(flag); // Update the model
    }

    
    // EXPIRE TIME
    public LongProperty expireTimeProperty() {
        return expireTime;
    }

    public long getExpireTime() {
        return expireTime.get();
    }

    public void setExpireTime(long expireTime) {
        this.expireTime.set(expireTime);
        user.setExpireTime(expireTime); // Update the model
    }
}