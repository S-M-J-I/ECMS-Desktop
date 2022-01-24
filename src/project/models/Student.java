package project.models;

public class Student {
    private int member_id;
    private String UIU_id;
    private String name;
    private String username;
    private String password;
    private String department;
    private String email;
    private String phone;

    public Student() {}

    public Student(int member_id, String UIU_id, String name, String username, String password, String department, String email, String phone) {
        this.member_id = member_id;
        this.UIU_id = UIU_id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public String getUIU_id() {
        return UIU_id;
    }

    public void setUIU_id(String UIU_id) {
        this.UIU_id = UIU_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getUsername() {
        return username;
    }
}
