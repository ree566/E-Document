/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BabSettingHistoryDAO;
import com.advantech.dao.LineDAO;
import com.advantech.dao.LineUserReferenceDAO;
import com.advantech.model.PrepareSchedule;
import com.advantech.dao.PrepareScheduleDAO;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.Floor;
import com.advantech.model.Line;
import com.advantech.model.LineUserReference;
import com.advantech.model.User;
import static com.google.common.collect.Lists.newArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class PrepareScheduleService {

    @Autowired
    private PrepareScheduleDAO dao;

    @Autowired
    private LineUserReferenceDAO lineUserRefDAO;

    @Autowired
    private LineDAO lineDAO;

    @Autowired
    private BabSettingHistoryDAO settingHistoryDAO;

    private List<Interval> restTimes;
    private DateTime scheduleStartTime, scheduleEndTime;

    public List<PrepareSchedule> findAll() {
        return dao.findAll();
    }

    public PrepareSchedule findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    private void updateDateParamater(DateTime d) {
        scheduleStartTime = new DateTime(d).withTime(8, 30, 0, 0);
//        scheduleEndTime = new DateTime(d).withTime(21, 0, 0, 0);
        scheduleEndTime = new DateTime(d).withTime(23, 59, 59, 0);
        restTimes = newArrayList(
                new Interval(new DateTime(d).withTime(12, 0, 0, 0), new DateTime(d).withTime(12, 50, 0, 0)),
                new Interval(new DateTime(d).withTime(15, 30, 0, 0), new DateTime(d).withTime(15, 40, 0, 0)),
                new Interval(new DateTime(d).withTime(17, 30, 0, 0), new DateTime(d).withTime(18, 0, 0, 0))
        );
    }

    public List<PrepareSchedule> findPrepareSchedule(Floor f, DateTime d) {

        updateDateParamater(d);
        d = d.withTime(0, 0, 0, 0);

        List<PrepareSchedule> l = dao.findByFloorAndDate(f, d);

        List<Line> lines = lineDAO.findBySitefloorAndLineType(f.getName(), 1, 2);

        List<LineUserReference> users = lineUserRefDAO.findByLinesAndDate(lines, d);

        List<PrepareSchedule> result = new ArrayList();

        lines.forEach((line) -> {
            List<LineUserReference> lineUsers = users.stream()
                    .filter(lr -> Objects.equals(line, lr.getLine()))
                    .collect(toList());

            List<PrepareSchedule> lineSchedule = l.stream()
                    .filter(p -> Objects.equals(line, p.getLine()))
                    .collect(toList());

            if (line != null && !lineUsers.isEmpty() && !l.isEmpty()) {
                lineSchedule = addUser(lineUsers, lineSchedule);
                lineSchedule = scheduleTime(lineUsers, lineSchedule);
                lineSchedule = addProducedFlag(lineSchedule);
                result.addAll(lineSchedule);
            }
        });

        return result;
    }

    private List<PrepareSchedule> addUser(List<LineUserReference> users, List<PrepareSchedule> l) {

        int maxPeopleCnt = users.size();
        List<User> settingUsers = users.stream().map(u -> u.getUser()).collect(toList());

        int peopleFlag = 0;
        int settingUsersCnt = settingUsers.size();

        for (PrepareSchedule w : l) {
            List<User> u = new ArrayList();
            if (settingUsersCnt <= maxPeopleCnt) {
                u.addAll(settingUsers);
            } else {
                for (int i = peopleFlag, j = peopleFlag + maxPeopleCnt; i < j; i++) {
                    u.add(settingUsers.get(i % settingUsersCnt));
                }
                peopleFlag += maxPeopleCnt;
            }
            w.setUsers(u);
        }

//        l = fillUserGap(line, l);
        return l;
    }

    private List<PrepareSchedule> fillUserGap(Line line, List<PrepareSchedule> l) {

        List<Line> cellLine = lineDAO.findBySitefloorAndLineType(line.getFloor().getName(), 2);
        List<LineUserReference> cellUsers = lineUserRefDAO.findByLines(cellLine);
        List<User> settingUsers = cellUsers.stream().map(u -> u.getUser()).collect(toList());

        int peopleFlag = 0;

        for (PrepareSchedule w : l) {
            int gapCnt = line.getPeople() - w.getUsers().size();
            List<User> scheduleUsers = w.getUsers();
            if (cellUsers.size() <= gapCnt) {
                scheduleUsers.addAll(settingUsers);
            } else {
                for (int i = 0; i < gapCnt; i++) {
                    scheduleUsers.add(settingUsers.get(peopleFlag % l.size()));
                }
                peopleFlag += gapCnt;
            }
        }

        return l;

    }

    private List<PrepareSchedule> addProducedFlag(List<PrepareSchedule> l) {
        if (l.isEmpty()) {
            return l;
        }
        List<String> modelNames = l.stream()
                .map(p -> p.getModelName())
                .distinct()
                .collect(toList());

        List<BabSettingHistory> settings = settingHistoryDAO.findByBabModelNames(modelNames);

        l.forEach(p -> {
            Map infoMap = new HashMap();
            List<User> scheduleUsers = p.getUsers();
            int[] ordinal = {0};
            scheduleUsers.forEach((u) -> {
//                System.out.println(ordinal[0]);
//                BabSettingHistory h = settings.stream().filter(s
//                        -> s.getBab().getModelName().equals(p.getModelName())
//                        && s.getStation() == ordinal[0]
//                        && s.getJobnumber().equals(u.getJobnumber()))
//                        .findFirst()
//                        .orElse(null);

                BabSettingHistory h = settings.stream().filter(s
                        -> s.getBab().getModelName().equals(p.getModelName())
                        && s.getJobnumber().equals(u.getJobnumber()))
                        .findFirst()
                        .orElse(null);

                infoMap.put(ordinal[0], h != null);
                ordinal[0]++;
            });
            p.setOtherInfo(infoMap);
        });

        return l;
    }

    private List<PrepareSchedule> scheduleTime(List<LineUserReference> users, List<PrepareSchedule> l) {

        l = l.stream().sorted(comparing(PrepareSchedule::getPriority)).collect(toList());

        int maxPeopleCnt = users.get(0).getLine().getPeople();

        PrepareSchedule prev = null;
        int cnt = 0;

        for (PrepareSchedule w : l) {
            DateTime start = this.scheduleStartTime;
            if (cnt != 0 && prev != null) {
                start = new DateTime(prev.getEndDate());
            }
            BigDecimal addTime = w.getTimeCost().setScale(0, RoundingMode.UP);
            DateTime end = start.plusMinutes(addTime.divide(new BigDecimal(users.size() > maxPeopleCnt ? maxPeopleCnt : users.size()), 0, BigDecimal.ROUND_UP).intValue());

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
            int restMin = Minutes.minutesBetween(restTime.getStart(), restTime.getEnd()).getMinutes();
            if (isInRestTime(restTime, i.getStart())) {
                return new Interval(restTime.getStart(), restTime.getStart().plusMinutes(restMin));
            }
            if (hasOverlap(i, restTime)) {
                return new Interval(i.getStart(), i.getEnd().plusMinutes(restMin));
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

    public int separateCnt(PrepareSchedule pojo, List<Integer> cnt) {
        int rowCnt = 0;
        int baseCnt = pojo.getScheduleQty();
        BigDecimal baseTimeCost = pojo.getTimeCost();

        Integer sum = cnt.stream().reduce(0, Integer::sum);
        if (baseCnt - sum > 0) {
            cnt.add(baseCnt - sum);
        } else if (sum > baseCnt) {
            return 1;
        }

        for (int c : cnt) {
            if (rowCnt++ == 0) {
                pojo.setScheduleQty(c);
                pojo.setTimeCost(baseTimeCost.divide(new BigDecimal(baseCnt), 2, BigDecimal.ROUND_UP)
                        .multiply(new BigDecimal(c))
                        .setScale(2, BigDecimal.ROUND_UP));
                dao.update(pojo);
            } else {
                PrepareSchedule clone = SerializationUtils.clone(pojo);
                clone.setId(0);
                clone.setScheduleQty(c);
                clone.setTimeCost(baseTimeCost.divide(new BigDecimal(baseCnt), 2, BigDecimal.ROUND_UP)
                        .multiply(new BigDecimal(c))
                        .setScale(2, BigDecimal.ROUND_UP));
                dao.insert(clone);
            }
        }

        return 1;
    }

    public int insert(PrepareSchedule pojo) {
        return dao.insert(pojo);
    }

    public int update(PrepareSchedule pojo) {
        return dao.update(pojo);
    }

    public int updateAndResortPriority(PrepareSchedule pojo) {
        dao.update(pojo);

        List<PrepareSchedule> l = dao.findByLineAndDate(pojo.getLine(), new DateTime(pojo.getOnBoardDate()));

        l = l.stream().sorted(comparing(PrepareSchedule::getPriority)).collect(toList());

        //Check pojo priority if > list size auto update to last one
        //Update and resort again
        int od = 1;
        for (PrepareSchedule ps : l) {
            if (!Objects.equals(ps, pojo)) {
                if (ps.getPriority() == pojo.getPriority()) {
                    ps.setPriority(++od);
                } else {
                    ps.setPriority(od);
                }
            } else if (Objects.equals(ps, pojo) && ps.getPriority() > l.size()) {
                ps.setPriority(l.size());
            }
            od++;
            dao.update(ps);
        }

        return 1;
    }

    public int delete(PrepareSchedule pojo) {
        return dao.delete(pojo);
    }

}
