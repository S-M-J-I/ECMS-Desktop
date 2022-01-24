package project.middlewares;

import project.database.DBHandler;
import project.models.Announcement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class AnnouncementMiddlewares {
    private static PreparedStatement pst;
    private static Connection connection;
    private static final DBHandler dbHandler = new DBHandler();

    public static void postAnnouncement(String title, String dateTime, String body, int posted_by){
        String post = "INSERT INTO announcements(`title`, `date`, `body`, `posted_by`) VALUES(?,?,?,?)";
        try {
            connection = dbHandler.getConnection();

            pst = connection.prepareStatement(post);

            pst.setString(1,title);
            pst.setString(2,dateTime);
            pst.setString(3,body);
            pst.setInt(4,posted_by);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTotalAnnouncements() {
        String get = "SELECT * FROM announcements";
        int count = 0;
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static Announcement pullMostRecentAnnouncement() {
        String get = "SELECT * FROM announcements WHERE id=?";
        ArrayList<Announcement> announcements = new ArrayList<>();
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);
            pst.setInt(1, getTotalAnnouncements());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                announcements.add(new Announcement(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("body"),
                        rs.getInt("posted_by")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return announcements.get(announcements.size()-1);
    }

    public static ArrayList<Announcement> pullAnnouncementByName(String cfName) {
        ArrayList<Announcement> announcements = new ArrayList<>();
        String get = "SELECT * FROM announcements WHERE `posted_by`=?";
        int id = CFMiddleware.getCFByName(cfName).getForumID();
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                announcements.add(new Announcement(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("date"),
                    rs.getString("body"),
                    rs.getInt("posted_by")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.reverse(announcements);
        return announcements;
    }
}
