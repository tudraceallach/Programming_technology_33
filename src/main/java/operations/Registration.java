package operations;

import enteties.User;
import java.sql.Connection;
import java.util.Scanner;

public class Registration {

    public static void signUp(Connection db) {

        Scanner scan = new Scanner(System.in);

        System.out.print("Логин: ");
        String login = scan.nextLine();

        System.out.print("Пароль: ");
        String passw = scan.nextLine();

        System.out.print("Адрес: ");
        String address = scan.nextLine();

        System.out.print("Телефон: +7 ");
        String phone = scan.nextLine();

        String sql = "INSERT into USER values(default,?,?,?,?)";

        Database.signUp(db, sql, new User("", login, passw, address, phone));
    }
}