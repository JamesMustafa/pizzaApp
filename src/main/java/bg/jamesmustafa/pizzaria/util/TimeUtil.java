package bg.jamesmustafa.pizzaria.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    //TODO: Learn about that exception
    private TimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime parseTimeToDate(String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateNow = LocalDateTime.now().format(dateFormatter).toString();
        String DateAndTimeSum = dateNow + " " + time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(DateAndTimeSum, dateTimeFormatter);

        return dateTime;
    }

    public static LocalDateTime parseDateToTime(String date){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String timeNow = LocalDateTime.now().format(dateFormatter).toString();
        String DateAndTimeSum = date + "-" + LocalDateTime.now().getDayOfMonth() + " " + timeNow;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(DateAndTimeSum, dateTimeFormatter);
        return dateTime;
    }
}
