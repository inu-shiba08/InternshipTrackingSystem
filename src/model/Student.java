package model;

public class Student extends User {
    public Student(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void viewDashboard() {
        System.out.println("Student Dashboard");
    }
}
