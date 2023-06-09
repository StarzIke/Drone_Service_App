package com.chisom.ikemefuna.drone.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeConverter {

    public static String convertLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM, yyyy hh:mm:ss a", Locale.US);
        return formatter.format(localDateTime);
    }
}
