/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.Floor;
import com.advantech.model.db1.Line;
import com.advantech.model.db1.LineUserReference;
import com.advantech.model.db1.PrepareSchedule;
import com.advantech.service.db1.BabSettingHistoryService;
import com.advantech.service.db1.FloorService;
import com.advantech.service.db1.LineService;
import com.advantech.service.db1.LineUserReferenceService;
import com.advantech.service.db1.PrepareScheduleService;
import static com.google.common.collect.Lists.newArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
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
public abstract class ArrangePrepareSchedule {

    private static final Logger logger = LoggerFactory.getLogger(ArrangePrepareSchedule.class);

    @Autowired
    private FloorService floorService;

    @Autowired
    private PrepareScheduleService psService;

    @Autowired
    private LineUserReferenceService lineUserRefService;

    @Autowired
    private LineService lineService;

    @Autowired
    private BabSettingHistoryService settingHistoryService;

    private List<Interval> restTimes;
    private DateTime scheduleStartTime;
    private DateTime scheduleEndTime;

    DateTimeFormatter df = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    Line emptyLine;

    public void execute() throws Exception {
        this.execute(new DateTime());
    }

    public void execute(DateTime d) throws Exception {
        d = d.withTime(0, 0, 0, 0);

        List<Floor> floors = floorService.findAll();
        floors = floors.stream()
                .filter(f -> f.getId() == 1 || f.getId() == 2)
                .collect(toList());

        for (Floor f : floors) {
            List<PrepareSchedule> ps = this.findPrepareSchedule(f, d);
            ps.forEach((p) -> {
                psService.update(p);
            });
        }

        logger.info("Update prepareSchedule finish");
    }

    public List<PrepareSchedule> findPrepareSchedule(Floor f, DateTime d) {
        /*
            Auto schedule line at 6:00 am
            Refresh schedule time & people when user change line schedule priority or user's setting
         */
 /*
            Get all user in line setting << how to group as Map<Line, List<LineUserReference>> result?
            Foreach schedule
            Find best user setting & last finished time not > 21:30
            Set schedule line
        
            Where to decide schedule order?
         */

        updateDateParamater(d);

        List<Line> lines = lineService.findBySitefloorAndLineType(f.getName(), 1);
        List<Line> cellLines = lineService.findBySitefloorAndLineType(f.getName(), 2);

        List<PrepareSchedule> l = psService.findByFloorAndDate(f, d);

        List<PrepareSchedule> noneCellableSchedules = l.stream()
                .filter(p -> p.getTimeCost().compareTo(new BigDecimal(100)) >= 0)
                .collect(toList());

        List<PrepareSchedule> cellableSchedules = l.stream()
                .filter(p -> p.getTimeCost().compareTo(new BigDecimal(100)) == -1)
                .collect(toList());

        noneCellableSchedules = noneCellableSchedules.stream()
                .sorted(comparing(PrepareSchedule::getTimeCost).reversed())
                .collect(toList());

        List<LineUserReference> users = lineUserRefService.findByLinesAndDate(lines, d);
        List<LineUserReference> cellUsers = lineUserRefService.findByLinesAndDate(cellLines, d);

        emptyLine = lineService.findByPrimaryKey(7);

        List<PrepareSchedule> result = new ArrayList();
        if (!noneCellableSchedules.isEmpty()) {
            result.addAll(setScheduleLine(users, noneCellableSchedules));
        }
        if (!cellableSchedules.isEmpty()) {
            result.addAll(setScheduleLine(cellUsers, cellableSchedules));
        }
        return result;
    }

    private void updateDateParamater(DateTime d) {
        scheduleStartTime = new DateTime(d).withTime(8, 30, 0, 0);
//        scheduleEndTime = new DateTime(d).withTime(21, 0, 0, 0);
        scheduleEndTime = new DateTime(d).withTime(20, 0, 0, 0);

        restTimes = newArrayList(
                new Interval(new DateTime(d).withTime(12, 0, 0, 0), new DateTime(d).withTime(12, 50, 0, 0)),
                new Interval(new DateTime(d).withTime(15, 30, 0, 0), new DateTime(d).withTime(15, 40, 0, 0)),
                new Interval(new DateTime(d).withTime(17, 30, 0, 0), new DateTime(d).withTime(18, 0, 0, 0))
        );

    }

    protected abstract List<PrepareSchedule> setScheduleLine(List<LineUserReference> users, List<PrepareSchedule> l);

    protected List<PrepareSchedule> scheduleTime(int users, List<PrepareSchedule> l) {

        int maxPeopleCnt = users;

        PrepareSchedule prev = null;
        int cnt = 0;

        for (PrepareSchedule w : l) {
            DateTime start = new DateTime(scheduleStartTime);
            if (cnt != 0 && prev != null) {
                start = new DateTime(prev.getEndDate());
            }
            BigDecimal addTime = w.getTimeCost().setScale(0, RoundingMode.UP);
            DateTime end = start.plusMinutes(addTime.divide(new BigDecimal(maxPeopleCnt), 0, BigDecimal.ROUND_UP).intValue());

            Interval timeAdjust = byPassRestTime(new Interval(start, end));

            w.setStartDate(timeAdjust.getStart().toDate());
            w.setEndDate(timeAdjust.getEnd().toDate());

            prev = w;
            cnt++;
        }
        return l;
    }

    private Interval byPassRestTime(Interval i) {
        for (Interval restTime : restTimes) {
            int iMin = Minutes.minutesBetween(i.getStart(), i.getEnd()).getMinutes();
            int restMin = Minutes.minutesBetween(restTime.getStart(), restTime.getEnd()).getMinutes();
            if (hasOverlap(i, restTime)) {
                if (isInRestTime(restTime, i.getStart()) && isInRestTime(restTime, i.getEnd())) {
                    /*
                        i   |----|
                        r |--------|   
                     */
                    return new Interval(restTime.getEnd(), restTime.getEnd().plusMinutes(iMin));
                } else if (isInRestTime(restTime, i.getStart()) && !isInRestTime(restTime, i.getEnd())) {
                    /*
                        i    |--------|
                        r |----|   
                     */
                    int overlap = Minutes.minutesBetween(i.getStart(), restTime.getEnd()).getMinutes();
                    return new Interval(restTime.getEnd(), i.getEnd().plusMinutes(overlap));
                } else if (!isInRestTime(restTime, i.getStart()) && isInRestTime(restTime, i.getEnd())) {
                    /*
                        i |----|
                        r   |--------|   
                     */
                    int overlap = Minutes.minutesBetween(restTime.getStart(), i.getEnd()).getMinutes();
                    return new Interval(i.getStart(), restTime.getEnd().plusMinutes(overlap));
                } else {
                    /*
                        i |--------|
                        r   |----|   
                     */
                    return new Interval(i.getStart(), i.getEnd().plusMinutes(restMin));
                }
            }
        }
        return i;
    }

    private boolean hasOverlap(Interval t1, Interval t2) {
        return !t1.getEnd().isBefore(t2.getStart()) && !t1.getStart().isAfter(t2.getEnd());
    }

    private boolean isInRestTime(Interval rest, DateTime d) {
        return rest.getStart().compareTo(d) * d.compareTo(rest.getEnd()) >= 0;
    }

}
