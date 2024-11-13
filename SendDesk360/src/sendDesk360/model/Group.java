package sendDesk360.model;

public class Group {

    private long groupID;
    private String name;

    // Constructors
    public Group(long groupID, String name) {
        this.groupID = groupID;
        this.name = name;
    }

    public Group(String name) {
        this.name = name;
    }

    // Getters and Setters
    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Optional: Override toString for easy debugging or display
    @Override
    public String toString() {
        return "Group{" +
                "groupID=" + groupID +
                ", name='" + name + '\'' +
                '}';
    }
}
