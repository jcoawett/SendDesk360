package sendDesk360.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

public class UserTest {
    private User user;
    
    @BeforeEach
    public void setUp() {
        // Initialize a user before each test
        user = new User();
    }
    
    @Test
    public void testDefaultConstructor() {
        // Test default constructor
        assertNotNull(user);
        assertEquals("", user.getUsername());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
        assertTrue(user.isFlag());
        assertNotNull(user.getName());
        assertNotNull(user.getRoles());
        assertEquals(0, user.getRoles().size());
        
        // Check expire time is set to 5 minutes from current time
        long currentTime = System.currentTimeMillis();
        assertTrue(user.getExpireTime() > currentTime);
        assertTrue(user.getExpireTime() <= currentTime + (60000 * 5));
    }
    
    @Test
    public void testOverloadedConstructor() {
        // Prepare test data
        String nameID = "Bryce-Alexander-Jackson-BJ";
        String username = "bryce.jackson";
        String email = "bryce.jackson@example.com";
        String password = "securePassword123";
        boolean flag = false;
        
        Vector<User.Role> roles = new Vector<>();
        User.Role role1 = new User.Role();
        role1.setName("Admin");
        role1.setPrivilege(10);
        roles.add(role1);
        
        User userWithParams = new User(nameID, username, email, password, flag, roles);
        
        // Verify name parts
        assertEquals("Bryce", userWithParams.getName().getFirst());
        assertEquals("Alexander", userWithParams.getName().getMiddle());
        assertEquals("Jackson", userWithParams.getName().getLast());
        assertEquals("BJ", userWithParams.getName().getPref());
        
        // Verify other fields
        assertEquals(username, userWithParams.getUsername());
        assertEquals(email, userWithParams.getEmail());
        assertEquals(password, userWithParams.getPassword());
        assertFalse(userWithParams.isFlag());
        assertEquals(1, userWithParams.getRoles().size());
        assertEquals("Admin", userWithParams.getRoles().get(0).getName());
        assertEquals(10, userWithParams.getRoles().get(0).getPrivilege());
        
        // Check expire time is set to 5 minutes from current time
        long currentTime = System.currentTimeMillis();
        assertTrue(userWithParams.getExpireTime() > currentTime);
        assertTrue(userWithParams.getExpireTime() <= currentTime + (60000 * 5));
    }
    
    @Test
    public void testSettersAndGetters() {
        // Test UserID
        user.setUserID(1234L);
        assertEquals(1234L, user.getUserID());
        
        // Test FullName setters and getters
        user.getName().setFirst("Bryce");
        user.getName().setMiddle("Alexander");
        user.getName().setLast("Jackson");
        user.getName().setPref("BJ");
        
        assertEquals("Bryce", user.getName().getFirst());
        assertEquals("Alexander", user.getName().getMiddle());
        assertEquals("Jackson", user.getName().getLast());
        assertEquals("BJ", user.getName().getPref());
        
        // Test Username
        user.setUsername("bryce.jackson");
        assertEquals("bryce.jackson", user.getUsername());
        
        // Test Email
        user.setEmail("bryce.jackson@example.com");
        assertEquals("bryce.jackson@example.com", user.getEmail());
        
        // Test Password
        user.setPassword("newPassword456");
        assertEquals("newPassword456", user.getPassword());
        
        // Test Flag
        user.setFlag(false);
        assertFalse(user.isFlag());
        
        // Check Expire Time
        user.setExpireTime(System.currentTimeMillis() + 10000);
        assertTrue(user.getExpireTime() > System.currentTimeMillis());
    }
}