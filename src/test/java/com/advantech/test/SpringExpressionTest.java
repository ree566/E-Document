/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.SpringExpressionUtils;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeMaterialPropertyUploadSettingService;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import static org.junit.Assert.assertTrue;
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

        PageInfo info = new PageInfo();
        info.setRows(-1);
        info.setSearchField("modelName");
        info.setSearchString("IDK1112P1902-T");
        info.setSearchOper("in");

        List<Worktime> l = worktimeService.findWithFullRelation(info);
//        List<WorktimeMaterialPropertyUploadSetting> l = matSettingService.findAll();

        assertTrue(!l.isEmpty());

        Worktime w = l.get(0);
        HibernateObjectPrinter.print(w);

        String formula1 = "flowByBabFlowId.name";
        String formula2 = "flowByBabFlowId.name.contains(\"RI\") ? upBiRi : 0";

        Object o1 = expressionUtils.getValueFromFormula(w, formula1);
        Object o2 = expressionUtils.getValueFromFormula(w, formula2);
        System.out.println(o1);
        System.out.println(o2);

    }
    
    @Test
    public void testExp2() throws JsonProcessingException {
        String exp = "nsInOneCollectionBox.intValue() == 0 ? new java.math.BigDecimal(0.8) : new java.math.BigDecimal(0.8 / nsInOneCollectionBox)";
        Worktime worktime = worktimeService.findByPrimaryKey(16204);
        Object o1 = expressionUtils.getValueFromFormula(worktime, exp);
        System.out.println(o1);
        System.out.println(o1.getClass());
    }
}
