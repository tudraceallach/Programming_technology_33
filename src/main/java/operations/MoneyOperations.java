package operations;

import enteties.Account;
import java.math.BigDecimal;

public class MoneyOperations {

    private static BigDecimal rateUSD = Convertation.getRateUSD();
    private static BigDecimal rateEUR = Convertation.getRateEUR();

    public static BigDecimal Add(Account account, BigDecimal sum, String addCurrency) {

        String accCurrency = account.getAccCode();

        // Если валюты равны, то складываем
        if (!accCurrency.equals(addCurrency)) {
            // Если нет, то сначала переводим в валюту карты
            switch (accCurrency) {
                case "RUB":
                    if (addCurrency.equals("USD")) sum = Convertation.CUR2RUB(sum, rateUSD);
                    else sum = Convertation.CUR2RUB(sum, rateEUR);
                    break;

                case "EUR":
                    if (addCurrency.equals("RUB")) sum = Convertation.RUB2CUR(sum, rateEUR);
                    else sum = Convertation.CUR2CUR(sum, rateUSD, rateEUR);
                    break;

                case "USD":
                    if (addCurrency.equals("RUB")) sum = Convertation.RUB2CUR(sum, rateUSD);
                    else sum = Convertation.CUR2CUR(sum, rateEUR, rateUSD);
                    break;
            }
        }
        account.setAmount(account.getAmount().add(sum) );
        return sum;
    }

    //    sub
    public static void Sub(Account account, BigDecimal sum, String addCurrency) {

        String accCurrency = account.getAccCode();

        // Если валюты равны, то складываем
        if (!accCurrency.equals(addCurrency)) {
            // Если нет, то сначала переводим в валюту карты
            switch (accCurrency) {
                case "RUB":
                    if (addCurrency.equals("USD")) sum = Convertation.CUR2RUB(sum, rateUSD);
                    else sum = Convertation.CUR2RUB(sum, rateEUR);
                    break;

                case "EUR":
                    if (addCurrency.equals("RUB")) sum = Convertation.RUB2CUR(sum, rateEUR);
                    else sum = Convertation.CUR2CUR(sum, rateUSD, rateEUR);
                    break;

                case "USD":
                    if (addCurrency.equals("RUB")) sum = Convertation.RUB2CUR(sum, rateUSD);
                    else sum = Convertation.CUR2CUR(sum, rateEUR, rateUSD);
                    break;
            }
        }
        account.setAmount(account.getAmount().subtract(sum) );
    }

}