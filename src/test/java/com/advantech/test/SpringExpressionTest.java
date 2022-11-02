/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.EmployeeZoneUtils;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.SpringExpressionUtils;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import com.advantech.service.WorktimeMaterialPropertyUploadSettingService;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringExpressionTest {

    @Autowired
    private SpringExpressionUtils expressionUtils;

    @Autowired
    private WorktimeMaterialPropertyUploadSettingService matSettingService;

    @Autowired
    private WorktimeService worktimeService;

    @Test
    public void testExp() throws JsonProcessingException {
        Worktime w = worktimeService.findByPrimaryKey(8850);
        List<WorktimeMaterialPropertyUploadSetting> l = matSettingService.findAll();

        HibernateObjectPrinter.print(w);

        l.forEach((setting) -> {
            Object o = expressionUtils.getValueFromFormula(w, setting.getFormula());
            System.out.println(setting.getColumnName() + ": " + o);
        });
        
        
    }
    
    @Autowired
    private EmployeeZoneUtils ezUtils;
    
    @Test
    public void testRestApi() {
        HibernateObjectPrinter.print(ezUtils.findUser("A-7568"));
    }
    
}
