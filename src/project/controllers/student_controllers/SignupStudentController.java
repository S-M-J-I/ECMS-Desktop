package project.controllers.student_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.SignUpResult;
import project.middlewares.StudentMiddleware;
import project.services.student_services.StudentSignupService;
import project.views.ViewFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupStudentController extends BaseController implements Initializable {

    public SignupStudentController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, "ui/student_views/"+fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private TextField uiuIDField;

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField departmentField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    void onLoginActionSwitch(ActionEvent event) {
        super.getViewFactory().showStudentLoginView();
        super.getViewFactory().closeWindow((Stage) confirmPasswordField.getScene().getWindow());
    }

    @FXML
    void onSignupAction(ActionEvent event) {
        StudentSignupService studentSignupService = new StudentSignupService(
                uiuIDField.getText(),
                fullnameField.getText(),
                usernameField.getText(),
                passwordField.getText(),
                confirmPasswordField.getText(),
                departmentField.getText(),
                phoneField.getText(),
                emailField.getText()
        );
        studentSignupService.start();
        studentSignupService.setOnSucceeded(e -> {
            SignUpResult signUpResult = studentSignupService.getValue();
            switch (signUpResult) {
                case SUCCESS -> {
                    super.getViewFactory().showStudentDashboard(StudentMiddleware.getDataByNameAndPassword(usernameField.getText(), passwordField.getText()));
                    super.getViewFactory().closeWindow((Stage) uiuIDField.getScene().getWindow());
                }
                case PASSWORDS_NO_MATCH -> {
                    errorLabel.setText("Passwords do not match!");
                }
                case ALREADY_IN_DB -> {
                    errorLabel.setText("Email already in use");
                } case FAILED_BY_UNEXPECTED_ERROR -> {
                    errorLabel.setText("Unexpected error");
                }
            }
        });

    }

}
