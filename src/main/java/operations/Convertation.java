package operations;

import java.math.BigDecimal;

public class Convertation {

    private static BigDecimal rateUSD = new BigDecimal(78.56);
    private static BigDecimal rateEUR = new BigDecimal(85.15);

    public static BigDecimal CUR2RUB(BigDecimal addSum, BigDecimal curRate) {
        return addSum.multiply(curRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal RUB2CUR (BigDecimal addSum, BigDecimal curRate) {
        return addSum.divide(curRate,2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal CUR2CUR (BigDecimal addSum, BigDecimal curFrom, BigDecimal curTo) {
        addSum = CUR2RUB(addSum, curFrom);
        return RUB2CUR(addSum, curTo);
    }

    public static BigDecimal getRateEUR() {
        return rateEUR;
    }

    public static BigDecimal getRateUSD() {
        return rateUSD;
    }
}