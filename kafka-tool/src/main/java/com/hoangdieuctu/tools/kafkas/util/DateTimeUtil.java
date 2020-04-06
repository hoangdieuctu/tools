package com.hoangdieuctu.tools.kafkas.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static final String getBaicDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

}
