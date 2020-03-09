/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.dao.db1.LineUserReferenceDAO;
import com.advantech.model.db1.LineUserReference;
import java.util.List;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
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
public class SyncLineUserReference {

    private static final Logger logger = LoggerFactory.getLogger(SyncLineUserReference.class);

    @Autowired
    private LineUserReferenceDAO dao;

    public void execute() throws Exception {
        DateTime d = new DateTime();
        if (d.getHourOfDay() >= 17) {
            d = d.minusDays(d.getDayOfWeek() == 1 ? 2 : 1);
        }
        this.execute(d);
    }

    public void execute(DateTime d) throws Exception {
        d = d.withTime(0, 0, 0, 0);
        int dayOfWeek = d.getDayOfWeek();

        List<LineUserReference> l = dao.findByDate(d);

        l.forEach(ref -> {
            LineUserReference nr = new LineUserReference();

            nr.setLine(ref.getLine());
            nr.setStation(ref.getStation());
            nr.setUser(ref.getUser());
            nr.setOnboardDate(new DateTime(ref.getOnboardDate()).plusDays(dayOfWeek == 6 ? 2 : 1).toDate());

            dao.insert(nr);
        });

        logger.info("SyncLineUserReference finish");
    }

}
