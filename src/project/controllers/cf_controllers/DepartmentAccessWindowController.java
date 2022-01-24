package project.controllers.cf_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.models.ClubForum;
import project.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentAccessWindowController extends BaseController implements Initializable {
    private String ref;
    private ClubForum clubForum;
    private Label refLabel;
    private CFDashboardController controller;

    public DepartmentAccessWindowController(ViewFactory viewFactory, String fxmlName, String ref, ClubForum clubForum, Label refLabel, CFDashboardController controller) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.ref = ref;
        this.clubForum = clubForum;
        this.refLabel = refLabel;
        this.controller = controller;
    }

    @FXML
    private Label departmentIndicatorLabel;

    @FXML
    private Label successLabel;

    @FXML
    private PasswordField accessDeptPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (ref) {
            case "hr" -> {
                departmentIndicatorLabel.setText("Enter password for HR");
            }
            case "pr" -> {
                departmentIndicatorLabel.setText("Enter password for PR");
            }
            case "rnd" -> {
                departmentIndicatorLabel.setText("Enter password for R&D");
            }
            case "treasury" -> {
                departmentIndicatorLabel.setText("Enter password for Treasury");
            }
            case "settings" -> {
                departmentIndicatorLabel.setText("Enter password for Settings");
            }
        }
    }

    @FXML
    void onAccessBtnAction(ActionEvent event) {
        // TODO: get password field text and match with password
        switch (ref) {
            case "hr" -> {
                if(accessDeptPasswordField.getText().equals(this.clubForum.getHrPass())) {
                    super.getViewFactory().showCfHRWindow(this.clubForum);
                    super.getViewFactory().closeWindow((Stage) this.refLabel.getScene().getWindow());
                    super.getViewFactory().closeWindow((Stage) this.successLabel.getScene().getWindow());
                    super.getViewFactory().closeAccessWindow();
                    CFDashboardController.hrAccess = true;
                } else {
                    successLabel.setText("Wrong password");
                    successLabel.setTextFill(Color.RED);
                }
            }
            case "pr" -> {
                if(accessDeptPasswordField.getText().equals(this.clubForum.getPrPass())) {
                    super.getViewFactory().showPRWindow(this.clubForum);
                    super.getViewFactory().closeWindow((Stage) this.refLabel.getScene().getWindow());
                    super.getViewFactory().closeWindow((Stage) this.successLabel.getScene().getWindow());
                    super.getViewFactory().closeAccessWindow();
                    CFDashboardController.prAccess = true;
                } else {
                    successLabel.setText("Wrong password");
                    successLabel.setTextFill(Color.RED);
                }
            }
            case "rnd" -> {
                if(accessDeptPasswordField.getText().equals(this.clubForum.getRndPass())) {
                    super.getViewFactory().showRnDWindow(this.clubForum);
                    super.getViewFactory().closeWindow((Stage) this.refLabel.getScene().getWindow());
                    super.getViewFactory().closeWindow((Stage) this.successLabel.getScene().getWindow());
                    super.getViewFactory().closeAccessWindow();
                    CFDashboardController.rndAccess = true;
                } else {
                    successLabel.setText("Wrong password");
                    successLabel.setTextFill(Color.RED);
                }
            }
            case "treasury" -> {
                if(accessDeptPasswordField.getText().equals(this.clubForum.getTreasuryPass())) {
                    super.getViewFactory().showTreasuryWindow(this.clubForum);
                    super.getViewFactory().closeWindow((Stage) this.refLabel.getScene().getWindow());
                    super.getViewFactory().closeWindow((Stage) this.successLabel.getScene().getWindow());
                    super.getViewFactory().closeAccessWindow();
                    CFDashboardController.treasuryAccess = true;
                } else {
                    successLabel.setText("Wrong password");
                    successLabel.setTextFill(Color.RED);
                }
            }
            case "settings" -> {
                if(accessDeptPasswordField.getText().equals(this.clubForum.getSettingsPass())) {
                    super.getViewFactory().showCFSettingsWindow(this.clubForum, controller);
                    super.getViewFactory().closeWindow((Stage) this.successLabel.getScene().getWindow());
                    super.getViewFactory().closeAccessWindow();
                    CFDashboardController.settings = true;
                } else {
                    successLabel.setText("Wrong password");
                    successLabel.setTextFill(Color.RED);
                }
            }
        }
    }

}
