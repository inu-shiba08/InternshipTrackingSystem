package model;

/** A student doing an internship. Inherits from User. */
public class Student extends User {
    private String rollNumber;
    private String department;
    private String currentInternshipId;

    public Student(String userId, String name, String email, String password,
                   String rollNumber, String department) {
        super(userId, name, email, password, "STUDENT");
        this.rollNumber = rollNumber;
        this.department = department;
    }

    @Override
    public void viewDashboard() {
        System.out.println("Opening Student Dashboard for: " + getName()
                + " (Roll No: " + rollNumber + ")");
    }

    public String getRollNumber() { return rollNumber; }

    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getCurrentInternshipId() { return currentInternshipId; }

    public void setCurrentInternshipId(String currentInternshipId) {
        this.currentInternshipId = currentInternshipId;
    }

    @Override
    public String toString() {
        return "Student{userId='" + getUserId() + "', name='" + getName()
                + "', email='" + getEmail() + "', rollNumber='" + rollNumber
                + "', department='" + department
                + "', currentInternshipId='" + currentInternshipId + "'}";
    }
}
