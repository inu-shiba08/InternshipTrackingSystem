package model;

/** The internship coordinator who oversees the program. Inherits from User. */
public class Coordinator extends User {
    private String employeeId;
    private String officeLocation;

    public Coordinator(String userId, String name, String email, String password,
                       String employeeId, String officeLocation) {
        super(userId, name, email, password, "COORDINATOR");
        this.employeeId = employeeId;
        this.officeLocation = officeLocation;
    }

    @Override
    public void viewDashboard() {
        System.out.println("Opening Coordinator Dashboard for: " + getName()
                + " [Office: " + officeLocation + "]");
    }

    public String getEmployeeId() { return employeeId; }

    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getOfficeLocation() { return officeLocation; }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    @Override
    public String toString() {
        return "Coordinator{userId='" + getUserId() + "', name='" + getName()
                + "', email='" + getEmail() + "', employeeId='" + employeeId
                + "', officeLocation='" + officeLocation + "'}";
    }
}
