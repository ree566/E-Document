/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditDAO;
import com.advantech.model.Worktime;
import com.advantech.webservice.port.ModelResponsorUploadPort;
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

    @Autowired
    private ModelResponsorUploadPort responsorUploadPort;

    public void uploadToMes(Worktime w, boolean checkRevision) throws Exception {
        if (checkRevision) {
            this.uploadWithCheckRevision(w);
        } else {
            this.uploadWithoutCheckRevision(w);
        }
    }

    public void deleteOnMes(Worktime w) throws Exception {
        sopUploadPort.delete(w);
        responsorUploadPort.delete(w);
    }

    private void uploadWithCheckRevision(Worktime w) throws Exception {
        Worktime rowLastStatus = (Worktime) auditDAO.findLastStatusBeforeUpdate(Worktime.class, w.getId());
        if (isSopFieldsChanged(rowLastStatus, w)) {
            this.uploadSop(w);
        }
        if (isModelResponsorChanged(rowLastStatus, w)) {
            this.uploadModelResponsor(w);
        }
    }

    private void uploadWithoutCheckRevision(Worktime w) throws Exception {
        this.uploadSop(w);
        this.uploadModelResponsor(w);
    }

    private void uploadSop(Worktime w) throws Exception {
        try {
            sopUploadPort.update(w);
        } catch (Exception e) {
            throw new Exception("SOP同步至MES失敗<br />" + e.getMessage());
        }
    }

    private void uploadModelResponsor(Worktime w) throws Exception {
        try {
            responsorUploadPort.update(w);
        } catch (Exception e) {
            throw new Exception("機種負責人同步至MES失敗<br />" + e.getMessage());
        }
    }

    private boolean isModelNameChanged(Worktime prev, Worktime current) {
        return !prev.getModelName().equals(current.getModelName());
    }

    private boolean isSopFieldsChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !Objects.equals(prev.getAssyPackingSop(), current.getAssyPackingSop())
                || !Objects.equals(prev.getTestSop(), current.getTestSop());
    }

    //Revision entity relation object are lasy loading.
    private boolean isModelResponsorChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !Objects.equals(prev.getUserBySpeOwnerId(), current.getUserBySpeOwnerId())
                || !Objects.equals(prev.getUserByEeOwnerId(), current.getUserByEeOwnerId())
                || !Objects.equals(prev.getUserByQcOwnerId(), current.getUserByQcOwnerId());
    }
}
