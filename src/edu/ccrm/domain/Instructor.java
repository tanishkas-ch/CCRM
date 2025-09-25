package edu.ccrm.domain;
public class Instructor extends Person {
    private String dept;

    public Instructor(String id, String fullName, String email, String dept) {
        super(id, fullName, email);
        this.dept = dept;
    }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    @Override
    public String role() { return "Instructor";}
}