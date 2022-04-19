package fr.axa.automation.webengine.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtil {


    public static String getDateTime(String format){
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern(format);
        return FOMATTER.format(LocalDateTime.now());
    }

    public static Calendar localDateTimeToCalendar(LocalDateTime localDateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(localDateTime.getYear(), localDateTime.getMonthValue()-1, localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
        return calendar;
    }

    public static LocalDateTime getLocalDateTime(Calendar calendar ){
        TimeZone tz = calendar.getTimeZone();
        ZoneId zoneId = tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zoneId);
    }

    public static Long getDiff(Calendar calendar1, Calendar calendar2){
        LocalDateTime localDateTime1 = getLocalDateTime(calendar1);
        LocalDateTime localDateTime2 = getLocalDateTime(calendar2);
        return Duration.between(localDateTime1,localDateTime2).toMillis();
    }
}
