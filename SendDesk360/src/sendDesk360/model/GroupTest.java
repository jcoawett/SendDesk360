package sendDesk360.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GroupTest {

    @Test
    void testConstructorWithGroupID() {
        // Test the constructor with groupID and name
        Group group = new Group(1L, "Development");
        
        assertEquals(1L, group.getGroupID());
        assertEquals("Development", group.getName());
    }

    @Test
    void testConstructorWithoutGroupID() {
        // Test the constructor with name only
        Group group = new Group("Marketing");
        
        assertEquals(0L, group.getGroupID()); // groupID is default 0 since it wasn't set
        assertEquals("Marketing", group.getName());
    }

    @Test
    void testSettersAndGetters() {
        // Test setters and getters
        Group group = new Group("Sales");

        group.setGroupID(2L);
        group.setName("HR");

        assertEquals(2L, group.getGroupID());
        assertEquals("HR", group.getName());
    }

    @Test
    void testToString() {
        // Test the toString method for readability
        Group group = new Group(3L, "Engineering");
        
        String expected = "Group{groupID=3, name='Engineering'}";
        assertEquals(expected, group.toString());
    }
}
