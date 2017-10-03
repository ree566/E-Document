/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/WorktimeDownload")
public class FileDownloadController {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

    @Autowired
    private SheetViewService sheetViewService;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ResourceLoader resourceLoader;

    @ResponseBody
    @RequestMapping(value = "/excel", method = {RequestMethod.GET})
    protected ModelAndView generateExcel(PageInfo info) {
        // create some sample data
        info.setRows(Integer.MAX_VALUE);
        info.setSidx("id");
        info.setSord("asc");
        info.setPage(1); //Prevent select query jump to page 2 bug.

        List<SheetView> l = sheetViewService.findAll(info);
        ModelAndView mav = new ModelAndView("ExcelRevenueSummary");
        mav.addObject("revenueData", l);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/excel2", method = {RequestMethod.GET})
    protected void generateExcel2(HttpServletResponse response, PageInfo info) throws Exception {
        this.fileExport("worktime-template.xls", response, info);
    }

    @ResponseBody
    @RequestMapping(value = "/excelForSpe", method = {RequestMethod.GET})
    protected void generateExcelForUpload(HttpServletResponse response, PageInfo info) throws Exception {
        this.fileExport("Plant-sp matl status(M3) (2).xls", response, info);
    }

    private void fileExport(String tempfileName, HttpServletResponse response, PageInfo info) throws Exception {

        Resource r = resourceLoader.getResource("classpath:excel-template\\" + tempfileName);

        //Set filedownload header
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + r.getFilename() + "\""));

        //Adjust search query and search data
        info.setRows(Integer.MAX_VALUE);
        info.setSidx("id");
        info.setSord("asc");
        info.setPage(1); //Prevent select query jump to page 2 bug.

        //Do nothing when empty result
        List<Worktime> l = worktimeService.findWithFullRelation(info);
        if (l.isEmpty()) {
            throw new Exception("Test");
        }

        try (InputStream is = r.getInputStream()) {
            try (OutputStream os = response.getOutputStream()) {
                this.outputFile(l, is, os);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void outputFile(List data, InputStream is, OutputStream os) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Number revisionNum = auditService.findLastRevisions(Worktime.class);
        String revisionInfo = this.encodeRevisionInfo(revisionNum);

        Context context = new Context();
        context.putVar("worktimes", data);
        context.putVar("dateFormat", dateFormat);
        context.putVar("revision", revisionInfo);

        Transformer transformer = TransformerFactory.createTransformer(is, os);
//        transformer.getTransformationConfig().setExpressionEvaluator(new JexlExpressionEvaluatorNoThreadLocal());
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();

        //避免Jexl2在javabean值為null時會log
        evaluator.getJexlEngine().setSilent(true);

        JxlsHelper helper = JxlsHelper.getInstance();
        helper.processTemplate(context, transformer);
    }

    private String encodeRevisionInfo(Number revisionNumber) {
        String encodeString = "revision: " + revisionNumber;
        byte[] bytesEncoded = Base64.encodeBase64(encodeString.getBytes());
        return new String(bytesEncoded);
    }
}
