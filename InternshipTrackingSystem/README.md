# Internship Tracking and Evaluation System - Stage 2 UI

**Course:** Java OOP PBL | CCSE0355

## Purpose
This is the Java Swing user-interface module (login screen and three role dashboards)
of the Internship Tracking and Evaluation System. It is a **UI prototype**: it uses
fictional, hard-coded demo data only. There is no database, JDBC, DAO or service
code, so it can later be connected to the project's model/service classes.

## Requirements
- JDK 17 or newer (developed and compiled with JDK 21). A JDK is required, not just a JRE.
- No external libraries. Only the Java standard library (Swing) is used.

## Project structure
```
InternshipTrackingSystem/
├── src/
│   └── ui/
│       ├── Main.java
│       ├── LoginUI.java
│       ├── StudentDashboard.java
│       ├── MentorDashboard.java
│       └── CoordinatorDashboard.java
├── README.md
└── .gitignore
```

## Open in VS Code
1. Install the **Extension Pack for Java** (Microsoft) if you do not have it.
2. Unzip the project, then use **File > Open Folder...** and select the `InternshipTrackingSystem` folder.
3. Open `src/ui/Main.java` and click **Run** above the `main` method (or press F5).

## Compile and run from a terminal
Run these commands from inside the `InternshipTrackingSystem` folder.

Compile (the output folder `out` is created automatically):
```
javac -d out -sourcepath src src/ui/Main.java
```

Run:
```
java -cp out ui.Main
```

## Demo login information
All accounts are fictional. Choose the matching role in the "Login as" box.

| Role        | Username / Email        | Password   |
|-------------|-------------------------|------------|
| Student     | aditi@example.com       | student123 |
| Mentor      | mehta@example.com       | mentor123  |
| Coordinator | coordinator@example.com | coord123   |

## Classes
- **Main** - starts the application at the login screen.
- **LoginUI** - login window with username/email, password and role selection. It checks for empty fields,
  checks the demo accounts, and opens the dashboard that matches the selected role.
- **StudentDashboard** - Register Internship form, Submit Progress form, and a View Evaluation/Status screen
  (internship details, submitted progress table, mentor evaluation). Includes Back to Dashboard buttons and Logout.
- **MentorDashboard** - View Students table, View Progress Reports (table with details panel), and an
  Evaluate Student form that validates the score (whole number 0-10) and remarks. Includes Back to Dashboard buttons and Logout.
- **CoordinatorDashboard** - View Internships/Students table, View Status table with a status filter,
  and a Generate/View Report screen with a sample report. Includes Back to Dashboard buttons and Logout.

## Navigation
`Main` -> `LoginUI` -> `StudentDashboard` / `MentorDashboard` / `CoordinatorDashboard` -> Logout -> `LoginUI`

## Notes for integration
- Replace the demo arrays and the hard-coded accounts (`LoginUI.isValidDemoLogin`) with calls to the
  project's model/service classes.
- The "Submit" buttons currently only validate input and show a confirmation. They do not save anything.
