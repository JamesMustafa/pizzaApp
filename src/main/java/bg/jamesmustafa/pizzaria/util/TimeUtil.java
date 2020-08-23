package bg.jamesmustafa.pizzaria.util;

import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {

    //This prevents the default parameter-less constructor from being used elsewhere in your code.
    //Additionally, you can make the class final, so that it can't be extended in subclasses,
    // which is a best practice for utility classes
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
