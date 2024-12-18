import java.sql.*;
import java.util.Scanner;

public class ClubManagement {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.56.104:4567/madang";
        String user = "yugyeongpark";
        String password = "1234";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            Class.forName("com.mysql.cj.jdbc.Driver");
            boolean exit = false;

            while (!exit) {
                System.out.println("\nMenu:");
                System.out.println("1. Init Database");
                System.out.println("2. Connect to Database");

                System.out.println("3. Find Student");
                System.out.println("4. Find Professor");
                System.out.println("5. Find Club");

                System.out.println("6. Insert Student");
                System.out.println("7. Insert Professor");
                System.out.println("8. Insert Club");
                System.out.println("9. Insert Student to Club");
                System.out.println("10. Insert Project");

                System.out.println("11. Update Student");
                System.out.println("12. Update Professor");
                System.out.println("13. Update Club");
                System.out.println("14. Update Project");

                System.out.println("15. Delete Student");
                System.out.println("16. Delete Professor");
                System.out.println("17. Delete Club");
                System.out.println("18. Delete Project");

                System.out.println("19. Matching Project with Student");

                System.out.println("20. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> initDatabase(con);
                    case 2 -> System.out.println("Database connected.");

                    case 3 -> findStudent(con, scanner);
                    case 4 -> findProfessor(con, scanner);
                    case 5 -> findClub(con, scanner);

                    case 6 -> insertStudent(con, scanner);
                    case 7 -> insertProfessor(con, scanner);
                    case 8 -> insertClub(con, scanner);
                    case 9 -> insertStudentToClub(con, scanner);
                    case 10 -> insertProject(con, scanner);

                    case 11 -> updateStudent(con, scanner);
                    case 12 -> updateProfessor(con, scanner);
                    case 13 -> updateClub(con, scanner);
                    case 14 -> updateProject(con, scanner);

                    case 15 -> deleteStudent(con, scanner);
                    case 16 -> deleteProfessor(con, scanner);
                    case 17 -> deleteClub(con, scanner);
                    case 18 -> deleteProject(con, scanner);

                    case 19 -> matchProjectWithStudent(con, scanner);

                    case 20 -> exit = true;
                    default -> System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initDatabase(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTables = """
                CREATE TABLE IF NOT EXISTS User (
                    UserID INT AUTO_INCREMENT PRIMARY KEY,
                    Name VARCHAR(100) NOT NULL,
                    Age INT NOT NULL,
                    Gender ENUM('Male', 'Female', 'Other') NOT NULL,
                    Email VARCHAR(100) UNIQUE NOT NULL,
                    Password VARCHAR(255) NOT NULL,
                    PhoneNumber VARCHAR(15)
                );
                CREATE TABLE IF NOT EXISTS Club (
                    ClubID INT AUTO_INCREMENT PRIMARY KEY,
                    ClubName VARCHAR(100) NOT NULL,
                    ClubRoom VARCHAR(100)
                );
                CREATE TABLE IF NOT EXISTS Student (
                    StudentID INT AUTO_INCREMENT PRIMARY KEY,
                    UserID INT UNIQUE NOT NULL,
                    ClubID INT,
                    Status ENUM('Active', 'Inactive') NOT NULL,
                    Major VARCHAR(100),
                    Semester INT,
                    GradeAverage DECIMAL(3, 2),
                    FOREIGN KEY (UserID) REFERENCES User(UserID),
                    FOREIGN KEY (ClubID) REFERENCES Club(ClubID)
                );
                CREATE TABLE IF NOT EXISTS Professor (
                    ProfessorID INT AUTO_INCREMENT PRIMARY KEY,
                    UserID INT UNIQUE NOT NULL,
                    ResearchField VARCHAR(100),
                    Office VARCHAR(100),
                    FOREIGN KEY (UserID) REFERENCES User(UserID)
                );
                CREATE TABLE IF NOT EXISTS ClubAdvisors (
                    ClubID INT PRIMARY KEY,
                    ProfessorID INT UNIQUE,
                    FOREIGN KEY (ClubID) REFERENCES Club(ClubID),
                    FOREIGN KEY (ProfessorID) REFERENCES Professor(ProfessorID)
                );
                CREATE TABLE IF NOT EXISTS Project (
                    ProjectID INT AUTO_INCREMENT PRIMARY KEY,
                    ProjectName VARCHAR(100) NOT NULL,
                    GitHub VARCHAR(255),
                    Deadline DATE,
                    Status ENUM('Planned', 'Ongoing', 'Completed')
                );
                CREATE TABLE IF NOT EXISTS ClubProjects (
                    ClubID INT NOT NULL,
                    ProjectID INT NOT NULL,
                    PRIMARY KEY (ClubID, ProjectID),
                    FOREIGN KEY (ClubID) REFERENCES Club(ClubID),
                    FOREIGN KEY (ProjectID) REFERENCES Project(ProjectID)
                );
                CREATE TABLE IF NOT EXISTS Skill (
                    SkillID INT AUTO_INCREMENT PRIMARY KEY,
                    Field VARCHAR(100) NOT NULL,
                    ProgrammingLanguage VARCHAR(100) NOT NULL
                );
                CREATE TABLE IF NOT EXISTS StudentSkills (
                    StudentID INT NOT NULL,
                    SkillID INT NOT NULL,
                    PRIMARY KEY (StudentID, SkillID),
                    FOREIGN KEY (StudentID) REFERENCES Student(StudentID),
                    FOREIGN KEY (SkillID) REFERENCES Skill(SkillID)
                );
                CREATE TABLE IF NOT EXISTS ProjectSkills (
                    ProjectID INT NOT NULL,
                    SkillID INT NOT NULL,
                    PRIMARY KEY (ProjectID, SkillID),
                    FOREIGN KEY (ProjectID) REFERENCES Project(ProjectID),
                    FOREIGN KEY (SkillID) REFERENCES Skill(SkillID)
                );
            """;
            stmt.execute(createTables);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void findStudent(Connection con, Scanner scanner) {
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();
        String query = "SELECT * FROM Student WHERE Name = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, studentName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.printf("StudentID: %d, Name: %s, Age: %d, Major: %s, Status: %s\n",
                        rs.getInt("StudentID"), rs.getString("Name"), rs.getInt("Age"),
                        rs.getString("Major"), rs.getString("Status"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding student: " + e.getMessage());
        }
    }

    public static void findProfessor(Connection con, Scanner scanner) {
        System.out.print("Enter professor name: ");
        String professorName = scanner.nextLine();
        String query = "SELECT * FROM Professor WHERE Name = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, professorName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.printf("ProfessorID: %d, Name: %s, ResearchField: %s, Office: %s\n",
                        rs.getInt("ProfessorID"), rs.getString("Name"), rs.getString("ResearchField"),
                        rs.getString("Office"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding professor: " + e.getMessage());
        }
    }

    public static void findClub(Connection con, Scanner scanner) {
        System.out.print("Enter club name: ");
        String clubName = scanner.nextLine();
        String query = "SELECT * FROM Club WHERE ClubName = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, clubName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.printf("ClubID: %d, ClubName: %s, ClubRoom: %s\n",
                        rs.getInt("ClubID"), rs.getString("ClubName"), rs.getString("ClubRoom"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding club: " + e.getMessage());
        }
    }

    public static void insertStudent(Connection con, Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter major: ");
        String major = scanner.nextLine();
        System.out.print("Enter semester: ");
        int semester = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter grade average: ");
        double gradeAverage = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String query = "INSERT INTO Student (Name, Age, Gender, Major, Semester, GradeAverage) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, major);
            pstmt.setInt(5, semester);
            pstmt.setDouble(6, gradeAverage);
            pstmt.executeUpdate();
            System.out.println("Student inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting student: " + e.getMessage());
        }
    }

    public static void insertProfessor(Connection con, Scanner scanner) {
        System.out.print("Enter professor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Enter research field: ");
        String researchField = scanner.nextLine();
        System.out.print("Enter office: ");
        String office = scanner.nextLine();

        String query = "INSERT INTO Professor (Name, Age, Gender, ResearchField, Office) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, researchField);
            pstmt.setString(5, office);
            pstmt.executeUpdate();
            System.out.println("Professor inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting professor: " + e.getMessage());
        }
    }

    public static void insertClub(Connection con, Scanner scanner) {
        System.out.print("Enter club name: ");
        String clubName = scanner.nextLine();
        System.out.print("Enter club room: ");
        String clubRoom = scanner.nextLine();

        String query = "INSERT INTO Club (ClubName, ClubRoom) VALUES (?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, clubName);
            pstmt.setString(2, clubRoom);
            pstmt.executeUpdate();
            System.out.println("Club inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting club: " + e.getMessage());
        }
    }

    public static void insertStudentToClub(Connection con, Scanner scanner) {
        System.out.print("Enter student ID: ");
        int studentID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter club ID: ");
        int clubID = scanner.nextInt();
        scanner.nextLine();

        String query = "UPDATE Student SET ClubID = ? WHERE StudentID = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, clubID);
            pstmt.setInt(2, studentID);
            pstmt.executeUpdate();
            System.out.println("Student assigned to club successfully.");
        } catch (SQLException e) {
            System.out.println("Error assigning student to club: " + e.getMessage());
        }
    }

    public static void insertProject(Connection con, Scanner scanner) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();
        System.out.print("Enter GitHub URL: ");
        String gitHubURL = scanner.nextLine();
        System.out.print("Enter deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine();
        System.out.print("Enter status (Planned/Ongoing/Completed): ");
        String status = scanner.nextLine();

        String query = "INSERT INTO Project (ProjectName, GitHub, Deadline, Status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, projectName);
            pstmt.setString(2, gitHubURL);
            pstmt.setDate(3, Date.valueOf(deadline));
            pstmt.setString(4, status);
            pstmt.executeUpdate();
            System.out.println("Project inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting project: " + e.getMessage());
        }
    }

    private static void addStudentToProject(Connection con, int projectID, int studentID) {
        String query = "INSERT INTO StudentProjects (ProjectID, StudentID) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, projectID);
            pstmt.setInt(2, studentID);
            pstmt.executeUpdate();
            System.out.println("Student added to project successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding student to project: " + e.getMessage());
        }
    }

    public static void deleteStudent(Connection con, Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        int studentID = scanner.nextInt();
        scanner.nextLine();

        String query = "DELETE FROM Student WHERE StudentID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
            System.out.println("Student deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    public static void deleteProfessor(Connection con, Scanner scanner) {
        System.out.print("Enter professor ID to delete: ");
        int professorID = scanner.nextInt();
        scanner.nextLine();

        String query = "DELETE FROM Professor WHERE ProfessorID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, professorID);
            pstmt.executeUpdate();
            System.out.println("Professor deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting professor: " + e.getMessage());
        }
    }

    public static void deleteClub(Connection con, Scanner scanner) {
        System.out.print("Enter club ID to delete: ");
        int clubID = scanner.nextInt();
        scanner.nextLine();

        String query = "DELETE FROM Club WHERE ClubID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, clubID);
            pstmt.executeUpdate();
            System.out.println("Club deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting club: " + e.getMessage());
        }
    }

    public static void deleteProject(Connection con, Scanner scanner) {
        System.out.print("Enter project ID to delete: ");
        int projectID = scanner.nextInt();
        scanner.nextLine();

        String query = "DELETE FROM Project WHERE ProjectID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, projectID);
            pstmt.executeUpdate();
            System.out.println("Project deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting project: " + e.getMessage());
        }
    }

    public static void updateStudent(Connection con, Scanner scanner) {
        System.out.print("Enter student ID to update: ");
        int studentID = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter new major (leave blank to skip): ");
        String major = scanner.nextLine();
        System.out.print("Enter new semester (leave blank to skip): ");
        String semesterStr = scanner.nextLine();
        System.out.print("Enter new grade average (leave blank to skip): ");
        String gradeAverageStr = scanner.nextLine();

        String query = "UPDATE Student SET Major = COALESCE(?, Major), Semester = COALESCE(?, Semester), GradeAverage = COALESCE(?, GradeAverage) WHERE StudentID = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, major.isEmpty() ? null : major);
            pstmt.setObject(2, semesterStr.isEmpty() ? null : Integer.parseInt(semesterStr));
            pstmt.setObject(3, gradeAverageStr.isEmpty() ? null : Double.parseDouble(gradeAverageStr));
            pstmt.setInt(4, studentID);
            pstmt.executeUpdate();
            System.out.println("Student information updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    public static void updateProfessor(Connection con, Scanner scanner) {
        System.out.print("Enter professor ID to update: ");
        int professorID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new research field (leave blank to skip): ");
        String researchField = scanner.nextLine();
        System.out.print("Enter new office (leave blank to skip): ");
        String office = scanner.nextLine();

        String query = "UPDATE Professor SET ResearchField = COALESCE(?, ResearchField), Office = COALESCE(?, Office) WHERE ProfessorID = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, researchField.isEmpty() ? null : researchField);
            pstmt.setString(2, office.isEmpty() ? null : office);
            pstmt.setInt(3, professorID);
            pstmt.executeUpdate();
            System.out.println("Professor information updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating professor: " + e.getMessage());
        }
    }

    public static void updateClub(Connection con, Scanner scanner) {
        System.out.print("Enter club ID to update: ");
        int clubID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new club name (leave blank to skip): ");
        String clubName = scanner.nextLine();
        System.out.print("Enter new club room (leave blank to skip): ");
        String clubRoom = scanner.nextLine();

        String query = "UPDATE Club SET ClubName = COALESCE(?, ClubName), ClubRoom = COALESCE(?, ClubRoom) WHERE ClubID = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, clubName.isEmpty() ? null : clubName);
            pstmt.setString(2, clubRoom.isEmpty() ? null : clubRoom);
            pstmt.setInt(3, clubID);
            pstmt.executeUpdate();
            System.out.println("Club information updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating club: " + e.getMessage());
        }
    }

    public static void updateProject(Connection con, Scanner scanner) {
        System.out.print("Enter project ID to update: ");
        int projectID = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter new project name (leave blank to skip): ");
        String projectName = scanner.nextLine();
        System.out.print("Enter new GitHub URL (leave blank to skip): ");
        String gitHubURL = scanner.nextLine();
        System.out.print("Enter new deadline (YYYY-MM-DD, leave blank to skip): ");
        String deadline = scanner.nextLine();
        System.out.print("Enter new status (leave blank to skip): ");
        String status = scanner.nextLine();

        String query = "UPDATE Project SET ProjectName = COALESCE(?, ProjectName), GitHub = COALESCE(?, GitHub), Deadline = COALESCE(?, Deadline), Status = COALESCE(?, Status) WHERE ProjectID = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, projectName.isEmpty() ? null : projectName);
            pstmt.setString(2, gitHubURL.isEmpty() ? null : gitHubURL);
            pstmt.setString(3, deadline.isEmpty() ? null : deadline);
            pstmt.setString(4, status.isEmpty() ? null : status);
            pstmt.setInt(5, projectID);
            pstmt.executeUpdate();
            System.out.println("Project information updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
        }
    }


    public static void matchProjectWithStudent(Connection con, Scanner scanner) {
        System.out.print("Enter project ID: ");
        int projectID = scanner.nextInt();
        scanner.nextLine();

        String projectSkillsQuery = "SELECT SkillID FROM ProjectSkills WHERE ProjectID = ?";
        String matchingStudentsQuery = """
        SELECT DISTINCT s.StudentID, s.Name, s.Major
        FROM Student s
        JOIN StudentSkills ss ON s.StudentID = ss.StudentID
        WHERE ss.SkillID = ?
    """;

        try (PreparedStatement projectStmt = con.prepareStatement(projectSkillsQuery);
             PreparedStatement studentStmt = con.prepareStatement(matchingStudentsQuery)) {

            projectStmt.setInt(1, projectID);
            ResultSet projectSkills = projectStmt.executeQuery();

            while (projectSkills.next()) {
                int skillID = projectSkills.getInt("SkillID");
                studentStmt.setInt(1, skillID);
                ResultSet students = studentStmt.executeQuery();

                System.out.println("Matching students for skill ID " + skillID + ":");
                while (students.next()) {
                    System.out.printf("StudentID: %d, Name: %s, Major: %s%n",
                            students.getInt("StudentID"),
                            students.getString("Name"),
                            students.getString("Major"));
                    System.out.print("Add this student to project? (yes/no): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("yes")) {
                        addStudentToProject(con, projectID, students.getInt("StudentID"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error matching project with students: " + e.getMessage());
        }
    }
}
