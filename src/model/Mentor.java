package model;

/** A mentor who reviews and scores student progress. Inherits from User. */
public class Mentor extends User {
    private String employeeId;
    private String department;
    private String designation;

    public Mentor(String userId, String name, String email, String password,
                  String employeeId, String department, String designation) {
        super(userId, name, email, password, "MENTOR");
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
    }

    @Override
    public void viewDashboard() {
        System.out.println("Opening Mentor Dashboard for: " + getName()
                + " (" + designation + ")");
    }

    public String getEmployeeId() { return employeeId; }

    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }

    public void setDesignation(String designation) { this.designation = designation; }

    @Override
    public String toString() {
        return "Mentor{userId='" + getUserId() + "', name='" + getName()
                + "', email='" + getEmail() + "', employeeId='" + employeeId
                + "', department='" + department
                + "', designation='" + designation + "'}";
    }
}
