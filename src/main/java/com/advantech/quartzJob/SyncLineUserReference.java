/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.dao.db1.LineUserReferenceDAO;
import com.advantech.model.db1.LineUserReference;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Component
@Transactional
public class SyncLineUserReference extends PrepareScheduleJob {
    
    private static final Logger logger = LoggerFactory.getLogger(SyncLineUserReference.class);
    
    @Autowired
    private LineUserReferenceDAO dao;

    @Override
    public void execute(List<DateTime> dts) throws Exception {
        dts.forEach(d -> {
            d = d.withTime(0, 0, 0, 0);
            int dayOfWeek = d.getDayOfWeek();
            DateTime nextDay = new DateTime(d).plusDays(dayOfWeek == 6 ? 2 : 1);
            
            List<LineUserReference> l = dao.findByDate(d);
            List<LineUserReference> nextDaysData = dao.findByDate(nextDay);
            
            if (nextDaysData.isEmpty()) {
                l.forEach(ref -> {
                    LineUserReference nr = new LineUserReference();
                    
                    nr.setLine(ref.getLine());
                    nr.setStation(ref.getStation());
                    nr.setUser(ref.getUser());
                    nr.setOnboardDate(nextDay.toDate());
                    
                    dao.insert(nr);
                });
                logger.info("SyncLineUserReference data in next workday (" + super.df.print(d) + ") finish");
            } else {
                logger.info("SyncLineUserReference fail, the data in next workday (" + super.df.print(d) + ") is already exists");
            }
        });
    }
    
}
