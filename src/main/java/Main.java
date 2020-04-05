import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {

        try {
            Connection db = DriverManager.getConnection(
                    "jdbc:h2://C:/Users/431fl/Downloads/Bank/database","sa", "");

            Menu.startMenu(db);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}