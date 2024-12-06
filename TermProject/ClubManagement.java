import java.sql.*;
import java.util.Scanner;

public class ClubManagement {
    public static void main(String[] args) {
        String url = "jdbc:mysql://<IP>:<PORT>/ClubManagement";
        String user = "<USERNAME>";
        String password = "<PASSWORD>";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            Class.forName("com.mysql.cj.jdbc.Driver");
            boolean exit = false;

            while (!exit) {
                System.out.println("\nMenu:");
                System.out.println("1. Connect to Database");
                System.out.println("2. Find Club Information");
                System.out.println("3. Insert New Club");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> System.out.println("Connection Successful.");
                    case 2 -> findClub(con);
                    case 3 -> insertClub(con, scanner);
                    case 4 -> exit = true;
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
