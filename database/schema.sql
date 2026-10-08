-- Internship Tracking and Evaluation System
-- Group 28 | Om Tandon - Database / JDBC / DAO
--
-- Target: MySQL 8+
-- Create the database once, then execute this file.
-- No passwords or other secrets are stored here.

CREATE DATABASE IF NOT EXISTS internship_tracking;
USE internship_tracking;

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT chk_user_role
        CHECK (role IN ('STUDENT', 'MENTOR', 'COORDINATOR'))
);

CREATE TABLE IF NOT EXISTS student_details (
    user_id VARCHAR(50) PRIMARY KEY,
    roll_number VARCHAR(50),
    department VARCHAR(120),
    current_internship_id VARCHAR(50),
    CONSTRAINT fk_student_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mentor_details (
    user_id VARCHAR(50) PRIMARY KEY,
    employee_id VARCHAR(50),
    department VARCHAR(120),
    designation VARCHAR(120),
    CONSTRAINT fk_mentor_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS coordinator_details (
    user_id VARCHAR(50) PRIMARY KEY,
    employee_id VARCHAR(50),
    office_location VARCHAR(160),
    CONSTRAINT fk_coordinator_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS internships (
    internship_id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL,
    company_name VARCHAR(180) NOT NULL,
    role VARCHAR(160) NOT NULL,
    duration_weeks INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED',
    CONSTRAINT chk_duration
        CHECK (duration_weeks > 0),
    CONSTRAINT fk_internship_student
        FOREIGN KEY (student_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS progress_reports (
    report_id VARCHAR(50) PRIMARY KEY,
    internship_id VARCHAR(50) NOT NULL,
    week_number INT NOT NULL,
    work_description TEXT NOT NULL,
    submission_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
    CONSTRAINT chk_week
        CHECK (week_number > 0),
    CONSTRAINT uq_internship_week
        UNIQUE (internship_id, week_number),
    CONSTRAINT fk_progress_internship
        FOREIGN KEY (internship_id) REFERENCES internships(internship_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS evaluations (
    evaluation_id VARCHAR(50) PRIMARY KEY,
    report_id VARCHAR(50) NOT NULL,
    mentor_id VARCHAR(50) NOT NULL,
    score DECIMAL(4,2) NOT NULL,
    remarks TEXT,
    evaluation_date DATE NOT NULL,
    CONSTRAINT chk_score
        CHECK (score >= 0 AND score <= 10),
    CONSTRAINT fk_evaluation_report
        FOREIGN KEY (report_id) REFERENCES progress_reports(report_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_evaluation_mentor
        FOREIGN KEY (mentor_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE OR REPLACE VIEW user_details AS
SELECT
    u.user_id,
    u.name,
    u.email,
    u.password,
    u.role,
    sd.roll_number,
    COALESCE(sd.department, md.department) AS department,
    COALESCE(sd.current_internship_id, NULL) AS current_internship_id,
    COALESCE(md.employee_id, cd.employee_id) AS employee_id,
    md.designation,
    cd.office_location
FROM users u
LEFT JOIN student_details sd ON sd.user_id = u.user_id
LEFT JOIN mentor_details md ON md.user_id = u.user_id
LEFT JOIN coordinator_details cd ON cd.user_id = u.user_id;
