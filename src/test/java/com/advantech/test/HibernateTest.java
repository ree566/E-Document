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
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static junit.framework.Assert.assertNotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
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
public class HibernateTest {

    private final String regex = "[(\\r\\n|\\n),\" ]+";

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AuditService auditService;

    private static Validator validator;

    List<Worktime> l;

    @Autowired
    private ResourceLoader resourceLoader;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        l = worktimeService.findByPrimaryKeys(8140);
    }

//    @Transactional
//    @Rollback(true)
//    @Test
    public void testAudit() throws JsonProcessingException {
        DateTime d = new DateTime("2017-09-26").withHourOfDay(0);

        Session session = sessionFactory.getCurrentSession();
        AuditReader reader = AuditReaderFactory.get(session);
        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, true)
                .add(AuditEntity.revisionProperty("REVTSTMP").gt(d.getMillis()))
                .add(AuditEntity.or(
                        AuditEntity.property("assyPackingSop").hasChanged(),
                        AuditEntity.property("testSop").hasChanged()
                ));
        HibernateObjectPrinter.print(q.getResultList());
    }

//    CRUD testing.
//    @Transactional
//    @Rollback(true)
//    @Test
    public void test() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        AuditReader reader = AuditReaderFactory.get(session);
        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, true);
        // if you want revision properties like revision number/type etc
        //                .addProjection(AuditEntity.revisionNumber())
        // for your normal entity properties
        //                .addProjection(AuditEntity.id())

//        q.addProjection(AuditEntity.selectEntity(false));
        q.add(AuditEntity.id().eq(8394));

        List resultList = q.getResultList();

        HibernateObjectPrinter.print(resultList);
    }

    private String[] getAllFields() {
        Worktime w = new Worktime();
        Class objClass = w.getClass();

        List<String> list = new ArrayList<>();
        // Get the public methods associated with this class.
        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && !name.startsWith("setDefault")) {
                list.add(lowerCaseFirst(name.substring(3)));
            }
        }
        return list.toArray(new String[0]);
    }

    private String lowerCaseFirst(String st) {
        StringBuilder sb = new StringBuilder(st);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    @Transactional
    @Rollback(true)
    @Test
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
                this.clearSops(l);
                for (int row = 0, rowCount = sheet.getRowCount(); row < rowCount; row++) {
                    String typeNo = sheet.getValue(row, "TYPE_NO").toString();
                    String itemNo = sheet.getValue(row, "ITEM_NO").toString();
                    String sopName = sheet.getValue(row, "SOP_NAME").toString();

                    if (itemNo == null || "".equals(itemNo)) {
                        break;
                    }

                    Worktime w = this.findMatches(itemNo);
                    if (w != null) {
                        String sop = this.replaceString(sopName);
                        if ("ASSY".equals(typeNo)) {
                            w.setAssyPackingSop(replaceWhenNull(w.getAssyPackingSop()) + "\n" + sop);
                        } else if ("T1".equals(typeNo)) {
                            w.setTestSop(replaceWhenNull(w.getTestSop()) + "\n" + sop);
                        }
                    }
                }
                this.outputFile(l, inputStream, outputStream);
            }
        }
    }

    public void clearSops(List<Worktime> l) {
        for (Worktime w : l) {
            w.setAssyPackingSop("--");
            w.setTestSop("--");
        }
    }

    public Worktime findMatches(final String modelName) {
        return Iterables.find(l, new Predicate<Worktime>() {
            @Override
            public boolean apply(Worktime input) {
                return modelName.replaceAll(regex, "").trim().equals(input.getModelName().replaceAll(regex, "").trim());
            }
        }, null);
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
}
