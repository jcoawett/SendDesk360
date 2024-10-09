package sendDesk360.viewModel;

import sendDesk360.SendDesk360;
import sendDesk360.model.Classroom;
import sendDesk360.model.User;
import java.util.Vector;

public class DashboardViewModel {

    private final SendDesk360 mainApp;
    private final Classroom classroom;

    public DashboardViewModel(SendDesk360 mainApp) {
        this.mainApp = mainApp;
        this.classroom = new Classroom();
        this.classroom.newFullClassroom();
    }

    public Vector<User> getUsers() {
        return classroom.getClassroom();
    }
}