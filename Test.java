import java.sql.*;

public class Test {
    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                        "jdbc:mysql://192.168.56.104:4567/madang",
                            "yugyeongpark","1234");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Book");

            while(rs.next())
                System.out.println(rs.getInt(1) + " " + rs.getString(2) +
                                                            " " + rs.getString(3));

            con.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
