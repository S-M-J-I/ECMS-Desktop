package project.middlewares;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import project.models.ClubForum;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class NotificationSender {

    public static void sendNotification(String cf) {
        ClubForum clubForum = CFMiddleware.getCFByName(cf);

        String title = "New Announcement";
        String message = "You've got a new announcement from " + cf;

        NotificationType notification = NotificationType.SUCCESS;
        TrayNotification tray = new TrayNotification(title, message, notification);
        try {
            tray.setImage(
                    new Image(new FileInputStream("D:\\Codes\\ECA-Management-System\\src\\project\\database\\profile_pics\\"+clubForum.getForumID()+".jpg"))
            );
        } catch (IOException e) {
            //
        }

        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndWait();
        playSound();
    }

    private static void playSound() {
        String bip = "src/project/resources/custom.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
}
