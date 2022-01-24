package project.services.cf_services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import project.enums.LoginResult;
import project.middlewares.CFMiddleware;

import java.sql.SQLException;

public class CFLoginServices extends Service<LoginResult> {

    private int forumID;
    private String name;
    private String username;
    private String password;
    private String enrolPassword;
    private boolean isRecruitmentOn;

    public CFLoginServices(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CFLoginServices(int forumID, String name, String username, String password, String enrolPassword, boolean isRecruitmentOn) {
        this.forumID = forumID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.enrolPassword = enrolPassword;
        this.isRecruitmentOn = isRecruitmentOn;
    }

    @Override
    protected Task<LoginResult> createTask() {
        return new Task<LoginResult>() {
            @Override
            protected LoginResult call() throws Exception {
                return loginCF();
            }
        };
    }

    private LoginResult loginCF() {
        try {
            return CFMiddleware.loginCF(this.username, this.password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return LoginResult.FAILED_BY_NETWORK;
        }
    }
}
