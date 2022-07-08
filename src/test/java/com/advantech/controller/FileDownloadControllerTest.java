/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.converter.CobotConverter;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class FileDownloadControllerTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SheetViewService sheetViewService;

    @Autowired
    private WorktimeService worktimeService;

//    @Test
    public void testFileDownloadFromTemp() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(get("/Worktime/excel2").accept("application/json"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(MockMvcResultHandlers.print());

    }

//    @Test
    public void testAddDataToTemp() throws Exception {
        Resource r = resourceLoader.getResource("classpath:excel-template\\Plant-sp matl status(M3).xls");
        try (InputStream is = r.getInputStream()) {
            List<SheetView> l = sheetViewService.findAll(new PageInfo().setRows(1));
            try (OutputStream os = new FileOutputStream("C:\\Users\\Wei.Cheng\\Desktop\\object_collection_output.xls")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Context context = new Context();
                context.putVar("sheetViews", l);
                context.putVar("dateFormat", dateFormat);

                Transformer transformer = TransformerFactory.createTransformer(is, os);
                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
                evaluator.getJexlEngine().setSilent(false);

                JxlsHelper helper = JxlsHelper.getInstance();
                helper.processTemplate(context, transformer);
//                .processTemplate(is, os, context);
            }
        }
    }
    
    @Autowired
    private CobotConverter cobotConverter;

    @Test
//    @Transactional
//    @Rollback(true)
    public void testAddDataToTemp2() throws Exception {
        Resource r = resourceLoader.getResource("classpath:excel-template\\worktime-template.xls");
        try (InputStream is = r.getInputStream()) {
            
            PageInfo info = new PageInfo();
            info.setRows(-1);
//            info.setSearchField("modelName");
//            info.setSearchString("UNO1372GE3A1604E-T,UNO-2473G-J3AE,UNO-2473G-E3AE,UNO2473GE3A1602E-T,UNO-1372G-E3AE");
//            info.setSearchOper("in");
            
            List<Worktime> l = worktimeService.findWithFullRelation(info);
//            assertEquals(5, l.size());
//            assertEquals(3, l.get(0).getCobots().size());
            HibernateObjectPrinter.print(l.get(0).getCobots());
            
            try (OutputStream os = new FileOutputStream("C:\\Users\\Wei.Cheng\\Desktop\\worktime_output.xls")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                Context context = new Context();
                context.putVar("worktimes", l);
                context.putVar("dateFormat", dateFormat);
                context.putVar("cobotConverter", cobotConverter);

                Transformer transformer = TransformerFactory.createTransformer(is, os);
                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
                evaluator.getJexlEngine().setSilent(true);

                JxlsHelper helper = JxlsHelper.getInstance();
                helper.processTemplate(context, transformer);
            }
        }
    }

}
