/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.ie.WorktimeStandardtimeUploadPort;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    "classpath:servlet-context.xml",
    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class StandardtimeUploadTest {

    @Autowired
    private WorktimeStandardtimeUploadPort port;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private WorktimeAutouploadSettingService settingService;

    @Transactional
    @Rollback(true)
    @Test
    public void testUpload() {
        PageInfo info = new PageInfo();

        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        info.setRows(Integer.MAX_VALUE);
        info.setSearchField("modifiedDate");
        info.setSearchOper("gt");
        info.setSearchString(df.print(new DateTime("2017-08-27")));

        List<Worktime> l = worktimeService.findAll(info);

        assertEquals(26, l.size());

        port.initSettings(newArrayList(settingService.findByPrimaryKey(16)));

        List errors = new ArrayList();
        for (Worktime w : l) {
            try {
                port.uploadStandardTime(w);
            } catch (Exception e) {
                errors.add(e);
            }
        }

        assertTrue(errors.isEmpty());

    }
}
