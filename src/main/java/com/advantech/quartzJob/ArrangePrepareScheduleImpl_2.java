/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.Bab;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.Line;
import com.advantech.model.db1.LineType;
import com.advantech.model.db1.LineUserReference;
import com.advantech.model.db1.PrepareSchedule;
import com.advantech.model.db1.User;
import com.advantech.service.db1.BabService;
import com.advantech.service.db1.FloorService;
import com.advantech.service.db1.LineService;
import com.advantech.service.db1.LineTypeService;
import com.advantech.service.db1.LineUserReferenceService;
import com.advantech.service.db1.PrepareScheduleService;
import static com.google.common.collect.Lists.newArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
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
public class ArrangePrepareScheduleImpl_2 {

    private static final Logger logger = LoggerFactory.getLogger(ArrangePrepareScheduleImpl_2.class);

    @Autowired
    private FloorService floorService;

    @Autowired
    private PrepareScheduleService psService;

    @Autowired
    private LineUserReferenceService lineUserRefService;

    @Autowired
    private LineService lineService;

    @Autowired
    private BabService babService;

    @Autowired
    private LineTypeService lineTypeService;

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

        List<LineType> lt = lineTypeService.findByPrimaryKeys(1, 2);

        List<Line> totalLine = lineService.findBySitefloorAndLineType(f.getName(), lt);

        List<Line> lines = totalLine.stream().filter(l -> l.getLineType().getId() == 1).collect(toList());
        List<Line> cellLines = totalLine.stream().filter(l -> l.getLineType().getId() == 2).collect(toList());

        List<PrepareSchedule> l = psService.findByFloorAndLineTypeAndDate(f, lt, d);

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
        scheduleEndTime = new DateTime(d).withTime(21, 0, 0, 0);

        restTimes = newArrayList(
                new Interval(new DateTime(d).withTime(12, 0, 0, 0), new DateTime(d).withTime(12, 50, 0, 0)),
                new Interval(new DateTime(d).withTime(15, 30, 0, 0), new DateTime(d).withTime(15, 40, 0, 0)),
                new Interval(new DateTime(d).withTime(17, 30, 0, 0), new DateTime(d).withTime(18, 0, 0, 0))
        );

    }

    private List<PrepareSchedule> setScheduleLine(List<LineUserReference> users, List<PrepareSchedule> l) {

        //Find modelName fit in settings
        List<String> modelNames = l.stream().map(s -> s.getModelName()).collect(toList());

        //Find jobnumber fit in settings
//        List<BabSettingHistory> jSettings = settings.stream().filter(s -> {
//            LineUserReference fitSettings = users.stream()
//                    .filter(fw -> fw.getId().getUser().getJobnumber().equals(s.getJobnumber()) && fw.getStation() == s.getStation())
//                    .findFirst().orElse(null);
//            return fitSettings != null;
//        }).collect(toList());
        Map<Line, List<PrepareSchedule>> result = new HashMap();
        List<Line> lines = users.stream().map(u -> u.getLine()).distinct().collect(toList());
        lines = lines.stream().sorted(comparing(Line::getName)).collect(toList());

        //Add empty List into result
        lines.forEach((line) -> {
            LineUserReference lr = users.stream()
                    .filter(lur -> lur.getLine().getId() == line.getId())
                    .findFirst()
                    .orElse(null);
            if (lr != null) {
                result.put(line, new ArrayList());
            }
        });

        result.put(emptyLine, new ArrayList());

        List<Bab> babs = babService.findByModelNamesAndLines(modelNames, lines);
        Map<String, Map<Line, Long>> modelUsageHistory = babs.stream()
                .collect(groupingBy(Bab::getModelName,
                        Collectors.groupingBy(Bab::getLine,
                                Collectors.mapping(Bab::getId,
                                        Collectors.counting()))));

        //Add missing zero count record
        for (String m : modelNames) {
            if (!modelUsageHistory.keySet().contains(m)) {
                Map<Line, Long> v = new HashMap();
                lines.forEach(ll -> {
                    v.put(ll, 0L);
                });
                modelUsageHistory.put(m, v);
            }
        }

        for (Map.Entry<String, Map<Line, Long>> entry : modelUsageHistory.entrySet()) {
            Map<Line, Long> v = entry.getValue();
            List<Line> existLines = new ArrayList(v.keySet());
            lines.forEach(ll -> {
                if (!existLines.contains(ll)) {
                    v.put(ll, 0L);
                }
            });
        }

        l.forEach((s) -> {
            //            HibernateObjectPrinter.print(s);

            Map<Line, Long> modelNameFitHistory = modelUsageHistory.get(s.getModelName());

            if (modelNameFitHistory == null) {
                modelNameFitHistory = new HashMap();
            }

            //Check line to add schedule(must schedule time and before 21:30)
            findFitLineSetting(s, users, modelNameFitHistory, result, new ArrayList());
        });

        List<PrepareSchedule> result2 = new ArrayList();

        result.forEach((k, v) -> {
            int i = 1;
            for (PrepareSchedule p : v) {
                p.setPriority(i++);
            }

            List<User> settingUsers = users.stream().filter(u -> u.getLine().equals(k))
                    .map(u -> u.getUser())
                    .collect(toList());

            v.forEach(s -> {
                s.setUsers(settingUsers);
            });

            result2.addAll(v);
        });

        return result2;
    }

    private void findFitLineSetting(PrepareSchedule currentSchedule, List<LineUserReference> users, Map<Line, Long> modelUsageCnt, Map<Line, List<PrepareSchedule>> result, List<Line> removeLine) {

        Map<Line, Long> modelUsageCntClone = new HashMap(modelUsageCnt);

        removeLine.forEach((line) -> {
            modelUsageCntClone.remove(line);
        });

        if (modelUsageCntClone.isEmpty()) {
            currentSchedule.setLine(emptyLine);
            currentSchedule.setStartDate(null);
            currentSchedule.setEndDate(null);
            currentSchedule.setUsers(null);
            currentSchedule.setOtherInfo(null);
            result.get(emptyLine).add(currentSchedule);
            return;
        }

        //Find best line setting
        Line line = modelUsageCntClone.entrySet()
                .stream()
                .max((Map.Entry<Line, Long> e1, Map.Entry<Line, Long> e2) -> {
                    int c1 = Long.compare(e1.getValue(), e2.getValue());
                    if (c1 != 0) {
                        return c1;
                    } else {
                        return e1.getKey().getName().compareTo(e2.getKey().getName());
                    }
                })
                .get()
                .getKey();

        if (result.isEmpty() || line == null) {
            return;
        }

        //Test and check last schedule time
        List<PrepareSchedule> lineSchedule = result.get(line);
        int usersCnt = (int) users.stream().filter(u -> u.getLine().getId() == line.getId()).count();

        if (usersCnt == 0) {
            removeLine.add(line);
            findFitLineSetting(currentSchedule, users, modelUsageCnt, result, removeLine);
        }
        List<PrepareSchedule> testScheduleList = newArrayList(lineSchedule);
        currentSchedule.setLine(line);

        testScheduleList.add(currentSchedule);
        testScheduleList = scheduleTime(usersCnt, testScheduleList);

        //If last schedule finish time is after 21:30, remove line choose in map
        Date lastScheduleDate = testScheduleList.get(testScheduleList.size() - 1).getEndDate();
        if (new DateTime(lastScheduleDate).isAfter(scheduleEndTime)) {
            removeLine.add(line);
            findFitLineSetting(currentSchedule, users, modelUsageCnt, result, removeLine);
        } else {
            result.replace(line, testScheduleList);
        }
    }

    private List<PrepareSchedule> scheduleTime(int users, List<PrepareSchedule> l) {

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
