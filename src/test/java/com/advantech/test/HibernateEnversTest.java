/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.XlsWorkBook;
import com.advantech.excel.XlsWorkSheet;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.AuditedRevisionEntity;
import com.advantech.model.Flow;
import com.advantech.model.PreAssy;
import com.advantech.model.Worktime;
import com.advantech.service.*;
import com.advantech.webservice.port.StandardtimeUploadPort;
import com.google.common.collect.Iterables;
import static com.google.common.collect.Lists.newArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.activation.UnsupportedDataTypeException;
import javax.persistence.criteria.JoinType;
import javax.transaction.Transactional;
import org.junit.Assert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditDisjunction;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.internal.property.EntityPropertyName;
import org.hibernate.envers.query.internal.property.ModifiedFlagPropertyName;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateEnversTest {

    private final String regex = "[(\\r\\n|\\n),\" ]+";

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private ResourceLoader resourceLoader;

    List<Worktime> l;

    @Autowired
    private SessionFactory sessionFactory;

    private AuditReader reader;

    @Autowired
    private WorktimeAuditService worktimeAuditService;

    @Before
    public void setUp() {
        Session session = sessionFactory.getCurrentSession();
        reader = AuditReaderFactory.get(session);
    }

    public void setUpList() {
        DateTime d = new DateTime("2017-09-26").withHourOfDay(0);

        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, false)
                .addProjection(AuditEntity.id())
                .add(AuditEntity.id().lt(8607))
                .add(AuditEntity.revisionProperty("REVTSTMP").gt(d.getMillis()))
                .add(AuditEntity.or(
                        AuditEntity.property("assyPackingSop").hasChanged(),
                        AuditEntity.property("testSop").hasChanged()
                ));

        List<Integer> ids = q.getResultList();
        assertEquals(59, ids.size());

        Iterator it = l.iterator();
        while (it.hasNext()) {
            Worktime w = (Worktime) it.next();
            if (ids.contains(w.getId())) {
                it.remove();
            }
        }

        assertEquals(4663, l.size());
    }

    //    @Transactional
//    @Rollback(true)
//    @Test
    public void testHibernateQuery() throws Exception {
        String inputLocation = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\M3SOP-ok.xls";
        String outputFile = "C:\\Users\\Wei.Cheng\\Desktop\\worktime-template.xls";

        String tempfile = "worktime-template.xls";
        Resource r = resourceLoader.getResource("classpath:excel-template\\" + tempfile);

        try ( InputStream inputStream = r.getInputStream()) {
            try ( FileOutputStream outputStream = new FileOutputStream(outputFile);  FileInputStream excelFile = new FileInputStream(new File(inputLocation))) {
                XlsWorkBook workbook = new XlsWorkBook(excelFile);
                assertNotNull(workbook);
                XlsWorkSheet sheet = workbook.getSheet("Sheet1");
                assertNotNull(sheet);

                Map<String, Set> tempSopInfos = this.generateSopInfos(sheet);

                for (Worktime w : l) {
                    w.setAssyPackingSop(combineListInfo(tempSopInfos.get(w.getModelName() + "_ASSY")));
                    w.setTestSop(combineListInfo(tempSopInfos.get(w.getModelName() + "_T1")));
                }

                this.outputFile(l, inputStream, outputStream);
            }
        }
    }

    @Transactional
    @Rollback(false)
//    @Test
    public void updateDbSops() throws Exception {
        setUpList();
        String inputLocation = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\M3SOP-ok.xls";
        try ( FileInputStream excelFile = new FileInputStream(new File(inputLocation))) {
            XlsWorkBook workbook = new XlsWorkBook(excelFile);
            assertNotNull(workbook);
            XlsWorkSheet sheet = workbook.getSheet("Sheet1");
            assertNotNull(sheet);
            Map<String, Set> tempSopInfos = this.generateSopInfos(sheet);
            for (Worktime w : l) {
                w.setAssyPackingSop(combineListInfo(tempSopInfos.get(w.getModelName() + "_ASSY")));
                w.setTestSop(combineListInfo(tempSopInfos.get(w.getModelName() + "_T1")));
            }
            this.mergeData(l);
        }
    }

    private void mergeData(List<Worktime> l) throws Exception {
        worktimeService.mergeWithMesUpload(l);
    }

    private Map<String, Set> generateSopInfos(XlsWorkSheet sheet) throws UnsupportedDataTypeException {
        Map<String, Set> tempSopInfos = new HashMap();
        for (int row = 0, rowCount = sheet.getRowCount(); row < rowCount; row++) {
            String typeNo = sheet.getValue(row, "TYPE_NO").toString();
            String itemNo = sheet.getValue(row, "ITEM_NO").toString();
            String sopName = sheet.getValue(row, "SOP_NAME").toString();
            if (itemNo == null || "".equals(itemNo)) {
                break;
            }
            String sop = this.replaceString(sopName);
            if ("PKG".equals(typeNo) || "T2".equals(typeNo)) {
                continue;
            }
            addInfoIntoSopTemp(tempSopInfos, itemNo + ("ASSY".equals(typeNo) ? "_ASSY" : "_T1"), sop);
        }
        return tempSopInfos;
    }

    private void addInfoIntoSopTemp(Map<String, Set> tempSopInfos, String key, String sop) {
        Set exist = tempSopInfos.get(key);
        if (exist == null) {
            exist = new HashSet();
        }
        exist.add(sop);
        tempSopInfos.put(key, exist);
    }

    private String combineListInfo(Set<String> l) {
        if (l == null || l.isEmpty()) {
            return null;
        }
        String temp = "";
        int i = 0;
        for (String st : l) {
            temp += st;
            if (i++ < l.size()) {
                temp += "\n";
            }
        }
        return temp;
    }

    public Worktime findMatches(final String modelName) {
        return Iterables.find(l, (Worktime input) -> modelName.replaceAll(regex, "").trim().equals(input.getModelName().replaceAll(regex, "").trim()), null);
    }

    private void outputFile(List data, InputStream is, OutputStream os) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Context context = new Context();
        context.putVar("worktimes", data);
        context.putVar("dateFormat", dateFormat);

        Transformer transformer = TransformerFactory.createTransformer(is, os);
//        transformer.getTransformationConfig().setExpressionEvaluator(new JexlExpressionEvaluatorNoThreadLocal());
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();

        //避免Jexl2在javabean值為null時會log
        evaluator.getJexlEngine().setSilent(true);

        JxlsHelper helper = JxlsHelper.getInstance();
        helper.processTemplate(context, transformer);
    }

    public String replaceString(String st) {
        return st.replaceAll(regex, "\n");
    }

    public String replaceWhenNull(String st) {
        return st == null ? "" : st;
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testModifyFlag() {
        Class c = Worktime.class;

        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(c, false, false)
                .add(AuditEntity.id().eq(17991))
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.id());

        Field fields[] = c.getDeclaredFields();
        for (Field field : fields) {
            if (!Collection.class.isAssignableFrom(field.getType()) && !field.isAnnotationPresent(javax.persistence.JoinColumn.class)) {
                String fieldName = field.getName();
                System.out.println(fieldName);
                // for each of your entity's properties
                q.addProjection(AuditEntity.property(fieldName));
                // for the modification properties

                if (!"id".equals(fieldName)) {
                    q.addProjection(new AuditProperty<>(fieldName, new ModifiedFlagPropertyName(new EntityPropertyName(fieldName))));
                }
            }
        }

        List result = q.getResultList();

        HibernateObjectPrinter.print(result);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testEnvers() {
        String[] checkFields = {
            "totalModule", "cleanPanel", "assy", "t1", "t2", "t3", "t4", "packing",
            "flowByTestFlowId", "flowByPackingFlowId", "flowByBabFlowId", "preAssy"
        };
        /*
            Send mail on 11:00 and 16:00
            When 11:00, time range prevDay 16:00 to currentDay 11:00
            When 16:00, time range currentDay 11:00 to currentDay 16:00
            Don't send mail when nothing has changed
         */

        DateTime now = new DateTime().withMinuteOfHour(0).withSecondOfMinute(0);
        int currentHour = now.getHourOfDay();

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy/M/d HH:mm:SS");
        DateTime sD = currentHour <= 11 ? now.minusDays(1).withHourOfDay(16) : now.withHourOfDay(11);
        DateTime eD = currentHour <= 11 ? now.withHourOfDay(11) : now.withHourOfDay(16);

        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, false);

        AuditDisjunction disjunction = AuditEntity.disjunction();

        for (String field : checkFields) {
            disjunction.add(AuditEntity.property(field).hasChanged());
        }

        q.add(disjunction);
        q.add(AuditEntity.revisionType().eq(RevisionType.MOD));
        q.add(AuditEntity.revisionProperty("REVTSTMP").between(sD.getMillis(), eD.getMillis()));

        List<Object[]> ws = q.getResultList();

        if (ws.isEmpty()) {
            System.out.println("Nothing has changed");
        }

        for (int i = 0; i < ws.size(); i++) {
            System.out.println("------------------");
            Object[] triplet = ws.get(i);

            Worktime entity = (Worktime) triplet[0];
            AuditedRevisionEntity revisionEntity = (AuditedRevisionEntity) triplet[1];
            RevisionType revisionType = (RevisionType) triplet[2];

            Worktime prevEntity = this.getPreviousVersion(entity, revisionEntity.getREV());

            System.out.printf("ModelName: %s, AUD: %d, ChangeBy: %s \r\n", entity.getModelName(), revisionEntity.getREV(), revisionEntity.getUsername());
            for (String field : checkFields) {
                try {
                    Object o1 = PropertyUtils.getProperty(entity, field);
                    Object o2 = PropertyUtils.getProperty(prevEntity, field);
                    if (!Objects.equals(o1, o2)) {
                        if (o1 instanceof Flow) {
                            if (equalsWithId(o1, o2) == false) {
                                System.out.printf("Different on %s %s -> %s \r\n", field, o2 == null ? "null" : ((Flow) o2).getName(), o1 == null ? "null" : ((Flow) o1).getName());
                            }
                        } else if (o1 instanceof PreAssy) {
                            if (equalsWithId(o1, o2) == false) {
                                System.out.printf("Different on %s %s -> %s \r\n", field, o2 == null ? "null" : ((PreAssy) o2).getName(), o1 == null ? "null" : ((PreAssy) o1).getName());
                            }
                        } else {
                            System.out.printf("Different on %s %s -> %s \r\n", field, o2 == null ? "null" : o2.toString(), o1 == null ? "null" : o1.toString());
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
            System.out.println("------------------");
        }
    }

    private boolean equalsWithId(Object o1, Object o2) throws Exception {
        return Objects.equals(PropertyUtils.getProperty(o1, "id"), PropertyUtils.getProperty(o2, "id"));
    }

    private Worktime getPreviousVersion(Worktime entity, int current_rev) {

        Number prior_revision = (Number) reader.createQuery()
                .forRevisionsOfEntity(entity.getClass(), false, true)
                .addProjection(AuditEntity.revisionNumber().max())
                .add(AuditEntity.id().eq(entity.getId()))
                .add(AuditEntity.revisionNumber().lt(current_rev))
                .getSingleResult();

        if (prior_revision != null) {
            return reader.find(entity.getClass(), entity.getId(), prior_revision);
        } else {
            return null;
        }
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testRevisionEntity() {
        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, false)
                .add(AuditEntity.id().eq(9753));
    }

    @Autowired
    private StandardtimeUploadPort port;

//    @Test
    @Transactional
    @Rollback(true)
    public void testRevisionQuery() throws Exception {
        Worktime w = (Worktime) reader.createQuery()
                .forEntitiesAtRevision(Worktime.class, 13208)
                .add(AuditEntity.id().eq(9295))
                .getSingleResult();

        assertNotNull(w);

        port.initSettings();
        port.update(w);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testRevisionQuery2() throws Exception {
        String seriseModel = "POC";

        List<Integer> del = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, true)
                .addProjection(AuditEntity.id())
                .add(AuditEntity.property("modelName").like(seriseModel, MatchMode.START))
                .add(AuditEntity.revisionType().eq(RevisionType.DEL))
                .getResultList();

        l = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, false)
                .addProjection(AuditEntity.property("modelName"))
                .addProjection(AuditEntity.property("productionWt"))
                .add(AuditEntity.property("modelName").like(seriseModel, MatchMode.START))
                .add(AuditEntity.property("productionWt").hasChanged())
                .add(AuditEntity.not(AuditEntity.id().in(del)))
                .addOrder(AuditEntity.property("modelName").asc())
                .getResultList();

        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testRevisionQuery3() throws Exception {
        List<Worktime> history = worktimeAuditService.findModifiedAtRevision(29032);
        List<Worktime> revertTargetData = worktimeAuditService.forEntityAtRevision(29031);

        Session session = sessionFactory.getCurrentSession();

        List<Worktime> dataInDb = session.createCriteria(Worktime.class).list();

        HibernateObjectPrinter.print(history.size());
        HibernateObjectPrinter.print(revertTargetData.size());

        assertTrue(history.size() == 688, "Size not equals 688");
        assertTrue(revertTargetData.size() != 1, "revertTargetData Size equals 1");

        history.forEach(d -> {
            Worktime w = dataInDb.stream()
                    .filter(wk -> wk.getId() == d.getId()).findFirst()
                    .orElse(null);

            Worktime revertTarget = revertTargetData.stream()
                    .filter(r -> r.getId() == d.getId()).findFirst()
                    .orElse(null);

            if (w != null && revertTarget != null) {
                w.setAssy(revertTarget.getAssy());
                w.setT1(revertTarget.getT1());
                w.setT2(revertTarget.getT2());
                w.setT3(revertTarget.getT3());
                w.setT4(revertTarget.getT4());
                w.setPacking(revertTarget.getPacking());

                w.setDefaultProductWt();
                w.setDefaultSetupTime();
                w.setDefaultAssyToT1();
                w.setDefaultT2ToPacking();
                w.setDefaultAssyKanbanTime();
                w.setDefaultAssyStation();
                w.setDefaultPackingKanbanTime();
                w.setDefaultPackingStation();
                w.setDefaultCleanPanelAndAssembly();

                HibernateObjectPrinter.print(w.getModelName());
                session.merge(w);
            }
        });
        HibernateObjectPrinter.print(l);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void testRevisionQuery4() throws Exception {
        List<Worktime> result = worktimeAuditService.findModifiedAtRevision(38932);
//        q.traverseRelation("pending", JoinType.LEFT);

        System.out.println(result.size());
//        HibernateObjectPrinter.print(result.stream().map(Worktime::getCobots).collect(toList()));
//        HibernateObjectPrinter.print(result);
//        HibernateObjectPrinter.print(result.get(0));
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testLastStatus() {
        Worktime w = worktimeService.findByPrimaryKey(8066);
        Worktime rowLastStatus = (Worktime) worktimeAuditService.findLastStatusBeforeUpdate(w.getId());
        System.out.println((int) w.getPartNoAttributeMaintain());
        System.out.println((int) rowLastStatus.getPartNoAttributeMaintain());
        System.out.println((int) '5');
        System.out.println(Objects.equals(w.getPartNoAttributeMaintain(), rowLastStatus.getPartNoAttributeMaintain()));
        System.out.println(Objects.equals((int) w.getPartNoAttributeMaintain(), (int) rowLastStatus.getPartNoAttributeMaintain()));

    }

}
