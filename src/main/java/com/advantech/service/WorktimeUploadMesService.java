/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditDAO;
import com.advantech.model.Worktime;
import com.advantech.webservice.port.SopUploadPort;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WorktimeUploadMesService {

    @Autowired
    private AuditDAO auditDAO;

    @Autowired
    private SopUploadPort sopUploadPort;

    public void uploadToMesWithCheckRevision(Worktime w) throws Exception {
        Worktime rowLastStatus = (Worktime) auditDAO.findLastStatusBeforeUpdate(Worktime.class, w.getId());
        if (isSopFieldsChanged(rowLastStatus, w)) {
            this.uploadToMes(w);
        }
    }

    public void uploadToMes(Worktime w) throws Exception {
        try {
            sopUploadPort.upload(w);
        } catch (Exception e) {
            throw new Exception("大表更新成功，SOP同步至MES失敗<br />" + e.getMessage());
        }
    }

    private boolean isSopFieldsChanged(Worktime prev, Worktime current) {
        return !prev.getModelName().equals(current.getModelName())
                || !Objects.equals(prev.getAssyPackingSop(), current.getAssyPackingSop())
                || !Objects.equals(prev.getTestSop(), current.getTestSop());
    }
}
