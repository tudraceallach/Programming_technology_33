import enteties.Account;
import enteties.User;
import operations.Authorization;
import operations.Database;
import operations.Registration;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Menu {

    public static void startMenu(Connection db) {

        String op = "1";
        while (!op.equals("0")) {
            System.out.print("\n1. Регистрация\n2. Войти\n0. Выйти\n\nВыбор: ");
            op = new Scanner(System.in).nextLine();
            if (op.equals("1")) {
                Registration.signUp(db);
            }
            else if(op.equals("2")) {
                User user = Authorization.signIn(db);
                if (user != null) Menu.userMenu(db, user);
            }
        }
    }

    public static void userMenu(Connection db, User user) {
        String op = "1";
        // загружаем из базы
        List<Account> userAccounts = Database.loadAccounts(db, user.getId());

        while (!op.equals("0")) {
            System.out.print("\nЧто Вы хотите сделать?\n\n" +
                    "1. Создать аккаунт\n" +
                    "2. Пополнить счёт\n" +
                    "3. Показать аккаунты\n" +
                    "4. Перевести средства другому человеку\n" +
                    "5. История операций\n" +
                    "0. Выйти\n\nВыбор: ");
            op = new Scanner(System.in).nextLine();

            switch (op) {
                case "1":
                    Account acc = Account.createAccount(db, user.getId());
                    if (acc != null) userAccounts.add(acc);
                    break;
                case "2":
                    Account.Refill(db, userAccounts);
                    break;
                case "3":
                    System.out.print("\nВаши аккаунты:\n");
                    Account.showAccounts(userAccounts);
                    break;
                case "4":
                    Account.sendMoney(db, user.getPhone(), userAccounts);
                    break;
                case "5":
                    Database.showHistory(db, user.getId());
                    break;
            }
        }
    }

}
