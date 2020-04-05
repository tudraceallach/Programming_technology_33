package enteties;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation {

    private String id;
    private String date;
    private String sum;
    private String accCode;
    private String fromAcc;
    private String toAcc;
    private String balBefore;
    private String balAfter;

    // операция перевода
    public Operation(String id, String date, String sum, String accCode,
              String fromAcc, String toAcc, String balBefore, String balAfter) {

        this.id = id;
        this.date = date;
        this.sum = sum;
        this.accCode = accCode;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
        this.balBefore = balBefore;
        this.balAfter = balAfter;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getSum() {
        return sum;
    }

    public String getAccCode() {
        return accCode;
    }

    public String getFromAcc() {
        return fromAcc;
    }

    public String getToAcc() {
        return toAcc;
    }

    public String getBalBefore() {
        return balBefore;
    }

    public String getBalAfter() {
        return balAfter;
    }

    public static String formatDate(Connection db, Date date) {

        String newdate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(date.toString());
            SimpleDateFormat newsdf = new SimpleDateFormat("dd-MM-yyyy");
            newdate = newsdf.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return newdate;
    }

}
