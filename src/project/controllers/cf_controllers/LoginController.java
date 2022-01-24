package project.controllers.cf_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.LoginResult;
import project.middlewares.CFMiddleware;
import project.services.cf_services.CFLoginServices;
import project.views.ViewFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {
    public LoginController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label errorLabel;

    @FXML
    void onSubmit(ActionEvent event) throws SQLException {

        if(username.getText().equalsIgnoreCase("Admin")) {
            super.getViewFactory().showAdminLoginWindow();
            Stage stage = (Stage) username.getScene().getWindow();
            super.getViewFactory().closeWindow(stage);
            return;
        }

        CFLoginServices cfLoginServices = new CFLoginServices(username.getText(),password.getText());
        cfLoginServices.start();
        cfLoginServices.setOnSucceeded(e -> {
            LoginResult loginResult = cfLoginServices.getValue();

            switch (loginResult) {
                case SUCCESS -> {
                    errorLabel.setText("");
                    super.getViewFactory().showCFDashboard(CFMiddleware.getCF(username.getText(), password.getText()));
                    super.getViewFactory().closeWindow((Stage) username.getScene().getWindow());
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
    void onStudentLoginSwitch(ActionEvent event) {
        super.getViewFactory().showStudentLoginView();
        Stage stage = (Stage) username.getScene().getWindow();
        super.getViewFactory().closeWindow(stage);
    }

}
