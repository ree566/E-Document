/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.ExcelGenerator;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.ModelResponsorQueryPort;
import com.advantech.webservice.unmarshallclass.ModelResponsor;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng Only check the field which MES has provide the XML query
 * port.
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
//    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(true)
public class RemoteAndLocalDataTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ModelResponsorQueryPort responsorQryPort;

    @Autowired
    private WorktimeService worktimeService;

    private List<Worktime> l;

    @Before
    public void init() {
//        Criteria c = sessionFactory.getCurrentSession().createCriteria(Worktime.class);
//        c.setFetchMode("userByEeOwnerId", FetchMode.EAGER);
//        c.setFetchMode("userByQcOwnerId", FetchMode.EAGER);
//        c.setFetchMode("userBySpeOwnerId", FetchMode.EAGER);
//        l = c.list();
        l = worktimeService.findAll();
    }

    @Test
    public void test() throws Exception {
        ExcelGenerator generator = new ExcelGenerator();
        List<Map> errors = new ArrayList();
        for (Worktime w : l) {
            List<ModelResponsor> mesData = responsorQryPort.query(w);

            if (!isUserTheSame(w, mesData)) {
                Map m = new HashMap();
                m.put("modelName", w.getModelName());
                m.put("speOwner_local", w.getUserBySpeOwnerId().getJobnumber());
                User eeOwner = w.getUserByEeOwnerId();
                m.put("eeOwner_local", eeOwner == null ? "n/a" : eeOwner.getJobnumber());
                m.put("qcOwner_local", w.getUserByQcOwnerId().getJobnumber());

                Predicate<ModelResponsor> speFilter = resp -> resp.getDeptId() == 19;
                Predicate<ModelResponsor> eeFilter = resp -> resp.getDeptId() == 17;
                Predicate<ModelResponsor> qcFilter = resp -> resp.getDeptId() == 15;
                ModelResponsor speOwnerRemote = FluentIterable.from(mesData).filter(speFilter).first().orNull();
                ModelResponsor eeOwnerRemote = FluentIterable.from(mesData).filter(eeFilter).first().orNull();
                ModelResponsor qcOwnerRemote = FluentIterable.from(mesData).filter(qcFilter).first().orNull();
                m.put("speOwner_remote", speOwnerRemote == null ? "n/a" : speOwnerRemote.getUserNo());
                m.put("eeOwner_remote", eeOwnerRemote == null ? "n/a" : eeOwnerRemote.getUserNo());
                m.put("qcOwner_remote", qcOwnerRemote == null ? "n/a" : qcOwnerRemote.getUserNo());

                errors.add(m);
            }
        }
        if (!errors.isEmpty()) {
            generator.generateWorkBooks(errors);
            generator.outputExcel("conflict");
        }
    }

    private boolean isUserTheSame(Worktime w, List<ModelResponsor> l) {
        Set<String> s1 = new HashSet();
        Set<String> s2 = new HashSet();
        User eeOwner = w.getUserByEeOwnerId();
        if (eeOwner != null) {
            s1.add(eeOwner.getJobnumber());
        }
        s1.add(w.getUserBySpeOwnerId().getJobnumber());
        s1.add(w.getUserByQcOwnerId().getJobnumber());

        l.forEach((m) -> {
            s2.add(m.getUserNo());
        });

        return s1.containsAll(s2);
    }

}
