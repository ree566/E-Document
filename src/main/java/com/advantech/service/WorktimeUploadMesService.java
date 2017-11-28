/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.webservice.port.FlowUploadPort;
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

    @Autowired
    private FlowUploadPort flowUploadPort;

    public void insert(Worktime w) throws Exception {
        try {
            sopUploadPort.insert(w);
        } catch (Exception e) {
            throw new Exception("SOP新增至MES失敗<br />" + e.getMessage());
        }

        try {
            responsorUploadPort.insert(w);
        } catch (Exception e) {
            throw new Exception("機種負責人新增至MES失敗<br />" + e.getMessage());
        }

        try {
            flowUploadPort.insert(w);
        } catch (Exception e) {
            throw new Exception("徒程新增至MES失敗<br />" + e.getMessage());
        }
    }

    public void update(Worktime w) throws Exception {
        Worktime rowLastStatus = (Worktime) auditDAO.findLastStatusBeforeUpdate(Worktime.class, w.getId());
        if (isSopChanged(rowLastStatus, w)) {
            try {
                sopUploadPort.update(w);
            } catch (Exception e) {
                throw new Exception("SOP更新至MES失敗<br />" + e.getMessage());
            }
        }

        if (isModelResponsorChanged(rowLastStatus, w)) {
            try {
                responsorUploadPort.update(w);
            } catch (Exception e) {
                throw new Exception("機種負責人更新至MES失敗<br />" + e.getMessage());
            }
        }

        if (isFlowChanged(rowLastStatus, w)) {
            try {
                flowUploadPort.update(w);
            } catch (Exception e) {
                throw new Exception("徒程更新至MES失敗<br />" + e.getMessage());
            }
        }

    }

    private boolean isModelNameChanged(Worktime prev, Worktime current) {
        return !prev.getModelName().equals(current.getModelName());
    }

    private boolean isSopChanged(Worktime prev, Worktime current) {
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

    //Revision entity relation object are lasy loading.
    private boolean isFlowChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !Objects.equals(prev.getPreAssy(), current.getPreAssy())
                || !Objects.equals(prev.getFlowByBabFlowId(), current.getFlowByBabFlowId())
                || !Objects.equals(prev.getFlowByTestFlowId(), current.getFlowByTestFlowId())
                || !Objects.equals(prev.getFlowByPackingFlowId(), current.getFlowByPackingFlowId());
    }

    public void delete(Worktime w) throws Exception {
        try {
            sopUploadPort.delete(w);
        } catch (Exception e) {
            throw new Exception("SOP刪除至MES失敗<br />" + e.getMessage());
        }

        try {
            responsorUploadPort.delete(w);
        } catch (Exception e) {
            throw new Exception("機種負責人刪除至MES失敗<br />" + e.getMessage());
        }

        try {
            flowUploadPort.delete(w);
        } catch (Exception e) {
            throw new Exception("徒程刪除至MES失敗<br />" + e.getMessage());
        }

    }
}
