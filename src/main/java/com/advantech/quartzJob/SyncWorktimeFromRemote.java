/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.dao.db1.LineUserReferenceDAO;
import com.advantech.model.db1.LineUserReference;
import com.advantech.model.db1.Worktime;
import com.advantech.service.db1.WorktimeService;
import com.advantech.service.db3.SqlViewService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Component
//@Transactional
public class SyncWorktimeFromRemote {

    private static final Logger logger = LoggerFactory.getLogger(SyncWorktimeFromRemote.class);

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    @Qualifier("sqlViewService3")
    private SqlViewService sqlViewService;

    public void execute() throws Exception {
        List<Worktime> remoteData = sqlViewService.findWorktime();
        List<Worktime> localData = worktimeService.findAll();
        List<Worktime> insertedData = new ArrayList();
        List<Worktime> updatedData = new ArrayList();

        remoteData.forEach(rw -> {
            Worktime lw = localData.stream()
                    .filter(w -> w.getModelName().trim().equals(rw.getModelName().trim()))
                    .findFirst()
                    .orElse(null);

            if (lw == null) {
                rw.setId(0);
                insertedData.add(rw);
            } else {
                rw.setId(lw.getId());
                updatedData.add(rw);
            }
        });
        
        worktimeService.insert(insertedData);
        worktimeService.update(updatedData);

    }

}
