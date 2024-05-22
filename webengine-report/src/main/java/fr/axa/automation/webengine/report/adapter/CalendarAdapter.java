package fr.axa.automation.webengine.report.adapter;

import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.Calendar;
import java.util.Locale;

public class CalendarAdapter extends XmlAdapter<String, Calendar> {

    @Override
    public Calendar unmarshal(String value) throws Exception {
        return (jakarta.xml.bind.DatatypeConverter.parseDateTime(value));
    }

    @Override
    public String marshal(Calendar calendar) throws Exception {
        return DateUtil.getDateTime(calendar,FormatDate.YYYYMMDD_T_HHMMSS, Locale.FRENCH, "Europe/paris");
    }
}
