package operations;

import enteties.User;
import java.sql.Connection;
import java.util.Scanner;

public class Authorization {

    public static User signIn(Connection db) {

        System.out.print("\nВведите логин или телефон для входа:\n" +
                "1. Ввести логин\n2. Ввести телефон\n0. Назад\n\nВыбор: ");
        String num = new Scanner(System.in).nextLine();

        switch (num) {
            case "1":
                System.out.print("\nЛогин: ");
                break;
            case "2":
                System.out.print("\nТелефон: +7 ");
                break;
            default:
                return null;
        }
        String data = new Scanner(System.in).nextLine();

        System.out.print("Пароль: ");
        String password = new Scanner(System.in).nextLine();

        String sql;

        if (num.equals("1")) sql = "SELECT * from USER where login=? and password=?";
        else sql = "SELECT * from USER where phone=? and password=?";

        return Database.signIn(db, sql, new User("", data, password, "", ""));
    }
}