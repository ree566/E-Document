/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Wei.Cheng
 */
public class DatetimeGenerator {

    private final String dateFormateType;
    private final DateTimeFormatter fmt;

    public DatetimeGenerator(String generateType) {
        dateFormateType = generateType;
        fmt = DateTimeFormat.forPattern(dateFormateType);
    }
    
    public DateTime getDate(){
        return new DateTime();
    }

    public String getToday() {
        return fmt.print(getDate());
    }

    public String getYesterday() {
        return fmt.print(getDate().minusDays(1));
    }
    
      public String dateFormatToString(Object date) {
        return fmt.print(new DateTime(date));
    }
}
