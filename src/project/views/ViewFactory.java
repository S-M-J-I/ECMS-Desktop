package project.views;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import project.controllers.*;
import project.controllers.admin_controllers.AdminDashboardController;
import project.controllers.admin_controllers.AdminLoginWindowController;
import project.controllers.cf_controllers.*;
import project.controllers.student_controllers.LoginStudentController;
import project.controllers.student_controllers.SignupStudentController;
import project.controllers.student_controllers.StudentDashboardController;
import project.controllers.student_controllers.TaskDetailsSubWindowController;
import project.models.ClubForum;
import project.models.Member;
import project.models.Student;
import project.models.Task;
import project.socket.Server;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ViewFactory implements Initializable {

    private static Map<Class<?>, Stage> windows = new HashMap<>();

    public ViewFactory() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /***************************** Admin view rendering methods *******************************************************/

    public void showAdminLoginWindow() {
        if(!checkIfWindowWasPrevOpened(AdminLoginWindowController.class)) {
            BaseController controller = new AdminLoginWindowController(this, "AdminLoginWindow.fxml");
            init_window(controller, "Admin Login");
        }
    }

    public void showAdminDashboard() {
        BaseController controller = new AdminDashboardController(this, "AdminDashboardWindow.fxml");
        init_window(controller, "Admin Dashboard");
    }


    /***************************** Student view rendering methods *******************************************************/

    public void showStudentLoginView() {
        if(!checkIfWindowWasPrevOpened(LoginStudentController.class)) {
            BaseController controller = new LoginStudentController(this, "LoginStudentWindow.fxml");
            init_window(controller, "Dashboard");
        }
    }

    public void showStudentSignupWindow() {
        BaseController controller = new SignupStudentController(this, "SignupStudentWindow.fxml");
        init_window(controller, "Sign Up");
    }

    public void showStudentDashboard(Student student) {
        BaseController controller = new StudentDashboardController(this, "StudentDashboard.fxml", student);
        init_window(controller, "Dashboard");
    }

    public void showTaskDetailsSubWindow(Task task) {
        BaseController controller = new TaskDetailsSubWindowController(this, "TaskDetailsSubWindow.fxml", task);
        init_window_UnclosableWindow(controller, "Task Details");
    }


    /***************************** CF view rendering methods *******************************************************/

    public void showCFLogin() {
        if(!checkIfWindowWasPrevOpened(LoginController.class)) {
            BaseController controller = new LoginController(this, "LoginWindow.fxml");
            init_window(controller, "Login");
        }
    }

    public void showCFDashboard(ClubForum clubForum) {
        BaseController controller = new CFDashboardController(this, "CFDashboardWindow.fxml", clubForum);
        init_window(controller, clubForum.getName() + " Dashboard");
    }

    public void showCfHRWindow(ClubForum clubForum) {
        BaseController controller = new HRServicesWindowController(this, "HRServicesWindow.fxml", clubForum);
        init_window(controller, clubForum.getName() + " HR Services");
    }

    public void showPRWindow(ClubForum clubForum) {
        BaseController controller = new PRServicesController(this, "PRServicesWindow.fxml", clubForum);
        init_window(controller, clubForum.getName() + " PR Services");
    }

    public void showRnDWindow(ClubForum clubForum) {
        BaseController controller = new RnDServicesController(this, "RnDServicesWindow.fxml", clubForum);
        init_window(controller, clubForum.getName() + " R&D Services");
    }

    public void showCFSettingsWindow(ClubForum clubForum, CFDashboardController cfDashboardController) {
        BaseController controller = new CFSettingsWindowController(this, "CFSettingsWindow.fxml", clubForum, cfDashboardController);
        init_window_UnclosableWindow(controller, clubForum.getName() + " Settings");
    }

    public void showDepartmentAccessWindow(ClubForum clubForum, String ref, Label refLabel, CFDashboardController cfDashboardController) {
        BaseController controller = new DepartmentAccessWindowController(this, "DepartmentAccessWindow.fxml", ref, clubForum, refLabel, cfDashboardController);
        init_window_UnclosableWindow(controller, "Login to Department");
    }

    public void showTreasuryWindow(ClubForum clubForum) {
        BaseController controller = new TreasuryWindowController(this, "TreasuryWindow.fxml", clubForum);
        init_window(controller, clubForum.getName() + " Treasury");
    }

    public void showReasonWindow(Member member, ClubForum clubForum, HRServicesWindowController passingController) {
        BaseController controller = new SeeReasonWindowController(this, "SeeReasonWindow.fxml", member, clubForum, passingController);
        init_window_UnclosableWindow(controller, "Reason");
    }




    private void init_window(BaseController controller, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);

        Parent parent;
        try{
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
        windows.put(controller.getClass(), stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                windows.clear();
                System.exit(0);
            }
        });
    }

    private void init_window_UnclosableWindow(BaseController controller, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);

        Parent parent;
        try{
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
        windows.put(controller.getClass(), stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                windows.remove(controller.getClass());
            }
        });
    }

    private boolean checkIfWindowWasPrevOpened(Class<?> controllerClass) {
        if(windows.containsKey(controllerClass)) {
            windows.get(controllerClass).show();
            return true;
        }

        return false;
    }

    public void closeWindow(Stage stage) {
        stage.close();
    }

    public boolean checkIfAccessWindowIsOpen() {
        return checkIfWindowWasPrevOpened(DepartmentAccessWindowController.class);
    }

    public void closeAccessWindow() {
        windows.remove(DepartmentAccessWindowController.class);
    }
}
