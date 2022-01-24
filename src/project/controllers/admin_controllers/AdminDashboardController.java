package project.controllers.admin_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.CSSTabPath;
import project.middlewares.AdminMiddleware;
import project.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController extends BaseController implements Initializable {


    public AdminDashboardController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, "ui/admin_views/"+fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
        rndPasswordField.setDisable(true);
        treasuryPasswordField.setDisable(true);
        rndCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rndPasswordField.setDisable(!rndCheckbox.isSelected());
            }
        });
        treasuryCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                treasuryPasswordField.setDisable(!treasuryCheckbox.isSelected());
            }
        });
        setUpTabsAndListenToChanges();
    }

    @FXML
    private Label adminName;

    @FXML
    private TabPane tabPane;


    /** Register CF Tab **/

    @FXML
    private Tab registerCFTab;

    @FXML
    private TextField cfName;

    @FXML
    private TextField cfPassword;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField cfJoinPassword;

    @FXML
    private Label successLabel;

    @FXML
    private CheckBox hrCheckBox;

    @FXML
    private CheckBox prCheckbox;

    @FXML
    private CheckBox rndCheckbox;

    @FXML
    private CheckBox treasuryCheckbox;

    @FXML
    private TextField hrPasswordField;

    @FXML
    private TextField prPasswordField;

    @FXML
    private TextField rndPasswordField;

    @FXML
    private TextField treasuryPasswordField;


    /** Update CF tab **/

    @FXML
    private Tab updateCFTab;

    @FXML
    private TextField cfNewName;

    @FXML
    private TextField cfNewPassword;


    /** Remove CF tab **/

    @FXML
    private Tab removeCFTab;

    @FXML
    private ChoiceBox<String> cfChoiceMenu;

    @FXML
    private ChoiceBox<String> cfRemoveChoiceMenu;


    @FXML
    void onRemoveCFAction(ActionEvent event) {
        AdminMiddleware.removeCF(cfRemoveChoiceMenu.getValue());
        System.out.println("Removed");
        refresh();
    }

    @FXML
    void onUpdateAction(ActionEvent event) {
        AdminMiddleware.updateCF(cfNewName, cfNewPassword, cfChoiceMenu.getValue());
        System.out.println("Updated");
    }


    @FXML
    void onLogoutAction(ActionEvent event) {
        super.getViewFactory().showAdminLoginWindow();
        super.getViewFactory().closeWindow((Stage) adminName.getScene().getWindow());
    }

    @FXML
    void onRegisterAction(ActionEvent event) {
        try {
            String hr = (hrCheckBox.isSelected() ? "T" : "F");
            String pr = (prCheckbox.isSelected() ? "T" : "F");
            String rnd = (rndCheckbox.isSelected() ? "T" : "F");
            String treasury = (treasuryCheckbox.isSelected() ? "T" : "F");
            AdminMiddleware.registerCF(
                    cfName, usernameField, cfPassword, cfJoinPassword,
                    hr, pr, rnd, treasury,
                    hrPasswordField.getText(), prPasswordField.getText(), rndPasswordField.getText(), treasuryPasswordField.getText()
            );
            // TODO: set up dept. passwords and make sure dept can change it (in CFSettingsWindowController)
            System.out.println("CF Created");
            refreshCreateCFTab();
            successLabel.setText(cfName.getText()+" successfully created!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /** HELPER METHODS **/

    private void refreshCreateCFTab() {
        Platform.runLater(() -> {
            cfName.setText("");
            usernameField.setText("");
            cfPassword.setText("");
            cfJoinPassword.setText("");
            rndCheckbox.setSelected(false);
            treasuryCheckbox.setSelected(false);
        });
    }

    private void refresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cfChoiceMenu.setItems(FXCollections.observableArrayList(AdminMiddleware.getAllCFNames()));
                cfRemoveChoiceMenu.setItems(FXCollections.observableArrayList(AdminMiddleware.getAllCFNames()));
            }
        });
    }

    private void setUpTabsAndListenToChanges() {
        tabPane.setStyle("-fx-background-color: white;");
        tabPane.getStyleClass().add("floating");
        registerCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
        updateCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        removeCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Platform.runLater(() -> {
                if(newTab.equals(registerCFTab)) {
                    registerCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    updateCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if(newTab.equals(updateCFTab)) {
                    registerCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    removeCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(removeCFTab)) {
                    registerCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                }
            });
        });
    }
}
