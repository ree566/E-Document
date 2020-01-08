/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.Floor;
import com.advantech.model.PrepareSchedule;
import com.advantech.service.FloorService;
import com.advantech.service.PrepareScheduleService1;
import java.util.List;
import static java.util.stream.Collectors.toList;
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
public class ArrangePrepareSchedule {

    private static final Logger logger = LoggerFactory.getLogger(ArrangePrepareSchedule.class);

    @Autowired
    private PrepareScheduleService1 psService;

    @Autowired
    private FloorService floorService;

    @Transactional
    public void execute() throws Exception {
        DateTime d = new DateTime().withTime(0, 0, 0, 0);
        this.execute(d);
    }
    
    public void execute(DateTime d) throws Exception {
        List<Floor> floors = floorService.findAll();
        floors = floors.stream()
                .filter(f -> f.getId() == 1 || f.getId() == 2)
                .collect(toList());        

        for (Floor f : floors) {
            List<PrepareSchedule> ps = psService.findPrepareSchedule(f, d);
            ps.forEach((p) -> {
                psService.update(p);
            });
        }

        logger.info("Update prepareSchedule finish");
    }

}
