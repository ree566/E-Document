/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
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
public class HibernateTest {

    @Autowired
    private SheetViewService sheetViewService;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SessionFactory sessionFactory;

    //CRUD testing.
//    @Transactional
//    @Rollback(true)
//    @Test
    public void testSheetView() throws Exception {
//        SheetView view = sheetViewService.findAll(new PageInfo().setRows(1)).get(0);
//
//        assertEquals(view.getBwAssyWorktimeAvg(), new BigDecimal(1));
//        assertEquals(view.getBwPackingWorktimeAvg(), new BigDecimal(1));
    }

    @Transactional
    @Rollback(true)
    @Test
    public void testResult() throws Exception {
        List<Worktime> l = worktimeService.findAll(new PageInfo().setMaxNumOfRows(1));
        
        HibernateObjectPrinter.print(l);
        
//        List<BwAvgView> l = w.getBwAvgViews();
//        assertEquals(1, l.size());
//        
//        BwAvgView bv = l.get(0);
//        assertTrue(bv != null);
//        assertTrue(bv.getAssyAvg() != null);
//        
//        assertTrue(new BigDecimal(4.17).compareTo(bv.getAssyAvg()) == 0);
    }

}
