/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 幫忙把BAB資料在晚上10點時記錄到資料庫(晚上6點的job 只關閉燈號 txt 1 -> 0)
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.BabStatus;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.model.db1.Fqc;
import com.advantech.service.db1.FqcService;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng Auto close bab and set to abnormal sign on bab isused
 * 避免Auto close on working time, 使用者無法得知系統已經自動關閉逾期未關閉工單
 */
public class HandleUncloseFqc extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(HandleUncloseFqc.class);

    private final FqcService fqcService;

    public HandleUncloseFqc() {
        fqcService = (FqcService) ApplicationContextHelper.getBean("fqcService");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        saveBABData();
    }

    //Auto save the data to Linebalancing_Main if user is not close the 工單.
    private void saveBABData() {

        List<Fqc> unClosed = fqcService.findProcessing();
        log.info("Unclosed fqcList size = " + unClosed.size());

        for (Fqc fqc : unClosed) {
            log.info("Begin save unclose fqc " + fqc.getId());
            fqc.setBabStatus(BabStatus.UNFINSHED);
            fqcService.update(fqc);
            log.info("Close bab status success");
        }

    }

}
