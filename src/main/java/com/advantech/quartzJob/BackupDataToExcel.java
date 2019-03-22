/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author Wei.Cheng
 */
public class BackupDataToExcel {

    private static final Logger log = LoggerFactory.getLogger(BackupDataToExcel.class);

    @Value("${WORKTIME.BACKUP.LOCATION}")
    private String filePath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private WorktimeService worktimeService;

    //Backup file password: 12345678
    private final String backupFileName = "worktime-bak.xls";

    public void backupToDisk() throws Exception {
        checkFilePath();

        Resource r = resourceLoader.getResource("classpath:excel-template\\" + backupFileName);

        try (InputStream is = r.getInputStream()) {
            PageInfo info = new PageInfo();
            info.setRows(-1);
            List<Worktime> l = worktimeService.findWithFullRelation(info);
            try (OutputStream os = new FileOutputStream(filePath + backupFileName)) {
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

    private void checkFilePath() throws FileNotFoundException {
        if (!Files.isWritable(Paths.get(filePath))) {
            throw new FileNotFoundException("File path \"" + filePath + "\" not exist.");
        }
    }

}
