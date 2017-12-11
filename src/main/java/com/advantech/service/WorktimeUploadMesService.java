/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.AuditDAO;
import com.advantech.model.Worktime;
import com.advantech.webservice.port.FlowUploadPort;
import com.advantech.webservice.port.MaterialPropertyUploadPort;
import com.advantech.webservice.port.ModelResponsorUploadPort;
import com.advantech.webservice.port.SopUploadPort;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private MaterialPropertyUploadPort materialPropertyUploadPort;

    @Value("${WORKTIME.UPLOAD.INSERT: true}")
    private boolean isInserted;

    @Value("${WORKTIME.UPLOAD.UPDATE: true}")
    private boolean isUpdated;

    @Value("${WORKTIME.UPLOAD.DELETE: true}")
    private boolean isDeleted;

    @Value("${WORKTIME.UPLOAD.SOP: true}")
    private boolean isUploadSop;

    @Value("${WORKTIME.UPLOAD.RESPONSOR: true}")
    private boolean isUploadResponsor;

    @Value("${WORKTIME.UPLOAD.FLOW: true}")
    private boolean isUploadFlow;

    @Value("${WORKTIME.UPLOAD.MATPROPERTY: true}")
    private boolean isUploadMatProp;

    public void insert(Worktime w) throws Exception {
        if (isInserted) {
            if (isUploadSop) {
                try {
                    sopUploadPort.insert(w);
                } catch (Exception e) {
                    throw new Exception("SOP新增至MES失敗<br />" + e.getMessage());
                }
            }
            if (isUploadResponsor) {
                try {
                    responsorUploadPort.insert(w);
                } catch (Exception e) {
                    throw new Exception("機種負責人新增至MES失敗<br />" + e.getMessage());
                }
            }
            if (isUploadFlow) {
                try {
                    flowUploadPort.insert(w);
                } catch (Exception e) {
                    throw new Exception("徒程新增至MES失敗<br />" + e.getMessage());
                }
            }
            if (isUploadMatProp) {
                try {
                    materialPropertyUploadPort.insert(w);
                } catch (Exception e) {
                    throw new Exception("料號屬性值新增至MES失敗<br />" + e.getMessage());
                }
            }
        }
    }

    public void update(Worktime w) throws Exception {
        if (isUpdated) {
            Worktime rowLastStatus = (Worktime) auditDAO.findLastStatusBeforeUpdate(Worktime.class, w.getId());
            if (isUploadSop && isSopChanged(rowLastStatus, w)) {
                try {
                    sopUploadPort.update(w);
                } catch (Exception e) {
                    throw new Exception("SOP更新至MES失敗<br />" + e.getMessage());
                }
            }

            if (isUploadResponsor && isModelResponsorChanged(rowLastStatus, w)) {
                try {
                    responsorUploadPort.update(w);
                } catch (Exception e) {
                    throw new Exception("機種負責人更新至MES失敗<br />" + e.getMessage());
                }
            }

            if (isUploadFlow && isFlowChanged(rowLastStatus, w)) {
                try {
                    flowUploadPort.update(w);
                } catch (Exception e) {
                    throw new Exception("徒程更新至MES失敗<br />" + e.getMessage());
                }
            }

            if (isUploadMatProp && isMatPropertyChanged(rowLastStatus, w)) {
                try {
                    materialPropertyUploadPort.update(w);
                } catch (Exception e) {
                    throw new Exception("料號屬性值更新至MES失敗<br />" + e.getMessage());
                }
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

    private boolean isMatPropertyChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !Objects.equals(prev.getPending(), current.getPending())
                || !Objects.equals(prev.getPendingTime(), current.getPendingTime())
                || !Objects.equals(prev.getBurnIn(), current.getBurnIn())
                || !Objects.equals(prev.getBiTime(), current.getBiTime())
                || !Objects.equals(prev.getBiTemperature(), current.getBiTemperature())
                || !Objects.equals(prev.getKeypartA(), current.getKeypartA())
                || !Objects.equals(prev.getKeypartB(), current.getKeypartB())
                || !Objects.equals(prev.getCe(), current.getCe())
                || !Objects.equals(prev.getCe(), current.getCe())
                || !Objects.equals(prev.getUl(), current.getUl())
                || !Objects.equals(prev.getRohs(), current.getRohs())
                || !Objects.equals(prev.getWeee(), current.getWeee())
                || !Objects.equals(prev.getMadeInTaiwan(), current.getMadeInTaiwan())
                || !Objects.equals(prev.getFcc(), current.getFcc())
                || !Objects.equals(prev.getEac(), current.getEac())
                || !Objects.equals(prev.getNsInOneCollectionBox(), current.getNsInOneCollectionBox())
                || !Objects.equals(prev.getPartNoAttributeMaintain(), current.getPartNoAttributeMaintain());
    }

    public void delete(Worktime w) throws Exception {
        if (isDeleted) {
            if (isUploadSop) {
                try {
                    sopUploadPort.delete(w);
                } catch (Exception e) {
                    throw new Exception("SOP刪除至MES失敗<br />" + e.getMessage());
                }
            }

            if (isUploadResponsor) {
                try {
                    responsorUploadPort.delete(w);
                } catch (Exception e) {
                    throw new Exception("機種負責人刪除至MES失敗<br />" + e.getMessage());
                }
            }

            if (isUploadFlow) {
                try {
                    flowUploadPort.delete(w);
                } catch (Exception e) {
                    throw new Exception("徒程刪除至MES失敗<br />" + e.getMessage());
                }
            }

            if (isUploadMatProp) {
                try {
                    materialPropertyUploadPort.delete(w);
                } catch (Exception e) {
                    throw new Exception("料號屬性值刪除至MES失敗<br />" + e.getMessage());
                }
            }
        }
    }
}
