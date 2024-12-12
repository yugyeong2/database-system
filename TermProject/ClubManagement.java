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

    private static void findClub(Connection con) {
        String query = "SELECT * FROM Club";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nClubs:");
            while (rs.next()) {
                System.out.println(rs.getInt("ClubID") + " | " +
                                   rs.getString("ClubName") + " | " +
                                   rs.getDate("EstablishedDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
