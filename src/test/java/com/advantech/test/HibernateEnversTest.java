/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.XlsWorkBook;
import com.advantech.excel.XlsWorkSheet;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.service.*;
import com.google.common.collect.Iterables;
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
import java.util.Set;
import javax.activation.UnsupportedDataTypeException;
import javax.transaction.Transactional;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.internal.property.EntityPropertyName;
import org.hibernate.envers.query.internal.property.ModifiedFlagPropertyName;
import org.joda.time.DateTime;
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

        try (InputStream inputStream = r.getInputStream()) {
            try (FileOutputStream outputStream = new FileOutputStream(outputFile); FileInputStream excelFile = new FileInputStream(new File(inputLocation))) {
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
        try (FileInputStream excelFile = new FileInputStream(new File(inputLocation))) {
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
        worktimeService.merge(l);
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
    
    @Test
    @Transactional
    @Rollback(true)
    public void tsetEnvers(){
//        AuditQuery q = reader.createQuery()
//                .forRevisionsOfEntity(Worktime.class, true, false)
//                .add(AuditEntity.revisionNumber().maximize().computeAggregationInInstanceContext());
//        List l = q.list();
//        assertEquals(,l.size());
    }

}
