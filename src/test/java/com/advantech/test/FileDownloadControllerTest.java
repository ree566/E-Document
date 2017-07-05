/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.service.SheetViewService;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import junit.framework.Assert;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
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
    "classpath:servlet-context.xml",
    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class FileDownloadControllerTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SheetViewService sheetViewService;

//    @Test
    public void testFileDownloadFromTemp() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(get("/Worktime/excel2").accept("application/json"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
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
                evaluator.getJexlEngine().setSilent(true);

                JxlsHelper helper = JxlsHelper.getInstance();
                helper.processTemplate(context, transformer);
//                .processTemplate(is, os, context);
            }
        }
    }

//    @Test
    public void testInaccessibleProperty() throws Exception {
        JexlEngine engine = new JexlBuilder().strict(true).create(); // 1332806
        JexlContext context = new MapContext();
        context.set("foo", new Foo138());
        Assert.assertEquals("baz", engine.createExpression("foo.baz").evaluate(context));
        Assert.assertEquals(null, engine.createExpression("foo.bar").evaluate(context));
    }

    public static class Foo138 {

        public String getBar() {
            return null;
        }

        public String getBaz() {
            return "baz";
        }
    }
}
