package bg.jamesmustafa.pizzaria.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    //TODO: Learn about that exception
    private TimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime parseTime(String time)
    {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String confirmDate = LocalDateTime.now().format(dateFormatter).toString();
        String waitingDateAndTime = confirmDate + " " + time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(waitingDateAndTime, dateTimeFormatter);

        return dateTime;
    }
}
