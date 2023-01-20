package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class DateUtilTest {

    static final Logger logger = LoggerFactory.getLogger(DateUtilTest .class);

    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HH");
        return dtf.format(LocalDateTime.now());
    }

    @Test
    void testGetDateTime() {
        String dateWithHour = getCurrentDate();
        logger.info("Date with hour :"+dateWithHour);
        String dateWithTime = DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat());
        logger.info("Date with time :"+dateWithTime);
        Assertions.assertTrue(dateWithTime.startsWith(dateWithHour));
    }
    @Test
    void testGetDateTime2() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.of(2022,12,15,12,00,00);
        Calendar calendar = DateUtil.localDateTimeToCalendar(localDateTime);
        String dateTime = DateUtil.getDateTime(calendar,FormatDate.YYYYMMDD_T_HHMMSS);
        logger.info("Date with hour :"+dateTime);
        Assertions.assertEquals("2022-12-15T12:00:00",dateTime);
    }

    @Test
    void testGetDateTime3() {
        LocalDateTime localDateTime = LocalDateTime.of(2022,12,15,12,00,00);
        Calendar calendar = DateUtil.localDateTimeToCalendar(localDateTime);
        String dateTime = DateUtil.getDateTime(calendar,FormatDate.YYYYMMDD_T_HHMMSS,Locale.getDefault());
        logger.info("Date with hour :"+dateTime);
        Assertions.assertEquals("2022-12-15T12:00:00",dateTime);
    }

    @Test
    void testGetDateTimeWithLocal() {
        String dateWithHour = getCurrentDate();
        logger.info("Date with hour :"+dateWithHour);
        String dateWithTime = DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat(),Locale.FRENCH);
        logger.info("Date with time :"+dateWithTime);
        Assertions.assertTrue(dateWithTime.startsWith(dateWithHour));
    }

    @Test
    void testLocalDateTimeToCalendar() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.of(2022,12,15,12,00,00);
        Calendar calendar = DateUtil.localDateTimeToCalendar(localDateTime);
        Date date = calendar.getTime();
        LocalDateTime localDateTimeResult = date.toInstant().atZone(zoneId).toLocalDateTime();
        Assertions.assertEquals(localDateTime, localDateTimeResult);
    }

    @Test
    void testGetLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2022, 12, 15, 12, 00, 00);
        Calendar calendar = DateUtil.localDateTimeToCalendar(localDateTime);
        LocalDateTime localDateTimeResult = DateUtil.getLocalDateTime(calendar);
        Assertions.assertEquals(localDateTime, localDateTimeResult);
    }

    @Test
    void testGetDiffCalendar() {
        LocalDateTime localDateTimeStart = LocalDateTime.of(2022, 12, 15, 12, 00, 00);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(2022, 12, 16, 12, 00, 00);
        Calendar calendarStart = DateUtil.localDateTimeToCalendar(localDateTimeStart);
        Calendar calendarEnd = DateUtil.localDateTimeToCalendar(localDateTimeEnd);
        Long diff = DateUtil.getDiff(calendarStart,calendarEnd);
        logger.info("Diff calendar :"+diff);
        Assertions.assertEquals(86400000,diff);
    }


}