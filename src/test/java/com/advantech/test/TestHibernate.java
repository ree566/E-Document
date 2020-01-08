/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.AlarmTestActionDAO;
import com.advantech.dao.BabDAO;
import com.advantech.dao.BabPassStationRecordDAO;
import com.advantech.dao.BabSettingHistoryDAO;
import com.advantech.dao.FloorDAO;
import com.advantech.dao.LineDAO;
import com.advantech.dao.LineUserReferenceDAO;
import com.advantech.dao.PrepareScheduleDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.AlarmBabAction;
import com.advantech.model.Bab;
import com.advantech.model.BabAlarmHistory;
import com.advantech.model.BabPassStationRecord;
import com.advantech.model.BabPcsDetailHistory;
import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.BabStatus;
import com.advantech.model.CountermeasureEvent;
import com.advantech.model.Floor;
import com.advantech.model.Fqc;
import com.advantech.model.FqcTimeTemp;
import com.advantech.model.Line;
import com.advantech.model.LineUserReference;
import com.advantech.model.PrepareSchedule;
import com.advantech.model.ReplyStatus;
import com.advantech.model.SensorTransform;
import com.advantech.model.TagNameComparison;
import com.advantech.model.User;
import com.advantech.security.State;
import com.advantech.service.BabPcsDetailHistoryService;
import com.advantech.service.PassStationRecordService;
import com.advantech.webservice.WebServiceRV;
import com.fasterxml.jackson.core.JsonProcessingException;
import static com.google.common.collect.Lists.newArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.*;
import java.util.stream.IntStream;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestHibernate {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private AlarmTestActionDAO alarmTestActionDAO;

    @Autowired
    private BabDAO babDAO;

    @Autowired
    private com.advantech.dao.TestDAO testDAO;

    @Autowired
    private BabPcsDetailHistoryService pcsHistoryService;

    @Autowired
    private BabPassStationRecordDAO passStationDAO;

//    @Test
    @Transactional
    @Rollback(true)
    public void test() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("select u from User u join u.userNotifications notifi where notifi.id = 1");
        List l = q.list();
        assertTrue(l.get(0) instanceof User);
        assertEquals(13, l.size());
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void test2() throws JsonProcessingException {
        Object o = alarmTestActionDAO.findByPrimaryKey("T1");
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void test3() throws JsonProcessingException {
        Object o = babDAO.findByPrimaryKey(11035);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlQuery() {
        Query q = sessionFactory.getCurrentSession().createSQLQuery("select t.* from testView t");
        q.setResultTransformer(Transformers.aliasToBean(Bab.class));
        List l = q.list();
        assertEquals(10, l.size());
        assertTrue(l.get(0) instanceof Bab);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testQuery() throws JsonProcessingException {
        testObject(
                //                Floor.class,
                User.class,
                //                UserProfile.class,
                //                UserNotification.class,
                //                Unit.class,
                //                TestTable.class,
                //                com.advantech.model.Test.class,
                //                TestRecord.class,
                //                AlarmTestAction.class,
                //                AlarmBabAction.class,
                //                Line.class,
                //                LineType.class,
                //                LineTypeConfig.class,
                //                Bab.class,
                //                Fbn.class,
                //                BabPcsDetailHistory.class,
                //                BabSettingHistory.class,
                //                BabBalanceHistory.class,
                //                BabAlarmHistory.class,
                //                Countermeasure.class,
                //                CountermeasureEvent.class,
                //                ActionCode.class,
                //                ErrorCode.class,
                CountermeasureEvent.class
        );

    }

    private void testObject(Class... cls) {
        Session session = sessionFactory.getCurrentSession();
        for (Class c : cls) {
            System.out.println(c);
            Object o = session.createCriteria(c).setMaxResults(1).uniqueResult();
            assertNotNull(o);
//            assertEquals(o.getClass(), c);
            System.out.println(c + " Test pass");
        }
    }

    @Autowired
    private SqlViewDAO sqlViewDAO;

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlFunction() throws JsonProcessingException {
        Object o = sqlViewDAO.findBabAvg(10870);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFbn() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(TagNameComparison.class);
        c.add(Restrictions.eq("id.lampSysTagName.name", "L4-S-1"));
        HibernateObjectPrinter.print(c.list());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testHql() throws JsonProcessingException {
        String hql = "select new Map(b.id as id, b.po as po, b.modelName as modelName, "
                + "l.name as lineName, f.name as floorName) "
                + "from Bab b left join b.line l left join l.floor f";
        Query q = sessionFactory.getCurrentSession().createQuery(hql);
        q.setMaxResults(10);
        List l = q.list();
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void chartTest() throws JsonProcessingException {
        List l = pcsHistoryService.findByBabForMap(12595);
        HibernateObjectPrinter.print(this.toChartForm(l));
    }

    private Map toChartForm(List<Map> l) {
        List<Map<String, Object>> total = new ArrayList();
        int diffSum = 0;
        int maxGroup = 0;
        for (Map m : l) {
            String tagName = (String) m.get("tagName");
            Integer groupid = (Integer) m.get("groupid");
            Integer diff = (Integer) m.get("diff");
            Map filter = total.stream()
                    .filter(i -> i.containsKey("name") && i.get("name").equals(tagName))
                    .findFirst().orElse(null);
            if (filter == null) {
                Map tagInfo = new HashMap();
                tagInfo.put("name", tagName);
                List dataPoints = new ArrayList();
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", diff);
                tagInfo.put("dataPoints", dataPoints);
                total.add(tagInfo);
            } else {
                List dataPoints = (List) filter.get("dataPoints");
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", m.get("diff"));
                dataPoints.add(dataPoint);
            }
            diffSum += diff;
            if (maxGroup < groupid) {
                maxGroup = groupid;
            }
        }

        int people = total.size();

        Map infoWithAvg = new HashMap();
        infoWithAvg.put("avg", (diffSum / people / maxGroup));
        infoWithAvg.put("data", total);
        return infoWithAvg;
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testConverter() {
        Session session = sessionFactory.getCurrentSession();
        Bab bab = (Bab) session.get(Bab.class, 12730);
        assertNotNull(bab);
        assertNotNull(bab.getBabStatus());
        assertEquals(bab.getBabStatus(), BabStatus.UNFINSHED);
        assertNotNull(bab.getReplyStatus());
        assertEquals(bab.getReplyStatus(), ReplyStatus.UNREPLIED);

        bab.setBabStatus(BabStatus.CLOSED);
        bab.setReplyStatus(ReplyStatus.NO_NEED_TO_REPLY);

        session.update(bab);

        HibernateObjectPrinter.print(bab);
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testTagNameCompar() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        SensorTransform sensor = session.get(SensorTransform.class, "Cell-15-S-1");
        assertNotNull(sensor);
        sensor.setName("Cell-15-S-1*");
        session.save(sensor);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSettingHistory() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        SensorTransform sensor = session.get(SensorTransform.class, "L1-S-1");
        assertNotNull(sensor);
        BabSensorLoginRecord loginRec = new BabSensorLoginRecord(sensor, "A-7568");
        session.save(loginRec);
        assertNotEquals(0, loginRec.getId());
        assertNotNull(loginRec.getBeginTime());
        HibernateObjectPrinter.print(loginRec);

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabProcessing() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        AlarmBabAction a = session.get(AlarmBabAction.class, "LD-L-4");
        assertNotNull(a);
        a.setAlarm(1);
        session.save(a);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void userPswRefractor() {
        Session session = sessionFactory.getCurrentSession();
        List<User> l = session.createCriteria(User.class).list();
        assertTrue(!l.isEmpty());
        l.stream().map((u) -> {
            u.setState(State.ACTIVE);
            return u;
        }).forEachOrdered((u) -> {
            session.update(u);
        });
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testQueryEnumField() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(Bab.class);
        c.add(Restrictions.eq("replyStatus", ReplyStatus.UNREPLIED));
        List l = c.list();
        assertEquals(375, l.size());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testCriteria() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(Bab.class);
        c.createAlias("line", "l");
        c.createAlias("l.lineType", "lt");
        c.createAlias("babAlarmHistorys", "h");
        c.createAlias("l.users", "u");
        c.add(Restrictions.eq("replyStatus", ReplyStatus.UNREPLIED));
        c.add(Restrictions.eq("l.floor.id", 1));
        List<Bab> l = c.list();
        Bab b = l.get(0);
        Set<User> s1 = b.getLine().getUsers();
        Set<BabAlarmHistory> s2 = b.getBabAlarmHistorys();
        assertTrue(!s1.isEmpty());
        assertTrue(!s2.isEmpty());
        HibernateObjectPrinter.print(l.get(0));
        HibernateObjectPrinter.print(s1);
        HibernateObjectPrinter.print(s2);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testQuery2() {
        String lineName = "CELL";
        List l = sessionFactory.getCurrentSession().createQuery(
                "from BabSettingHistory bsh join bsh.bab b join b.line l "
                + "where bsh.id in( "
                + "select min(bsh1.id) from BabSettingHistory bsh1 "
                + "join bsh1.bab b2 join b2.line l2 "
                + "where b2.babStatus is null "
                + ("CELL".equals(lineName)
                ? "and lower(l2.name) like CONCAT(lower(:lineName), '%')"
                : "and l2.name = :lineName ")
                + "and bsh1.lastUpdateTime is null "
                + "group by bsh1.tagName) "
                + "order by bsh.tagName")
                .setParameter("lineName", lineName)
                .list();

        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testReport() {
        String testModel = "UTC-520FP-IKA0E";
        String lineTypeName = "ASSY";

        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(BabPcsDetailHistory.class, "bps");
        List<BabPcsDetailHistory> l = c.createAlias("bab", "b")
                .createAlias("b.line", "l")
                .createAlias("l.lineType", "lt")
                .add(Restrictions.eq("b.modelName", testModel))
                .add(Restrictions.eq("lt.name", lineTypeName))
                .add(Restrictions.eq("b.babStatus", BabStatus.CLOSED))
                .setMaxResults(10)
                .list();

        l.forEach(b -> {
            Hibernate.initialize(b.getBab().getBabAlarmHistorys());
        });

        HibernateObjectPrinter.print(l);

    }

    @Autowired
    private WebServiceRV rv;

    @Autowired
    private PassStationRecordService passStationService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testOneToMany() {
        Session session = sessionFactory.getCurrentSession();
        Fqc fqc = session.get(Fqc.class, 780);
        List<FqcTimeTemp> pauseTimeTemps = session.createCriteria(FqcTimeTemp.class).list();
        FqcTimeTemp tempLastRecord = pauseTimeTemps.stream()
                .filter(o -> Objects.equals(o.getFqc(), fqc)).reduce((first, second) -> second)
                .orElse(null);

        assertNotNull(tempLastRecord);

        HibernateObjectPrinter.print(fqc);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabPassStationRecordDAO() {
        Bab b = babDAO.findByPrimaryKey(29710);
        assertNotNull(b);
        List<BabPassStationRecord> l = passStationDAO.findByBab(b);

        List<BabPassStationRecord> l2 = l.stream()
                .sorted(Comparator.comparing(BabPassStationRecord::getBarcode))
                .collect(toList());

        HibernateObjectPrinter.print(l2);

    }

    @Autowired
    private PrepareScheduleDAO dao;

    @Autowired
    private LineUserReferenceDAO lineUserRefDAO;

    @Autowired
    private LineDAO lineDAO;

    @Autowired
    private BabSettingHistoryDAO settingHistoryDAO;

    @Autowired
    private FloorDAO floorDAO;

    Interval rest1 = new Interval(new DateTime().withTime(12, 0, 0, 0), new DateTime().withTime(12, 50, 0, 0));
    Interval rest2 = new Interval(new DateTime().withTime(15, 30, 0, 0), new DateTime().withTime(15, 40, 0, 0));
    Interval rest3 = new Interval(new DateTime().withTime(17, 30, 0, 0), new DateTime().withTime(18, 0, 0, 0));

    DateTimeFormatter df = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");

    Line emptyLine;

    @Test
    @Transactional
    @Rollback(true)
    public void testSchedule() {
        /*
            Auto schedule line at 6:00 am
            Refresh schedule time & people when user change line schedule priority or user's setting
         */
 /*
            Get all user in line setting << how to group as Map<Line, List<LineUserReference>> result?
            Foreach schedule
            Find best user setting & last finished time not > 21:30
            Set schedule line
         */

        Floor floorFive = floorDAO.findByPrimaryKey(1);
        DateTime d = new DateTime("2019-12-16").withTime(0, 0, 0, 0);

        List<Line> line = lineDAO.findBySitefloorAndLineType(floorFive.getName(), 1);

        List<PrepareSchedule> l = dao.findByFloorAndDate(floorFive, d);
        List<LineUserReference> users = lineUserRefDAO.findByLines(line);

        emptyLine = lineDAO.findByPrimaryKey(7);

//        List<PrepareSchedule> testData = l.stream().limit(5).collect(toList());
        List<PrepareSchedule> testData = l;

        System.out.println("Testing " + testData.size() + " datas");

        testData = setScheduleLine(users, testData);

        testData.forEach(s -> {

            System.out.println(s.getLine().getName());

            DateTime start = new DateTime(s.getStartDate());
            DateTime end = new DateTime(s.getEndDate());
            int addTime = Minutes.minutesBetween(start, end).getMinutes();
            System.out.printf("Po: %s, Model: %s, time cost: %d, %s -> %s\r\n", s.getPo(), s.getModelName(), addTime, df.print(start), df.print(end));
            s.getUsers().forEach(ss -> System.out.print(ss.getUsernameCh() + " "));

            System.out.println();

            s.getOtherInfo().forEach((kk, vv) -> System.out.print(vv + ", "));

            System.out.println();
            System.out.println("-------");

        });

    }

    private List<PrepareSchedule> setScheduleLine(List<LineUserReference> users, List<PrepareSchedule> l) {

        //Find modelName fit in settings
        List<String> modelNames = l.stream().map(s -> s.getModelName()).collect(toList());
        List<BabSettingHistory> settings = settingHistoryDAO.findByBabModelNames(modelNames);

        //Find jobnumber fit in settings
        List<BabSettingHistory> jSettings = settings.stream().filter(s -> {
            LineUserReference fitSettings = users.stream()
                    .filter(fw -> fw.getId().getUser().getJobnumber().equals(s.getJobnumber()) && fw.getStation() == s.getStation())
                    .findFirst().orElse(null);
            return fitSettings != null;
        }).collect(toList());

        Map<Line, List<PrepareSchedule>> result = new HashMap();
        List<Line> lines = users.stream().map(u -> u.getId().getLine()).distinct().collect(toList());
        lines = lines.stream().sorted(comparing(Line::getName)).collect(toList());

        //Add empty List into result
        lines.forEach((line) -> {
            result.put(line, new ArrayList());
        });

        result.put(emptyLine, new ArrayList());

        for (PrepareSchedule s : l) {
//            HibernateObjectPrinter.print(s);

            String modelName = s.getModelName();
            List<BabSettingHistory> modelNameFitSetting = jSettings.stream()
                    .filter(bsh -> bsh.getBab().getModelName().equals(modelName))
                    .collect(toList());

            //jSettings group by line & find max number of model experience
            //Map<Line, Integer cnt of users>
            Map<Line, List<Boolean>> historyFitUserSetting = users.stream()
                    .collect(groupingBy(x -> x.getId().getLine(), mapping(f -> {
                        BabSettingHistory obj = modelNameFitSetting.stream()
                                .filter(bsh -> bsh.getJobnumber().equals(f.getId().getUser().getJobnumber()))
                                .findFirst().orElse(null);
                        return obj != null;
                    }, toList())));

            //Check line to add schedule(must schedule time and before 21:30)
            findFitLineSetting(s, historyFitUserSetting, result, new ArrayList());
        }
        
        List<PrepareSchedule> result2 = new ArrayList();

        result.forEach((k, v) -> {
            List<User> settingUsers = users.stream().filter(u -> u.getId().getLine().equals(k))
                    .map(u -> u.getId().getUser())
                    .collect(toList());

            v.forEach(s -> {
                s.setUsers(settingUsers);
            });
            
            result2.addAll(v);
        });

        return result2;
    }

    private void findFitLineSetting(PrepareSchedule currentSchedule, Map<Line, List<Boolean>> lineUserSetting, Map<Line, List<PrepareSchedule>> result, List<Line> removeLine) {

        Map<Line, List<Boolean>> settingClone = new HashMap(lineUserSetting);

        removeLine.forEach((line) -> {
            settingClone.remove(line);
        });

        if (settingClone.isEmpty()) {
            currentSchedule.setLine(emptyLine);
            result.get(emptyLine).add(currentSchedule);
            return;
        }

        //Find best line setting
        Line line = settingClone.entrySet()
                .stream()
                .max((Entry<Line, List<Boolean>> e1, Entry<Line, List<Boolean>> e2) -> {
                    int v1 = e1.getValue().stream().mapToInt(b -> b == true ? 1 : 0).sum();
                    int v2 = e2.getValue().stream().mapToInt(b -> b == true ? 1 : 0).sum();
                    return Integer.compare(v1, v2);
                })
                .get()
                .getKey();

        if (result.isEmpty() || line == null) {
            return;
        }

        //Test and check last schedule time
        List<PrepareSchedule> lineSchedule = result.get(line);
        int usersCnt = lineUserSetting.get(line).size();
        List<PrepareSchedule> testScheduleList = newArrayList(lineSchedule);
        List<Boolean> fitStatus = lineUserSetting.get(line);
        Map<Integer, Boolean> fitStatusMap = IntStream.range(0, fitStatus.size())
                .boxed()
                .collect(toMap(i -> i, fitStatus::get));
        currentSchedule.setLine(line);
        currentSchedule.setOtherInfo(fitStatusMap);
        testScheduleList.add(currentSchedule);
        testScheduleList = scheduleTime(usersCnt, testScheduleList);

        //If last schedule finish time is after 21:30, remove line choose in map
        Date lastScheduleDate = testScheduleList.get(testScheduleList.size() - 1).getEndDate();
        if (new DateTime(lastScheduleDate).isAfter(new DateTime().withTime(21, 30, 0, 0))) {
            removeLine.add(line);
            findFitLineSetting(currentSchedule, lineUserSetting, result, removeLine);
        } else {
            //recursive replace 的物件到底是誰
            result.replace(line, testScheduleList);

            //Null pointer exception because items has been remove from local paramater when recursive
//            return result;
        }
    }

    private List<PrepareSchedule> scheduleTime(int users, List<PrepareSchedule> l) {

        int maxPeopleCnt = users;

        PrepareSchedule prev = null;
        int cnt = 0;

        for (PrepareSchedule w : l) {
            DateTime start = new DateTime().withTime(8, 30, 0, 0);
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
        int rest1Min = Minutes.minutesBetween(rest1.getStart(), rest1.getEnd()).getMinutes();
        int rest2Min = Minutes.minutesBetween(rest2.getStart(), rest2.getEnd()).getMinutes();
        if (isInRestTime(i.getStart())) {
            return new Interval(rest1.getStart(), rest1.getStart().plusMinutes(rest1Min));
        }
        if (isInRestTime2(i.getStart())) {
            return new Interval(rest1.getStart(), rest1.getStart().plusMinutes(rest2Min));
        }
        if (hasOverlap(i, rest1)) {
            return new Interval(i.getStart(), i.getEnd().plusMinutes(rest1Min));
        }
        if (hasOverlap(i, rest2)) {
            return new Interval(i.getStart(), i.getEnd().plusMinutes(rest2Min));
        }

        return i;
    }

    private boolean hasOverlap(Interval t1, Interval t2) {
        return !t1.getEnd().isBefore(t2.getStart()) && !t1.getStart().isAfter(t2.getEnd());
    }

    private boolean isInRestTime(DateTime d) {
        return rest1.getStart().compareTo(d) * d.compareTo(rest1.getEnd()) >= 0;
    }

    private boolean isInRestTime2(DateTime d) {
        return rest2.getStart().compareTo(d) * d.compareTo(rest2.getEnd()) >= 0;
    }

}
