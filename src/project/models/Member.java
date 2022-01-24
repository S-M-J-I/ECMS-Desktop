package project.models;

import project.middlewares.MemberMiddleware;

import java.util.HashMap;

public class Member {
    private int id;
    private int student_id;
    private String UIUid;
    private String name;
    private String designation;
    private String department;
    private String phone;
    private String email;
    private String cfName;
    private String regFeeRemaining;
    private String reason;

    public Member() {
    }

    public Member(int id, int student_id, String UIUid, String name) {
        this.id = id;
        this.student_id = student_id;
        this.UIUid = UIUid;
        this.name = name;
    }

    public Member(int id, int student_id, String UIUid, String name, String reason) {
        this.id = id;
        this.student_id = student_id;
        this.UIUid = UIUid;
        this.name = name;
        this.reason = reason;
    }

    public Member(String cfName, String designation, String department) {
        this.cfName = cfName;
        this.designation = designation;
        this.department = department;
    }

    public Member(String cfName, String designation, String department, String regFeeRemaining) {
        this.cfName = cfName;
        this.designation = designation;
        this.department = department;
        this.regFeeRemaining = regFeeRemaining;
    }

    public Member(int id, String UIUid, String name, String designation, String department, String phone, String email) {
        this.id = id;
        this.UIUid = UIUid;
        this.name = name;
        this.designation = designation;
        this.department = department;
        this.phone = phone;
        this.email = email;
    }

    public String getCfName() {
        return cfName;
    }

    public String getDesignation() {
        return designation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCfName(String cfName) {
        this.cfName = cfName;
    }

    public String getUIUid() {
        return UIUid;
    }

    public void setUIUid(String UIUid) {
        this.UIUid = UIUid;
    }

    public String getRegFeeRemaining() {
        return regFeeRemaining;
    }

    public void setRegFeeRemaining(String regFeeRemaining) {
        this.regFeeRemaining = regFeeRemaining;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
