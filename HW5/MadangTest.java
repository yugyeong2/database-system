import java.sql.*;
import java.util.Scanner;

public class MadangTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.56.104:4567/madang";
        String user = "yugyeongpark";
        String password = "1234";

        try (Connection con = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in)) {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            boolean exit = false;

            while (!exit) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Search Books");
                System.out.println("2. Insert a New Book");
                System.out.println("3. Delete a Book");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> searchBooks(con);         // 검색
                    case 2 -> insertBook(con, scanner); // 삽입
                    case 3 -> deleteBook(con, scanner); // 삭제
                    case 4 -> exit = true;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 데이터 검색
    private static void searchBooks(Connection con) {
        String query = "SELECT * FROM Book";
        try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nBook List:");
            while (rs.next()) {
                System.out.println(rs.getInt("bookid") + " | " +
                                    rs.getString("bookname") + " | " +
                                    rs.getString("publisher") + " | " +
                                    rs.getInt("price"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }

    // 데이터 삽입
    private static void insertBook(Connection con, Scanner scanner) {
        System.out.print("Enter book ID: ");
        int bookid = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter book name: ");
        String bookname = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter price: ");
        int price = scanner.nextInt();

        String query = "INSERT INTO Book (bookid, bookname, publisher, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, bookid);
            pstmt.setString(2, bookname);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, price);
            pstmt.executeUpdate();
            System.out.println("Book inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting book: " + e.getMessage());
        }
    }

    private static void deleteBook(Connection con, Scanner scanner) {
        System.out.print("Enter the ID of the book to delete: ");
        int bookid = scanner.nextInt();

        String query = "DELETE FROM Book WHERE bookid = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, bookid);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book with ID " + bookid + " does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }
}
