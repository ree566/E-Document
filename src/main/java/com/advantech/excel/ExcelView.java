/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.excel;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 *
 * @author Wei.Cheng
 */
public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest req, HttpServletResponse res) throws Exception {
        res.setHeader("Content-Disposition", "attachment; filename=\"sample.xlsx\"");
        List<Object> datas = (List<Object>) map.get("revenueData");
        if (map.containsKey("templateWorkbook")) {
            Object o = map.get("templateWorkbook");
            if (o != null) {
                workbook = (Workbook) o;
            }
        }
        ExcelGenerator generator = new ExcelGenerator(workbook);
        generator.generateWorkBooks(datas);
    }
}
