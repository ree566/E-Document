/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Type;
import com.advantech.model.Worktime;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;

/**
 *
 * @author Wei.Cheng
 */

public class ExcelTest {

    private final String xmlConfig = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\worktime.xml";
    private final String dataXLS = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\worktime-template.xls";

//    @Test
    public void testJxls() throws Exception {
        try (InputStream inputXML = new FileInputStream(new File(xmlConfig))) {
            assertNotNull(inputXML);
            
            XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
            
            try (InputStream inputXLS = new FileInputStream(new File(dataXLS))) {
                assertNotNull(inputXLS);
                
                List<Worktime> l = new ArrayList();
                Map beans = new HashMap();
                beans.put("worktimes", l);
                beans.put("type", new Type());
                XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
                assertTrue(readStatus.isStatusOK());
                System.out.println(new Gson().toJson(l));
            }
        }
    }
}
