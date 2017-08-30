/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class BackupDataToExcel {

    private final String filePath = "\\\\ACLOA\\New_Group\\Dong_Hu_Plant_Public\\生產部公用資料夾\\工時資料&產能\\worktime-bak.xls";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private WorktimeService worktimeService;

    public void backupToDisk() throws Exception {
        Resource r = resourceLoader.getResource("classpath:excel-template\\worktime-bak.xls");

        try (InputStream is = r.getInputStream()) {
            PageInfo info = new PageInfo();
            info.setRows(Integer.MAX_VALUE);
            List<Worktime> l = worktimeService.findWithFullRelation(info);
            try (OutputStream os = new FileOutputStream(filePath)) {
                this.outputFile(l, is, os);
            }
        }
    }

    private void outputFile(List data, InputStream is, OutputStream os) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Context context = new Context();
        context.putVar("worktimes", data);
        context.putVar("dateFormat", dateFormat);

        Transformer transformer = TransformerFactory.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();

        //避免Jexl2在javabean值為null時會log
        evaluator.getJexlEngine().setSilent(true);

        JxlsHelper helper = JxlsHelper.getInstance();
        helper.processTemplate(context, transformer);

        is.close();
        os.close();
    }

}
