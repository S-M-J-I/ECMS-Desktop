package project.services.student_services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import project.enums.SignUpResult;
import project.middlewares.StudentMiddleware;

public class StudentSignupService extends Service<SignUpResult> {

    private String UIU_id;
    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private String department;
    private String email;
    private String phone;

    public StudentSignupService() {}

    public StudentSignupService(String UIU_id, String name, String username, String password, String confirmPassword, String department, String email, String phone) {
        this.UIU_id = UIU_id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }


    @Override
    protected Task<SignUpResult> createTask() {
        return new Task<SignUpResult>() {
            @Override
            protected SignUpResult call() throws Exception {
                return signup();
            }
        };
    }

    private SignUpResult signup() {
        return StudentMiddleware.signup(this.UIU_id, this.name, this.username, this.password, this.confirmPassword, this.department, this.email, this.phone);
    }
}
