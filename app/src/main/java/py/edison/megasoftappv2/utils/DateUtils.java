package py.edison.megasoftappv2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat DB_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getCurrentDateTime() {
        return DB_FORMAT.format(new Date());
    }

    public static String formatForDisplay(String dbDate) {
        try {
            Date date = DB_FORMAT.parse(dbDate);
            return SimpleDateFormat.getDateTimeInstance().format(date);
        } catch (ParseException e) {
            return dbDate;
        }
    }
}