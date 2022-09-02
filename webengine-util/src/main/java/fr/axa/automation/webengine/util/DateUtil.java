package fr.axa.automation.webengine.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {


    public static String getDateTime(String format){
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern(format);
        return FOMATTER.format(LocalDateTime.now());
    }

    public static String getDateTime(String format, Locale locale){
        DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern(format).withLocale(locale);
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

    public static void main(String[] args){
        String date = getDateTime("dd/MM/yyyy");
        String date2 = getDateTime("d MMM yyyy",Locale.FRENCH);
        String heure = getDateTime("HH",Locale.FRENCH);
        String minute = getDateTime("mm",Locale.FRENCH);
        System.out.println(date);
        System.out.println(date2);
        System.out.println(heure);
        System.out.println(minute);
    }

}
