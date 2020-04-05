package operations;

import enteties.Account;
import enteties.Operation;
import enteties.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {

    //    внести пользователя в базу
    public static void signUp(Connection db, String sql, User user) {

        try(PreparedStatement preparedStatement = db.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getAddres());
            preparedStatement.setString(4, user.getPhone());

            preparedStatement.execute();
            System.out.println("Регистрация завершена!");
        }
        catch (SQLException e) {
            System.out.println("Такой логин или телефон уже существует!");
        }
    }

    //взять данные пользователя из базы
    public static User signIn(Connection db, String sql, User user) {

        User newUser = null;
        try(PreparedStatement preparedStatement = db.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            ResultSet result = preparedStatement.executeQuery();

            if (result.next())
                newUser = new User(result.getString("id"), result.getString("login"),
                        result.getString("password"), result.getString("address"),
                        result.getString("phone"));
            else throw new SQLException();

        }
        catch (SQLException e) {
            System.out.println("Пользователь не найден!");
        }

        return newUser;
    }

    // найти пользователя
    public static User findUser(Connection db, String phone) {

        String sql = "SELECT * from USER where phone=?";
        User user = null;
        try(PreparedStatement preparedStatement = db.prepareStatement(sql)) {
            preparedStatement.setString(1, phone);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next())
                user = new User(result.getString("id"), result.getString("login"),
                        result.getString("password"), result.getString("address"),
                        result.getString("phone"));
            else throw new SQLException();
        }
        catch (SQLException e) {
            System.out.println("Пользователь не найден!");
        }
        return user;
    }

//    создании счёта
    public static Account createAccount(Connection db, String sql, Account account) {

        try(PreparedStatement preparedStatement = db.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getUuid());
            preparedStatement.setString(2, account.getClientId());
            preparedStatement.setString(3, account.getAccCode());

            preparedStatement.execute();
            System.out.println("Аккаунт создан!");
        }
        catch (SQLException e) {
            return null;
        }
        return account;
    }

    public static List<Account> loadAccounts(Connection db, String id) {

        String sql = "SELECT * from ACCOUNT where Client_Id=?";
        List<Account> userAccounts = new ArrayList<>();

        try (PreparedStatement preparedStatement = db.prepareStatement(sql)) {

            preparedStatement.setString(1, id);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                Account account = new Account(result.getString("ID")
                        , result.getString("CLIENT_ID")
                        , new BigDecimal(result.getString("AMOUNT"))
                        , result.getString("ACC_CODE"));
                userAccounts.add(account);
            }
            if (userAccounts.size() == 0) return new ArrayList<>();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return userAccounts;
    }

//    обновление счёта
    public static void updateBalance(Connection db, Account account) {

        String sql = "SELECT * from ACCOUNT where Id=? and Client_id=?";
        try (PreparedStatement preparedStatement =
                     db.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

            preparedStatement.setString(1, account.getUuid());
            preparedStatement.setString(2, account.getClientId());
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                result.updateBigDecimal("Amount", account.getAmount());
                result.updateRow();
            }
            else throw new SQLException();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showHistory(Connection db, String userId) {
        String sql = "SELECT * from OPERATION where id=?"; // user id

        System.out.printf("\n%-15s %-21s %-12s %-41s %-41s %-32s %-41s\n\n"
                , "Дата:", "Сумма:", "Валюта:"
                , "Счёт с которого перевели:", "Счёт на который перевели:"
                , "Кол-во средств до перевода:", "Кол-во средств после перевода:");
        try (PreparedStatement preparedStatement = db.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {

                String newDate = Operation.formatDate(db, result.getDate("Date_Operation"));
                String sum = result.getString("Sum");
                String accCode = result.getString("Acc_Code");
                String fromAcc = result.getString("From_Acc");
                String toAcc = result.getString("To_Acc");
                String balanceBefore = result.getString("Balance_Before");
                String balanceAfter = result.getString("Balance_After");

                System.out.printf("%-15s %-21s %-12s %-41s %-41s %-32s %-41s\n"
                        , newDate, sum, accCode
                        , fromAcc, toAcc
                        , balanceBefore, balanceAfter);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addOperation(Connection db, Operation op) {
        String sql = "INSERT into OPERATION values(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = db.prepareStatement(sql)) {

            preparedStatement.setString(1, op.getId());
            preparedStatement.setString(2, op.getDate());
            preparedStatement.setString(3, op.getSum());
            preparedStatement.setString(4, op.getAccCode());
            preparedStatement.setString(5, op.getFromAcc());
            preparedStatement.setString(6, op.getToAcc());
            preparedStatement.setString(7, op.getBalBefore());
            preparedStatement.setString(8, op.getBalAfter());

            preparedStatement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

