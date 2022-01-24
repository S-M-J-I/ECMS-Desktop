package project.controllers.admin_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.LoginResult;
import project.services.admin_services.AdminLoginServices;
import project.views.ViewFactory;

import java.sql.SQLException;

public class AdminLoginWindowController extends BaseController {
    public AdminLoginWindowController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, "ui/admin_views/"+fxmlName);
    }

    @FXML
    private TextField adminName;

    @FXML
    private PasswordField adminPassword;

    @FXML
    private Label errorLabel;

    @FXML
    void onLoginAction(ActionEvent event) throws SQLException {
        AdminLoginServices adminLoginServices = new AdminLoginServices(adminName.getText(), adminPassword.getText());
        adminLoginServices.start();
        adminLoginServices.setOnSucceeded(e -> {
            LoginResult loginResult = adminLoginServices.getValue();

            switch (loginResult) {
                case SUCCESS -> {
                    errorLabel.setText("");
                    super.getViewFactory().showAdminDashboard();
                    super.getViewFactory().closeWindow((Stage) adminName.getScene().getWindow());
                }
                case FAILED_BY_CREDENTIALS -> {
                    errorLabel.setText("Invalid Credentials");
                }
                case FAILED_BY_UNEXPECTED_ERROR -> {
                    errorLabel.setText("Failed by Unexpected Error");
                }
                case FAILED_BY_NETWORK -> {
                    errorLabel.setText("Failed to get to network");
                }
            }
        });
    }

    @FXML
    void onCFLoginSwitch(ActionEvent event) {
        super.getViewFactory().showCFLogin();
        super.getViewFactory().closeWindow((Stage) adminName.getScene().getWindow());
    }


    @FXML
    void onStudentLoginSwitch(ActionEvent event) {
        super.getViewFactory().showStudentLoginView();
        super.getViewFactory().closeWindow((Stage) adminName.getScene().getWindow());
    }


}
