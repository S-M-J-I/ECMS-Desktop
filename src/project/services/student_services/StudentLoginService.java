package project.services.student_services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import project.enums.LoginResult;
import project.middlewares.StudentMiddleware;

import java.sql.SQLException;

public class StudentLoginService extends Service<LoginResult> {

    private String username;
    private String password;

    public StudentLoginService() {
    }


    public StudentLoginService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected Task<LoginResult> createTask() {
        return new Task<LoginResult>() {
            @Override
            protected LoginResult call() throws Exception {
                return studentLogin();
            }
        };
    }

    private LoginResult studentLogin() throws SQLException {
        return StudentMiddleware.loginStudent(this.username, this.password);
    }
}
