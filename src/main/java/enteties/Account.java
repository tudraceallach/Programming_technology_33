package enteties;

import operations.Database;
import operations.MoneyOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static java.util.UUID.randomUUID;

public class Account {

    private String uuid;
    private String clientId;
    private BigDecimal amount;
    private String accCode;

    public Account(String uuid, String clientId, String accCode) {
        this.uuid = uuid;
        this.clientId = clientId;
        this.amount = new BigDecimal("0.00");
        this.accCode = accCode;
    }

    public Account(String uuid, String clientId, BigDecimal amount, String accCode) {
        this.uuid = uuid;
        this.clientId = clientId;
        this.amount = amount;
        this.accCode = accCode;
    }


    public static Account createAccount(Connection db, String clientId) {

        System.out.print("Выберите валюту:\n1. RUB\n2. EUR\n3. USD\n\nВыбор: ");
        String accCode = new Scanner(System.in).nextLine();

        switch (accCode) {
            case "1":
                accCode = "RUB";
                break;
            case "2":
                accCode = "EUR";
                break;
            case "3":
                accCode = "USD";
                break;
            default:
                return null;
        }

        String sql = "INSERT into ACCOUNT values(?, ?, default, ?)";
        Account account = new Account(randomUUID().toString(), clientId, accCode);

        return Database.createAccount(db, sql, account);
    }

    public String getUuid() {
        return uuid;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAccCode() {
        return accCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static void showAccounts(List<Account> userAccounts) {

        String u;
        for (int i=0; i < userAccounts.size(); i++) {
            Account account = userAccounts.get(i);
            u = account.getUuid();
            u = u.substring(u.lastIndexOf("-")+1);

            System.out.printf("%d. %s\n\t%s %s\n\n", i+1, u, account.getAmount(), account.getAccCode());
        }
    }

    public static void Refill(Connection db, List<Account> userAccounts) {

        if (userAccounts.size() == 0) {
            System.out.print("У Вас не создано ни одного аккаунта!\n\n");
            return;
        }

        System.out.print("Какой счёт пополнить?\n\n");
        Account.showAccounts(userAccounts);
        System.out.print("\nВыбор: ");

        Account account;
        try {
            int num = new Scanner(System.in).nextInt();
            account = userAccounts.get(num-1);
        }
        catch (Exception e) {
            return;
        }

        System.out.print("Какую валюту положить на этот счёт?:\n1. RUB\n2. EUR\n3. USD\n\nВыбор: ");
        String currency = new Scanner(System.in).nextLine();
        switch (currency) {
            case "1":
                currency = "RUB";
                break;
            case "2":
                currency = "EUR";
                break;
            case "3":
                currency = "USD";
                break;
            default:
                return;
        }

        System.out.print("Введите сумму: ");
        BigDecimal sum = new Scanner(System.in).nextBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP);

        MoneyOperations.Add(account, sum, currency);
        Database.updateBalance(db, account);

        System.out.print("Счёт пополнен!\n");
        Database.loadAccounts(db, account.getClientId());
    }

    public static void sendMoney(Connection db, String userPhone, List<Account> fromUserAccounts) {

        System.out.print("Введите номер телефона для перевода:\n\nВвод: +7 ");
        String phone = new Scanner(System.in).nextLine();

        if (userPhone.equals(phone)) {
            System.out.print("Счёт списания и зачисления должны быть различны!");
            return;
        }

        User toUser = Database.findUser(db, phone); // получили другого пользователя
        if (toUser == null) return;

        Account fromAcc = null;
        BigDecimal sum = null;
        boolean fl = true;
        while (fl) {

            System.out.print("С какого счёта снять средства?\n\n");
            Account.showAccounts(fromUserAccounts);
            System.out.print("Выбор: ");

            try {
                int num = new Scanner(System.in).nextInt();
                fromAcc = fromUserAccounts.get(num - 1);
                if ((fromAcc.getAmount().compareTo(new BigDecimal("0.00")) <= 0)) {
                    System.out.print("Недостаточно средств для перевода, выберите другой счёт.\n\n");
                } else fl = false;
            } catch (Exception e) {
                return;
            }
        }
        fl = true;
        while (fl) {
            try {
                System.out.print("Введите сумму: ");
                sum = new Scanner(System.in).nextBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP);
                if ((sum.compareTo(fromAcc.getAmount())) > 0) {
                    System.out.print("\nВы ввели сумму, превышающую остаток средств.");
                }
                else fl = false;
            } catch (Exception e) {
                return;
            }
        }

        Account toAcc = null;
        List<Account> toUserAccounts = Database.loadAccounts(db, toUser.getId());
        // проверяем, есть ли у другого пользователя аккаунт в нужной валюте
        if (toUserAccounts.size() != 0) {

            for (Account account : toUserAccounts)
                if (account.getAccCode().equals(fromAcc.getAccCode())) toAcc = account;
            if (toAcc == null) toAcc = toUserAccounts.get(toUserAccounts.size()-1);
        }
        else {
            System.out.print("Невозможно сделать перевод этому пользователю!\n");
            return;
        }

        String oldBalanceFrom = fromAcc.getAmount().toString();
        String oldBalanceTo = toAcc.getAmount().toString();


        MoneyOperations.Sub(fromAcc, sum, fromAcc.getAccCode());

        BigDecimal sum2 = MoneyOperations.Add(toAcc, sum, fromAcc.getAccCode());
        Database.updateBalance(db, fromAcc);
        Database.updateBalance(db, toAcc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Calendar.getInstance().getTime();

        Operation opFrom = new Operation(fromAcc.getClientId(), sdf.format(date)
                , sum.toString(), fromAcc.getAccCode()
                , fromAcc.getUuid(), toAcc.getUuid()
                , oldBalanceFrom, fromAcc.getAmount().toString());
        Database.addOperation(db, opFrom);

        Operation opTo = new Operation(toAcc.getClientId(), sdf.format(date)
                , sum2.toString(), toAcc.getAccCode()
                , fromAcc.getUuid(), toAcc.getUuid()
                , oldBalanceTo, toAcc.getAmount().toString());
        Database.addOperation(db, opTo);


        System.out.print("Перевод выполнен!\n");
        Database.loadAccounts(db, fromAcc.getClientId());
    }

}