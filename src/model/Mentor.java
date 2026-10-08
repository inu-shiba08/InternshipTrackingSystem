package model;

public class Mentor extends User {
    public Mentor(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void viewDashboard() {
        System.out.println("Mentor Dashboard");
    }
}
