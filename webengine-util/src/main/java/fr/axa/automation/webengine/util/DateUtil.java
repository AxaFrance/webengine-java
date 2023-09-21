package fr.axa.automation.webengine.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtil {

    private DateUtil() {
    }

    public static String getDateTime(FormatDate formatDate){
        return getDateTime(formatDate.getFormat());
    }

    public static String getDateTime(String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public static String getDateTime(FormatDate formatDate, Locale locale){
        return getDateTime(formatDate.getFormat(),locale);
    }

    public static String getDateTime(String format, Locale locale){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format).withLocale(locale);
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public static String getDateTime(Calendar calendar,FormatDate formatDate){
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(formatDate.getFormat());
        return dateTimeFormatter.format(new Date(calendar.getTimeInMillis()));
    }

    public static String getDateTime(Calendar calendar,FormatDate formatDate,  Locale locale){
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(formatDate.getFormat(),locale);
        return dateTimeFormatter.format(new Date(calendar.getTimeInMillis()));
    }

    public static String getDateTime(Calendar calendar,FormatDate formatDate,  Locale locale, String timeZone){
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(formatDate.getFormat(),locale);
        dateTimeFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateTimeFormatter.format(new Date(calendar.getTimeInMillis()));
    }


    public static Calendar localDateTimeToCalendar(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Date date = Date.from(localDateTime.atZone(zoneId).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        return calendar;
    }

    public static LocalDateTime getLocalDateTime(Calendar calendar ){
        TimeZone tz = calendar.getTimeZone();
        ZoneId zoneId = tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zoneId);
    }

    public static Long getDiff(Calendar startCalendar, Calendar endCalendar){
        LocalDateTime localDateTime1 = getLocalDateTime(startCalendar);
        LocalDateTime localDateTime2 = getLocalDateTime(endCalendar);
        return Duration.between(localDateTime1,localDateTime2).toMillis();
    }

    public static String minusDay(FormatDate formatDate, int day){
        return addDay(formatDate,day*(-1));
    }

    public static String addDay(FormatDate formatDate, int day){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatDate.getFormat());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return dateFormat.format(cal.getTime());
    }

    public static String addMonth(FormatDate formatDate, int month){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatDate.getFormat());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return dateFormat.format(cal.getTime());
    }

    public static String minusMonth(FormatDate formatDate, int month){
        return addMonth(formatDate,month*(-1));
    }


}
