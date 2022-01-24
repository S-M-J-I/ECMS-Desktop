package project.controllers.cf_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.middlewares.CFMiddleware;
import project.middlewares.MemberMiddleware;
import project.models.ClubForum;
import project.views.ViewFactory;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class CFDashboardController extends BaseController implements Initializable {
    private ClubForum clubForum;
    private URL url;
    private ResourceBundle rs;
    static boolean hrAccess = false;
    static boolean prAccess = false;
    static boolean rndAccess = false;
    static boolean treasuryAccess = false;
    static boolean settings = false;

    public CFDashboardController(ViewFactory viewFactory, String fxmlName, ClubForum clubForum) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.clubForum = clubForum;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.url = url;
        this.rs = resourceBundle;
        cfName.setText(clubForum.getName());
        memcountLabel.setText(Integer.toString(MemberMiddleware.getMembersCount(clubForum.getForumID())));
        rndBtn.setVisible(CFMiddleware.ifRnD(this.clubForum.getForumID()));
        treasuryBtn.setVisible(CFMiddleware.ifTreasury(this.clubForum.getForumID()));

        try {

            Image image = new Image(new FileInputStream("D:\\Codes\\ECA-Management-System\\src\\project\\database\\profile_pics\\"+this.clubForum.getForumID()+".jpg"));

            if(image != null) {
                cfImageHolder.setFill(new ImagePattern(image));
            }
        } catch (Exception e) {
            // empty
        }

    }

    public void reInitialize(ClubForum clubForum) {
        this.clubForum = clubForum;
        initialize(url, rs);
    }

    @FXML
    private AnchorPane sideAnchorPane;

    @FXML
    private Button hrBtn;

    @FXML
    private Circle cfImageHolder;

    @FXML
    private ImageView hrImage;

    @FXML
    private ImageView prImage;

    @FXML
    private ImageView rndImage;

    @FXML
    private ImageView mktImage;

    @FXML
    private Button prBtn;

    @FXML
    public Button rndBtn;

    @FXML
    public Button treasuryBtn;

    @FXML
    private Label cfName;

    @FXML
    private Label memcountLabel;

    @FXML
    void onHRAction(ActionEvent event) {
        if(hrAccess) {
            super.getViewFactory().showCfHRWindow(this.clubForum);
            super.getViewFactory().closeWindow((Stage) hrBtn.getScene().getWindow());
            return;
        }

        if(!super.getViewFactory().checkIfAccessWindowIsOpen()) {
            super.getViewFactory().showDepartmentAccessWindow(this.clubForum, "hr", getCfName(), this);
        }
    }

    @FXML
    void onPRAction(ActionEvent event) {
        if(prAccess) {
            super.getViewFactory().showPRWindow(this.clubForum);
            super.getViewFactory().closeWindow((Stage) hrBtn.getScene().getWindow());
            return;
        }

        if(!super.getViewFactory().checkIfAccessWindowIsOpen()) {
            super.getViewFactory().showDepartmentAccessWindow(this.clubForum, "pr", getCfName(), this);
        }
    }

    @FXML
    void onrRnDAction(ActionEvent event) {
        if(rndAccess) {
            super.getViewFactory().showRnDWindow(this.clubForum);
            super.getViewFactory().closeWindow((Stage) hrBtn.getScene().getWindow());
            return;
        }

        if(!super.getViewFactory().checkIfAccessWindowIsOpen()) {
            super.getViewFactory().showDepartmentAccessWindow(this.clubForum, "rnd", getCfName(), this);
        }
    }

    @FXML
    void onTreasuryServicesAction(ActionEvent event) {
        if(treasuryAccess) {
            super.getViewFactory().showTreasuryWindow(this.clubForum);
            super.getViewFactory().closeWindow((Stage) hrBtn.getScene().getWindow());
            return;
        }

        if(!super.getViewFactory().checkIfAccessWindowIsOpen()) {
            super.getViewFactory().showDepartmentAccessWindow(this.clubForum, "treasury", getCfName(), this);
        }
    }

    @FXML
    void onLogoutAction(ActionEvent event) {
        super.getViewFactory().showCFLogin();
        super.getViewFactory().closeWindow((Stage) hrBtn.getScene().getWindow());
        hrAccess = false;
        prAccess = false;
        rndAccess = false;
        treasuryAccess = false;
        settings = false;
    }

    @FXML
    void onSettingsAction(ActionEvent event) {
        if(settings) {
            super.getViewFactory().showCFSettingsWindow(this.clubForum, this);
            return;
        }

        if(!super.getViewFactory().checkIfAccessWindowIsOpen()) {
            super.getViewFactory().showDepartmentAccessWindow(this.clubForum, "settings", getCfName(), this);
        }
    }

    /** GETTERS AND SETTERS **/

    public ClubForum getClubForum() {
        return clubForum;
    }

    public Label getCfName() {
        return cfName;
    }
}
