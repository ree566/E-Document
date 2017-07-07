/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.Session;
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
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("from Worktime w join w.bwAvgViews bv where bv.assyAvg > 5");
        
        q.setMaxResults(5);
        
        HibernateObjectPrinter.print(q.list());
    }

}
