/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.excel;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.commons.jexl2.JexlEngine;
import org.jxls.expression.JexlExpressionEvaluatorNoThreadLocal;
import org.jxls.transform.TransformationConfig;
import org.jxls.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class JxlsExcelView extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(JxlsExcelView.class);

    private static final String CONTENT_TYPE = "application/vnd.ms-excel";

    private final String templatePath;
    private final String exportFileName;

    /**
     * @param templatePath 模版相对于当前classpath路径
     * @param exportFileName 导出文件名
     */
    public JxlsExcelView(String templatePath, String exportFileName) {
        this.templatePath = templatePath;
        if (exportFileName != null) {
            try {
                exportFileName = URLEncoder.encode(exportFileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        this.exportFileName = exportFileName;
        setContentType(CONTENT_TYPE);
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Context context = new Context(model);
        
        response.setContentType(getContentType());
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + exportFileName + "\""));
        
        ServletOutputStream os = response.getOutputStream();
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(templatePath)) {
            processJxlsTemplate(is, os, context);
        }
    }

    private void processJxlsTemplate(InputStream is, OutputStream os, Context context) throws Exception {
        JxlsHelper helper = JxlsHelper.getInstance();
        Transformer transformer = helper.createTransformer(is, os);
        TransformationConfig config = transformer.getTransformationConfig();
        config.setExpressionEvaluator(new JexlExpressionEvaluatorNoThreadLocal());
        JexlExpressionEvaluatorNoThreadLocal evaluator = (JexlExpressionEvaluatorNoThreadLocal) config.getExpressionEvaluator();

        //避免Jexl2在javabean值為null時會log
        JexlEngine engine = evaluator.getJexlEngine();
        engine.setSilent(true); // will throw errors now for selects that don't evaluate properly
//        engine.setLenient(false);
//        engine.setDebug(true);
        helper.processTemplate(context, transformer);
    }
}
