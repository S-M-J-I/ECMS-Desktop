package project.middlewares;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.database.DBHandler;
import project.enums.LoginResult;
import project.models.Admin;
import project.views.ViewFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminMiddleware {

    private static PreparedStatement pst;
    private static DBHandler dbHandler = new DBHandler();
    private static Connection connection;

    public static LoginResult login(String adminName, String adminPassword) throws SQLException {
        dbHandler = new DBHandler();

        try {
            String query = "SELECT * FROM admin WHERE admin_name=? and password=?";

            connection = dbHandler.getConnection();

            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, adminName);
            pst.setString(2, adminPassword);

            ResultSet rs = pst.executeQuery();

            int count = 0;

            while (rs.next()) {
                count += 1;
            }

            if (count == 1) {
                return LoginResult.SUCCESS;
            } else {
                return LoginResult.FAILED_BY_CREDENTIALS;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return LoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
    }

    public static void registerCF(TextField cfName, TextField usernameField, TextField cfPassword, TextField cfJoinPassword, String hr, String pr, String rnd, String mkt, String hr_pass, String pr_pass, String rnd_pass, String mkt_pass) {
        String insert = "INSERT INTO user(`name`, `username`, `password`, `enrol_password`, `hr`, `pr`, `rnd`, `mkt`, `hr_pass`, `pr_pass`, `rnd_pass`, `mkt_pass`)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        dbHandler = new DBHandler();
        connection = dbHandler.getConnection();


        try{
            pst = connection.prepareStatement(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            pst.setString(1, cfName.getText());
            pst.setString(2, usernameField.getText());
            pst.setString(3, cfPassword.getText());
            pst.setString(4, cfJoinPassword.getText());
            pst.setString(5, hr);
            pst.setString(6, pr);
            pst.setString(7, rnd);
            pst.setString(8, mkt);
            pst.setString(9, hr_pass);
            pst.setString(10, pr_pass);
            pst.setString(11, rnd_pass);
            pst.setString(12, mkt_pass);


            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCF(TextField cfNewName, TextField cfNewPassword, String cfName) {
        String update = "UPDATE user SET username=?, password=? WHERE name=?";

        dbHandler = new DBHandler();
        connection = dbHandler.getConnection();



        try{
            pst = connection.prepareStatement(update);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            pst.setString(1, cfNewName.getText());
            pst.setString(2, cfNewPassword.getText());
            pst.setString(3, cfName);


            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCF(String cfName) {
        String delete = "DELETE FROM user WHERE name=?";

        dbHandler = new DBHandler();
        connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(delete);
            pst.setString(1, cfName);

            pst.executeUpdate();
            deleteAllDataOfCf(cfName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteAllDataOfCf(String cfName) {
        String delete = "DELETE FROM members WHERE cf=?";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(delete);

            pst.setString(1,cfName);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllCFNames() {
        ArrayList<String> names = new ArrayList<>();
        String select = "SELECT name FROM user";

        dbHandler = new DBHandler();
        connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(select);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                names.add( new String(rs.getString("name")) );
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }

    public static Admin getAdmin(String adminName, String adminPassword) {
        dbHandler = new DBHandler();

        Admin admin = new Admin();
        try {
            String query = "SELECT * FROM admin WHERE admin_name=? and password=?";

            connection = dbHandler.getConnection();

            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, adminName);
            pst.setString(2, adminPassword);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                admin = new Admin(
                        rs.getString("admin_name"),
                        rs.getString("password")
                );
            }

            return admin;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
