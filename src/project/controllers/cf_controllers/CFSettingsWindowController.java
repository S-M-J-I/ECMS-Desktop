package project.controllers.cf_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import project.controllers.BaseController;
import project.middlewares.CFMiddleware;
import project.models.ClubForum;
import project.views.ViewFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CFSettingsWindowController extends BaseController implements Initializable {
    private ClubForum clubForum;
    private CFDashboardController cfDashboardController;

    public CFSettingsWindowController(ViewFactory viewFactory, String fxmlName, ClubForum clubForum, CFDashboardController cfDashboardController) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.clubForum = clubForum;
        this.cfDashboardController = cfDashboardController;
    }

    @FXML
    private CheckBox hrCheckBox;

    @FXML
    private CheckBox prCheckBox;

    @FXML
    private CheckBox rndCheckBox;

    @FXML
    private CheckBox treasuryCheckBox;

    @FXML
    private Label changesLabel;

    @FXML
    private TextField hrDepartmentPasswordField;

    @FXML
    private TextField prDepartmentPasswordField;

    @FXML
    private TextField treasuryDepartmentPasswordField;

    @FXML
    private TextField rndDepartmentPasswordField;

    @FXML
    private ColorPicker primaryColor;

    @FXML
    private ColorPicker secondaryColor;

    @FXML
    private TextField cfPpath;

    @FXML
    void onBrowseAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose profile picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.jpeg", "*.gif", "*.png"));
        File file = fileChooser.showOpenDialog(cfPpath.getScene().getWindow());

        if (file != null) {
            // pickUpPathField it's your TextField fx:id
            cfPpath.setText(file.getPath());

        } else  {
            System.out.println("error"); // or something else
        }
    }


    @FXML
    void onChangesAction(ActionEvent event) {
        ArrayList<String> departments = new ArrayList<>();
        ArrayList<String> newPasswords = new ArrayList<>();

        if(!cfPpath.getText().equals("")) {
            String imgPath = cfPpath.getText();
            CFMiddleware.uploadImage(imgPath, this.clubForum.getForumID());
        }

        if(!hrDepartmentPasswordField.getText().equals("")) {
            departments.add("`hr_pass`");
            newPasswords.add("'"+hrDepartmentPasswordField.getText()+"'");
            this.clubForum.setHrPass(hrDepartmentPasswordField.getText());
        }

        if(!prDepartmentPasswordField.getText().equals("")) {
            departments.add("`pr_pass`");
            newPasswords.add("'"+prDepartmentPasswordField.getText()+"'");
            this.clubForum.setPrPass(prDepartmentPasswordField.getText());
        }

        if(!rndCheckBox.isSelected()) {
            CFMiddleware.changeRnd(this.clubForum.getForumID(), "F");
        } else {
            CFMiddleware.changeRnd(this.clubForum.getForumID(), "T");
            if(!rndDepartmentPasswordField.getText().equals("")) {
                departments.add("`rnd_pass`");
                newPasswords.add("'"+rndDepartmentPasswordField.getText()+"'");
                this.clubForum.setRndPass(rndDepartmentPasswordField.getText());
            }
        }

        if(!treasuryCheckBox.isSelected()) {
            CFMiddleware.changeTreasury(this.clubForum.getForumID(), "F");
        } else {
            CFMiddleware.changeTreasury(this.clubForum.getForumID(), "T");
            if(!treasuryDepartmentPasswordField.getText().equals("")) {
                departments.add("`treasury_pass`");
                newPasswords.add("'"+ treasuryDepartmentPasswordField.getText()+"'");
                this.clubForum.setTreasuryPass(treasuryDepartmentPasswordField.getText());
            }
        }

        if(departments.size() > 0) {
            CFMiddleware.updateDepartmentPasswords(newPasswords, departments, this.clubForum.getForumID());
        }


        changesLabel.setText("Updated!");

        this.cfDashboardController.reInitialize(this.clubForum);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hrCheckBox.setDisable(true);
        prCheckBox.setDisable(true);
        rndCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rndDepartmentPasswordField.setDisable(!rndCheckBox.isSelected());
            }
        });
        treasuryCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                treasuryDepartmentPasswordField.setDisable(!treasuryCheckBox.isSelected());
            }
        });
        rndCheckBox.setSelected(CFMiddleware.ifRnD(this.clubForum.getForumID()));
        treasuryCheckBox.setSelected(CFMiddleware.ifTreasury(this.clubForum.getForumID()));
        rndDepartmentPasswordField.setDisable(!CFMiddleware.ifRnD(this.clubForum.getForumID()));
        treasuryDepartmentPasswordField.setDisable(!CFMiddleware.ifTreasury(this.clubForum.getForumID()));
    }

    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                .toUpperCase();
    }
}
