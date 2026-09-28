# 🎓 Course Registration System (CRS)

A JavaFX desktop application that automates course registration at an educational institution. Students manage their courses and academic records, and administrators manage courses, students, enrollments and reports, all backed by a MySQL database.

![Java](https://img.shields.io/badge/Java_17-E75480?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-B784B7?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

---

## ✨ Features

- **Course Management**: Maintain comprehensive details of all courses, including course ID, title, credit hours, department, prerequisites, and maximum enrollment capacity.
- **Student Management**: Manage student profiles, including student ID, name, date of birth, program of study, year, and contact information.
- **Enrollment Management**: Enable students to register for courses based on eligibility and course capacity. Allow students to add or drop courses during a designated period.
- **Academic Records**: Keep a record of all courses a student has enrolled in, including grades received.
- **Reporting Tools**: Generate reports on course enrollments, vacancies, and student schedules.
- **User Authentication**: Role-based access control (RBAC) for students and administrative staff.

## 🛠️ Tech Stack

| Part | Technology |
|---|---|
| Language | Java 17 |
| User interface | JavaFX + FXML (designed with Scene Builder) |
| Database | MySQL 8 via JDBC (MySQL Connector/J) |
| Architecture | MVC: `models/`, `view/`, `controller/`, plus a DAO layer |

## 📁 Project Structure

```plaintext
src/
├── Main.java        # App entry point (opens the login screen)
├── controller/      # JavaFX controllers for each screen and dialog
├── db/              # DBConnection (MySQL connection settings)
├── models/          # Student, Course, Enrollment, AcademicRecord, DAO classes
└── view/            # FXML screens and background images
lib/                 # JavaFX and MySQL Connector/J jars
database_dump.sql    # Database schema with sample data
screenshots/         # App screenshots used below
```

## 🗄️ Database

The dump creates these tables: `admins`, `students`, `courses`, `course_registrations`, `enrollments`, `student_courses` and `student_academic_records`.

## 🚀 How to Run

**Requirements:** JDK 17+, MySQL 8+, and an IDE such as IntelliJ IDEA or Eclipse.

1. Clone the repository:
   ```bash
   git clone https://github.com/Radun111/coursework.git
   ```
2. Create the database and load the sample data:
   ```bash
   mysql -u root -p -e "CREATE DATABASE coursework_crs"
   mysql -u root -p coursework_crs < database_dump.sql
   ```
3. Open `src/db/DBConnection.java` and set your own MySQL username and password.
4. In your IDE, add every `.jar` in the `lib/` folder to the project libraries.
5. Add these VM options to the run configuration so JavaFX loads:
   ```text
   --module-path lib --add-modules javafx.controls,javafx.fxml
   ```
6. Run `Main.java`.

## 📖 User Guide

### For Students

1. **Log In**: Open the application and log in using your student email and password.
2. **View Enrolled Courses**: Open **View Courses** to see the courses you are enrolled in.
3. **Register for Courses**: Open **Register Courses**, search for available courses and register based on eligibility and availability.
4. **View Academic Records**: Check your grades for completed courses in **Academic Records**.

### For Administrators

1. **Log In**: Open the application and log in using your admin credentials.
2. **Manage Courses**: Add, update, or delete courses in **Manage Courses**.
3. **Manage Students**: Add, update, or delete student profiles in **Manage Students**.
4. **Manage Enrollments**: Approve or reject course registrations in **Enrollment Management**.
5. **Generate Reports**: Generate reports on course enrollments, vacancies, and student schedules in **Reports**.

## 🖼️ Screenshots

### Students

#### Login Page
![Login Page](/screenshots/login_page.png)

#### Student Dashboard
![Student Dashboard](/screenshots/student_dashboard.png)

#### Student Profile
![Student Profile](/screenshots/student_profile.png)

#### View Courses
![Student View Courses](/screenshots/student_viewcourses.png)

#### Register for Courses
![Student Register for Courses, step 1](/screenshots/student_registerforcourses1.png)
![Student Register for Courses, step 2](/screenshots/student_registerforcourses2.png)

#### Academic Records
![Student Academic Records](/screenshots/student_viewacademicrecordes.png)

### Admin

#### Admin Dashboard
![Admin Dashboard](/screenshots/admin_dashboard.png)

#### Manage Courses
![Admin Manage Courses](/screenshots/admin_managecourses.png)

#### Manage Students
![Admin Manage Students](/screenshots/admin_managestudents.png)

#### Enrollment Management
![Admin Enrollment Management 1](/screenshots/admin_enrollmentmanagement1.png)
![Admin Enrollment Management 2](/screenshots/admin_enrollmentmanagement2.png)
![Admin Enrollment Management 3](/screenshots/admin_enrollmentmanagement3.png)
![Admin Enrollment Management 4](/screenshots/admin_enrollmentmanagement4.png)

#### Academic Records
![Admin Academic Records](/screenshots/admin_academicrecordes.png)

#### Reports
![Admin Reports](/screenshots/admin_reports.png)

## 👩‍💻 Author

**Raduni Thesanya** · [GitHub](https://github.com/Radun111) · [LinkedIn](https://www.linkedin.com/in/raduni-thesanya/)
