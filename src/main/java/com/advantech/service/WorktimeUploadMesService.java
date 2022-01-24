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
import com.advantech.webservice.port.MtdTestIntegrityUploadPort;
import org.apache.commons.lang3.ObjectUtils;
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

    @Autowired
    private MtdTestIntegrityUploadPort testIntegrityUploadPort;

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

    @Value("${WORKTIME.UPLOAD.TESTINTEGRITY: true}")
    private boolean isUploadTestIntegrity;

    public void portParamInit() throws Exception {
        if (isUploadMatProp) {
            materialPropertyUploadPort.initSettings();
        }
    }

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
            if (isUploadTestIntegrity) {
                try {
                    testIntegrityUploadPort.insert(w);
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
            if (isUploadTestIntegrity && isUploadTestIntegrityChanged(rowLastStatus, w)) {
                try {
                    testIntegrityUploadPort.update(w);
                } catch (Exception e) {
                    throw new Exception("測試狀態更新至MES失敗<br />" + e.getMessage());
                }
            }
        }
    }

    private boolean isModelNameChanged(Worktime prev, Worktime current) {
        return !prev.getModelName().equals(current.getModelName());
    }

    private boolean isSopChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !isEquals(prev.getAssyPackingSop(), current.getAssyPackingSop())
                || !isEquals(prev.getTestSop(), current.getTestSop());
    }

    //Revision entity relation object are lasy loading.
    private boolean isModelResponsorChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !isEquals(prev.getUserBySpeOwnerId(), current.getUserBySpeOwnerId())
                || !isEquals(prev.getUserByEeOwnerId(), current.getUserByEeOwnerId())
                || !isEquals(prev.getUserByQcOwnerId(), current.getUserByQcOwnerId())
                || !isEquals(prev.getUserByMpmOwnerId(), current.getUserByMpmOwnerId());
    }

    //Revision entity relation object are lasy loading.
    private boolean isFlowChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !isEquals(prev.getPreAssy(), current.getPreAssy())
                || !isEquals(prev.getFlowByBabFlowId(), current.getFlowByBabFlowId())
                || !isEquals(prev.getFlowByTestFlowId(), current.getFlowByTestFlowId())
                || !isEquals(prev.getFlowByPackingFlowId(), current.getFlowByPackingFlowId());
    }

    private boolean isMatPropertyChanged(Worktime prev, Worktime current) {
        boolean b = isModelNameChanged(prev, current)
                || !isEquals(prev.getPending(), current.getPending())
                || !isEquals(prev.getPendingTime(), current.getPendingTime())
                || !isEquals(prev.getBurnIn(), current.getBurnIn())
                || !isEquals(prev.getBiTime(), current.getBiTime())
                || !isEquals(prev.getBiTemperature(), current.getBiTemperature())
                || !isEquals(prev.getKeypartA(), current.getKeypartA())
                || !isEquals(prev.getKeypartB(), current.getKeypartB())
                || !isEquals(prev.getMacTotalQty(), current.getMacTotalQty())
                || !isEquals(prev.getCe(), current.getCe())
                || !isEquals(prev.getCe(), current.getCe())
                || !isEquals(prev.getUl(), current.getUl())
                || !isEquals(prev.getRohs(), current.getRohs())
                || !isEquals(prev.getWeee(), current.getWeee())
                || !isEquals(prev.getMadeInTaiwan(), current.getMadeInTaiwan())
                || !isEquals(prev.getFcc(), current.getFcc())
                || !isEquals(prev.getEac(), current.getEac())
                || !isEquals(prev.getKc(), current.getKc())
                || !isEquals(prev.getNsInOneCollectionBox(), current.getNsInOneCollectionBox())
                || !isEquals(prev.getAcwVoltage(), current.getAcwVoltage())
                || !isEquals(prev.getIrVoltage(), current.getIrVoltage())
                || !isEquals(prev.getTestProfile(), current.getTestProfile())
                || !isEquals(prev.getLltValue(), current.getLltValue())
                || !isEquals(prev.getGndValue(), current.getGndValue())
                || !isEquals(prev.getWeight(), current.getWeight())
                || !isEquals(prev.getWeightAff(), current.getWeightAff())
                || !isEquals(prev.getTolerance(), current.getTolerance())
                || (current.getPartNoAttributeMaintain() != ' '
                && !isEquals(prev.getPartNoAttributeMaintain(), current.getPartNoAttributeMaintain()))
                || !isEquals(prev.getPartLink(), current.getPartLink())
                || !isEquals(prev.getBurnInQuantity(), current.getBurnInQuantity());

        return b;
    }

    private boolean isUploadTestIntegrityChanged(Worktime prev, Worktime current) {
        return isModelNameChanged(prev, current)
                || !isEquals(prev.getT1ItemsQty(), current.getT1ItemsQty())
                || !isEquals(prev.getT1StatusQty(), current.getT1StatusQty())
                || !isEquals(prev.getT2ItemsQty(), current.getT2ItemsQty())
                || !isEquals(prev.getT2StatusQty(), current.getT2StatusQty());
    }

    private <T extends Comparable> boolean isEquals(T o1, T o2) {
        return ObjectUtils.compare(o1, o2) == 0;
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
            
            if (isUploadTestIntegrity) {
                try {
                    testIntegrityUploadPort.delete(w);
                } catch (Exception e) {
                    throw new Exception("測試狀態刪除至MES失敗<br />" + e.getMessage());
                }
            }
        }
    }
}
