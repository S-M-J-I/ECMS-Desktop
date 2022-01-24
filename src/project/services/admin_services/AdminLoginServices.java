package project.services.admin_services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import project.enums.LoginResult;
import project.middlewares.AdminMiddleware;

import java.sql.SQLException;

public class AdminLoginServices extends Service<LoginResult> {

    private String adminName;
    private String adminPassword;

    public AdminLoginServices(String adminName, String adminPassword) {
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    @Override
    protected Task<LoginResult> createTask() {
        return new Task<LoginResult>() {
            @Override
            protected LoginResult call() throws Exception {
                return adminLogin();
            }
        };
    }

    private LoginResult adminLogin() {
        try {
            return AdminMiddleware.login(this.adminName, this.adminPassword);
        } catch (SQLException e) {
            return LoginResult.FAILED_BY_NETWORK;
        }
    }
}
