package project.controllers.cf_controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.middlewares.AnnouncementMiddlewares;
import project.models.Announcement;
import project.models.ClubForum;
import project.views.ViewFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class PRServicesController extends BaseController implements Initializable {

    private ClubForum clubForum;

    public PRServicesController(ViewFactory viewFactory, String fxmlName, ClubForum clubForum) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.clubForum = clubForum;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(this.clubForum.getName());
        setUpAnnouncementArea();
    }

    @FXML
    private HTMLEditor announcementArea;

    @FXML
    void onAnnouncementPublish(ActionEvent event) {
        sendAnnouncement();
        Platform.runLater(() -> {
            setUpAnnouncementArea();
        });
    }

    private void setUpAnnouncementArea() {
        announcementArea.setHtmlText("");
    }

    private void sendAnnouncement() {
        announcementArea.setHtmlText(announcementArea.getHtmlText()+"</h1><div><p><hr style=\"text-align: center;\"></p></div>");
        String announcement = this.clubForum.getName() +"\nPosted at "+new Date().toString()+":\n"+ announcementArea.getHtmlText()  +"\nbreak";
        String announcementInDB = announcementArea.getHtmlText();
        super.getWriter().println(announcement);
        System.out.println(announcement);
        AnnouncementMiddlewares.postAnnouncement(
                this.clubForum.getName(),
                new Date().toString(),
                announcementInDB,
                this.clubForum.getForumID()
        );
    }

    @FXML
    void onBackToDashboardAction(ActionEvent event) {
        super.getViewFactory().showCFDashboard(clubForum);
        super.getViewFactory().closeWindow((Stage) announcementArea.getScene().getWindow());
    }

}
