package project.controllers.student_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.LoginResult;
import project.middlewares.StudentMiddleware;
import project.services.student_services.StudentLoginService;
import project.views.ViewFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginStudentController extends BaseController implements Initializable {

    private StudentLoginService studentAuthenticationServices = new StudentLoginService();

    public LoginStudentController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, "ui/student_views/"+fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private Label errorLabel;

    @FXML
    void onSubmit(ActionEvent event) throws SQLException {

        if(name.getText().equalsIgnoreCase("Admin")) {
            super.getViewFactory().showAdminLoginWindow();
            super.getViewFactory().closeWindow((Stage) name.getScene().getWindow());
            return;
        }

        studentAuthenticationServices = new StudentLoginService(name.getText(), password.getText());
        studentAuthenticationServices.start();
        studentAuthenticationServices.setOnSucceeded(e -> {
            LoginResult loginResult = studentAuthenticationServices.getValue();

            switch (loginResult) {
                case SUCCESS -> {
                    System.out.println("Login Success");
                    super.getViewFactory().showStudentDashboard(StudentMiddleware.getDataByNameAndPassword(name.getText(), password.getText()));
                    errorLabel.setText("");
                    Stage stage = (Stage) errorLabel.getScene().getWindow();
                    super.getViewFactory().closeWindow(stage);
                }
                case FAILED_BY_UNEXPECTED_ERROR -> {
                    errorLabel.setText("Unexpected error");
                }
                case FAILED_BY_NETWORK -> {
                    errorLabel.setText("Failed to get to network");
                }
                case FAILED_BY_CREDENTIALS -> {
                    errorLabel.setText("Invalid Credentials");
                }
            }
        });

    }

    @FXML
    void onCFLoginSwitch(ActionEvent event) {
        super.getViewFactory().showCFLogin();
        super.getViewFactory().closeWindow((Stage) name.getScene().getWindow());
    }

    @FXML
    void onSignupActionSwitch(ActionEvent event) {
        super.getViewFactory().showStudentSignupWindow();
        super.getViewFactory().closeWindow((Stage) name.getScene().getWindow());
    }


}
