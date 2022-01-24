package project.middlewares;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.database.DBHandler;
import project.enums.LoginResult;
import project.enums.SignUpResult;
import project.models.Student;
import project.views.ViewFactory;

import java.sql.*;

public class StudentMiddleware {

    private static PreparedStatement pst;
    private static Connection connection;
    private static final DBHandler dbHandler = new DBHandler();

    public static LoginResult loginStudent(String name, String password) throws SQLException{

        System.out.println("Trying to log in again");
        connection = dbHandler.getConnection();

        String query = "SELECT * FROM students WHERE username=? and password=?";

        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            int count = 0;

            while (rs.next()) {
                count+=1;
            }

            if(count == 1) {
                System.out.println("Valid Credentials");
                return LoginResult.SUCCESS;
            } else {
                return LoginResult.FAILED_BY_CREDENTIALS;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return LoginResult.FAILED_BY_UNEXPECTED_ERROR;
        } finally {
            connection.close();
        }
    }


    public static SignUpResult signup(String uiuIDField, String fullnameField, String usernameField, String passwordField, String confirmPassword, String departmentField, String phoneField, String emailField) {
        String insert = "INSERT INTO students(UIU_id, name, username, password, department, email, phone)"
                + "VALUES (?,?,?,?,?,?,?)";


        connection = dbHandler.getConnection();

        if(!uiuIDField.contains(" ")) {
            uiuIDField = (uiuIDField.replaceAll("(.{" + 3 + "})", "$1 ").trim());
        }

        if(!confirmPassword.equals(passwordField)) {
            return SignUpResult.PASSWORDS_NO_MATCH;
        }

        try {

            pst = connection.prepareStatement(insert);

            pst.setString(1, uiuIDField);
            pst.setString(2, fullnameField);
            pst.setString(3, usernameField);
            pst.setString(4, passwordField);
            pst.setString(5, departmentField);
            pst.setString(6, emailField);
            pst.setString(7, phoneField);


            pst.executeUpdate();
            return SignUpResult.SUCCESS;

        } catch (SQLException e) {
            return SignUpResult.ALREADY_IN_DB;
        } catch (Exception e) {
            return SignUpResult.FAILED_BY_UNEXPECTED_ERROR;
        }
    }

    public static void update(TextField newUsername, TextField newEmail, TextField newPhone, PasswordField newPassword, int id) {

        String username;
        String password;
        String email;
        String phone;


        connection = dbHandler.getConnection();

        if(!newUsername.getText().equals("")) {
            username = newUsername.getText();
        } else {
            username = newUsername.getPromptText().split(" ")[1];
        }

        if(!newPassword.getText().equals("")) {
            password = newPassword.getText();
        } else {
            password = newPassword.getPromptText().split(" ")[1];
        }

        if(!newEmail.getText().equals("")) {
            email = newEmail.getText();
        } else {
            email = newEmail.getPromptText().split(" ")[1];
        }

        if(!newPhone.getText().equals("")) {
            phone = newPhone.getText();
        } else {
            phone = newPhone.getPromptText().split(" ")[1];
        }

        String update = "UPDATE students SET username=?, password=?, email=?, phone=? WHERE member_id=?";

        try {

            pst = connection.prepareStatement(update);

            pst.setString(1,username);
            pst.setString(2,password);
            pst.setString(3,email);
            pst.setString(4,phone);
            pst.setInt(5,id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Student getData(int id) {
        Student student = new Student();

        connection = dbHandler.getConnection();

        String select = "SELECT * FROM students WHERE member_id=?";

        try {

            pst = connection.prepareStatement(select);

            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                student = new Student(
                        rs.getInt("member_id"),
                        rs.getString("UIU_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static Student getDataByName(String name) {
        Student student = new Student();

        connection = dbHandler.getConnection();

        String select = "SELECT * FROM students WHERE name=?";

        try {

            pst = connection.prepareStatement(select);

            pst.setString(1,name);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                student = new Student(
                        rs.getInt("member_id"),
                        rs.getString("UIU_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }

    public static Student getDataByNameAndPassword(String username, String password) {
        Student student = new Student();

        connection = dbHandler.getConnection();

        String select = "SELECT * FROM students WHERE username=? and password=?";

        try {

            pst = connection.prepareStatement(select);

            pst.setString(1,username);
            pst.setString(2,password);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                student = new Student(
                        rs.getInt("member_id"),
                        rs.getString("UIU_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }



}
