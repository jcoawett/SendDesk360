package sendDesk360.model;

import java.util.ArrayList;
import java.util.Vector;

public class User {

    public static class FullName {
        private String first;
        private String middle;
        private String last;
        private String pref;

        // Getters and Setters for FullName
        
        // FIRST
        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        
        // MIDDLE
        public String getMiddle() {
            return middle;
        }

        public void setMiddle(String middle) {
            this.middle = middle;
        }

        
        // LAST
        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        
        // PREFFERED
        public String getPref() {
            return pref;
        }

        public void setPref(String pref) {
            this.pref = pref;
        }
    }

    
    
    public static class Role {
        private String name;
        private int privilege;
        private ArrayList<String> tags;
        
        @Override
        public String toString() {
            return name;
        }

        // Getters and Setters for Role
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrivilege() {
            return privilege;
        }

        public void setPrivilege(int privilege) {
            this.privilege = privilege;
        }
        
        public ArrayList<String> getTags(){
        	return this.tags; 
        }
        
        public void addNewTag(String newTag) {
        	this.tags.add(newTag);
        }
        
    }
	

    private long userID; // Added userID field
    private FullName name = new FullName();
    private Vector<Role> roles = new Vector<>();
    private Role activeRole;
    private String username;
    private String email;
    private String password;
    private boolean flag;
    private long expireTime;

    // Default Constructor
    public User() {
        this.name = new FullName();
        this.username = "";
        this.email = "";
        this.password = "";
        this.roles = new Vector<>();
        this.flag = true;
        this.expireTime = System.currentTimeMillis() + (60000 * 5);
    }
    
    public Role getActiveRole() {
        return activeRole;
    }
    
    public void setActiveRole(Role activeRole) {
        this.activeRole = activeRole;
    }

    // Overloaded Constructor
    public User(String nameID, String username, String email, String password, boolean flag, Vector<Role> roles) {
        String[] parts = nameID.split("-");

        name.setFirst(parts.length > 0 ? parts[0] : "");
        name.setMiddle(parts.length > 1 ? parts[1] : "");
        name.setLast(parts.length > 2 ? parts[2] : "");
        name.setPref(parts.length > 3 ? parts[3] : ""); // Avoid IndexOutOfBoundsException

        this.username = username;
        this.email = email;
        this.password = password;
        this.flag = flag;
        this.roles.addAll(roles);
        this.expireTime = System.currentTimeMillis() + (60000 * 5);
    }

    // Getters and Setters for User fields
    
    public long getUserID() {
        return userID;
    }
    
    public void setUserID(long userID) {
        this.userID = userID;
    }
    
    // FULL NAME
    public FullName getName() {
        return name;
    }

    public void setName(FullName name) {
        this.name = name;
    }

    
    // ROLE
    public Vector<Role> getRoles() {
        return roles;
    }

    public void setRoles(Vector<Role> roles) {
        this.roles = roles;
    }

    
    // USERNAME
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    // EMAIL
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    // PASSWORD
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    // FLAG (for one time code)
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    
    // EXPIRE TIME
    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
}