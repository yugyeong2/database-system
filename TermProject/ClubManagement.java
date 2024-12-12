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

    private static void insertClub(Connection con, Scanner scanner) {
        System.out.print("Enter Club Name: ");
        String clubName = scanner.nextLine();
        System.out.print("Enter Established Date (YYYY-MM-DD): ");
        String establishedDate = scanner.nextLine();

        String query = "INSERT INTO Club (ClubName, EstablishedDate) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, clubName);
            pstmt.setDate(2, Date.valueOf(establishedDate));
            pstmt.executeUpdate();
            System.out.println("Club added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
