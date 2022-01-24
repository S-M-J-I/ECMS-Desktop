package project.controllers.cf_controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.middlewares.CFMiddleware;
import project.middlewares.MemberMiddleware;
import project.models.ClubForum;
import project.models.Member;
import project.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SeeReasonWindowController extends BaseController implements Initializable {
    private Member member;
    private ClubForum clubForum;
    private HRServicesWindowController controller;

    public SeeReasonWindowController(ViewFactory viewFactory, String fxmlName, Member member, ClubForum clubForum, HRServicesWindowController controller) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.member = member;
        this.clubForum = clubForum;
        this.controller = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        identifierLabel.setText("Why " + member.getName() + " wants to join " + clubForum.getName()+":");
        reasonLabel.setText(member.getReason());
        statusLabel.setText("");
    }

    @FXML
    private Label identifierLabel;

    @FXML
    private Label reasonLabel;

    @FXML
    private Label statusLabel;

    @FXML
    void onAcceptAction(ActionEvent event) {
        MemberMiddleware.enrollMemberIntoCF(member);
        statusLabel.setText("Accepted " + member.getName());
        controller.setUpMemberRequestsTab();
        Platform.runLater(() -> {
            try {
                controller.init_table();
                Thread.sleep(2000);
                super.getViewFactory().closeWindow((Stage) statusLabel.getScene().getWindow());
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void onRejectAction(ActionEvent event) {
        MemberMiddleware.declineJoinRequestIntoCf(member);
        statusLabel.setText("Rejected " + member.getName());
        statusLabel.setTextFill(Color.RED);
        controller.setUpMemberRequestsTab();
        Platform.runLater(() -> {
            try {
                controller.init_table();
                Thread.sleep(2000);
                super.getViewFactory().closeWindow((Stage) statusLabel.getScene().getWindow());
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
