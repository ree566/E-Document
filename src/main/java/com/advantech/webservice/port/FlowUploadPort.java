/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Flow;
import com.advantech.model.PreAssy;
import com.advantech.model.Worktime;
import com.advantech.service.FlowService;
import com.advantech.service.PreAssyService;
import com.advantech.webservice.root.FlowUploadRoot;
import com.advantech.webservice.root.Section;
import com.advantech.webservice.unmarshallclass.FlowRule;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng 徒程上傳
 */
@Component
public class FlowUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(FlowUploadPort.class);

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private MaterialFlowQueryPort materialFlowQueryPort;

    @Autowired
    private FlowRuleQueryPort flowRuleQueryPort;

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(FlowUploadRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    public void insert(Worktime w) throws Exception {
        Map<String, String> errorFields = new HashMap();
        List<MaterialFlow> l = materialFlowQueryPort.query(w);
        if (!l.isEmpty()) {
            this.delete(w);
        }
        for (Section section : Section.values()) {
            try {
                FlowRule rule = null;
                switch (section) {
                    case PREASSY:
                        PreAssy preAssy = w.getPreAssy();
                        if (preAssy != null) {
                            rule = this.getFlowRule(section, preAssyService.findByPrimaryKey(preAssy.getId()).getName());
                        }
                        break;
                    case BAB:
                        Flow babFlow = w.getFlowByBabFlowId();
                        if (babFlow != null) {
                            rule = this.getFlowRule(section, flowService.findByPrimaryKey(babFlow.getId()).getName());
                        }
                        break;
                    case TEST:
                        Flow testFlow = w.getFlowByTestFlowId();
                        if (testFlow != null) {
                            rule = this.getFlowRule(section, flowService.findByPrimaryKey(testFlow.getId()).getName());
                        }
                        break;
                    case PACKAGE:
                        Flow packingFlow = w.getFlowByPackingFlowId();
                        if (packingFlow != null) {
                            rule = this.getFlowRule(section, flowService.findByPrimaryKey(packingFlow.getId()).getName());
                        }
                        break;
                    default:
                        break;
                }
                if (rule != null) {
                    FlowUploadRoot root = new FlowUploadRoot();
                    FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
                    flow.setITEMNO(w.getModelName());
                    flow.setFLOWRULEID(rule.getId());
                    flow.setUNITNO(section.getCode());
                    this.insert(root);
                }
            } catch (Exception e) {
                errorFields.put(section + "_Flow", e.getMessage());
            }
        }
        if (!errorFields.isEmpty()) {
            throw new Exception(w.getModelName() + " 徒程新增至MES失敗: " + errorFields.toString());
        }
    }

    public void update(Worktime w) throws Exception {
        List<MaterialFlow> l = materialFlowQueryPort.query(w);
        Map<String, String> errorFields = new HashMap();
        for (Section section : Section.values()) {
            try {
                MaterialFlow mf = l.stream().filter(materialFlow -> materialFlow.getUnitNo().equals(section.getCode())).findFirst().orElse(null);
                String flowName = null;
                switch (section) {
                    case PREASSY:
                        PreAssy preAssy = w.getPreAssy();
                        flowName = (preAssy != null ? preAssyService.findByPrimaryKey(preAssy.getId()).getName() : null);
                        break;
                    case BAB:
                        Flow babFlow = w.getFlowByBabFlowId();
                        flowName = (babFlow != null ? flowService.findByPrimaryKey(babFlow.getId()).getName() : null);
                        if ("empty".equals(flowName)) {
                            flowName = null;
                        }
                        break;
                    case TEST:
                        Flow testFlow = w.getFlowByTestFlowId();
                        flowName = (testFlow != null ? flowService.findByPrimaryKey(testFlow.getId()).getName() : null);
                        break;
                    case PACKAGE:
                        Flow packingFlow = w.getFlowByPackingFlowId();
                        flowName = (packingFlow != null ? flowService.findByPrimaryKey(packingFlow.getId()).getName() : null);
                        break;
                    default:
                        break;
                }
                this.update(w.getModelName(), mf, flowName, section);
            } catch (Exception e) {
                errorFields.put(section + "_Flow", e.getMessage());
            }
        }
        if (!errorFields.isEmpty()) {
            throw new Exception(w.getModelName() + " 徒程更新至MES失敗: " + errorFields.toString());
        }
    }

    public void update(String modelName, MaterialFlow mf, String flowName, Section section) throws Exception {
        if (mf == null && flowName != null) {
            //insert
            FlowRule rule = this.getFlowRule(section, flowName);
            FlowUploadRoot root = new FlowUploadRoot();
            FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
            flow.setITEMNO(modelName);
            flow.setFLOWRULEID(rule.getId());
            flow.setUNITNO(section.getCode());
            this.insert(root);
        } else if (mf != null && flowName != null) {
            if (!Objects.equals(flowName, mf.getFlowRuleName())) {
                //update
                FlowRule rule = this.getFlowRule(section, flowName);
                FlowUploadRoot root = new FlowUploadRoot();
                FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
                flow.setMFID(mf.getId());
                flow.setITEMNO(modelName);
                flow.setFLOWRULEID(rule.getId());
                flow.setITEMID(mf.getItemId());
                flow.setUNITNO(mf.getUnitNo());
                this.update(root);
            }
        } else if (mf != null && flowName == null) {
            //delete
            FlowUploadRoot root = new FlowUploadRoot();
            FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
            flow.setMFID(mf.getId());
            flow.setITEMNO(modelName);
            flow.setFLOWRULEID(mf.getFlowRuleId());
            flow.setITEMID(mf.getItemId());
            flow.setUNITNO(mf.getUnitNo());
            this.delete(root);
        }
    }

    private FlowRule getFlowRule(Section section, String flowName) throws Exception {
        FlowRule rule = flowRuleQueryPort.query(section.getCode(), flowName);
        if (rule == null) {
            throw new Exception("Can't found flow name: " + flowName + " on MES");
        }
        return rule;
    }

    public void delete(Worktime w) throws Exception {
        Map<String, String> errorFields = new HashMap();

        List<MaterialFlow> l = materialFlowQueryPort.query(w);
        l.forEach((mf) -> {
            FlowUploadRoot root = new FlowUploadRoot();
            FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
            flow.setMFID(mf.getId());
            flow.setITEMNO(w.getModelName());
            flow.setFLOWRULEID(mf.getFlowRuleId());
            flow.setITEMID(mf.getItemId());
            flow.setUNITNO(mf.getUnitNo());
            try {
                this.delete(root);
            } catch (Exception e) {
                errorFields.put(Section.getByCode(mf.getUnitNo()) + "_Flow", e.getMessage());
            }
        });

        if (!errorFields.isEmpty()) {
            throw new Exception(w.getModelName() + " 徒程刪除至MES失敗: " + errorFields.toString());
        }
    }

    private void insert(FlowUploadRoot root) throws Exception {
        super.upload(root, UploadType.INSERT);
    }

    private void update(FlowUploadRoot root) throws Exception {
        super.upload(root, UploadType.UPDATE);
    }

    private void delete(FlowUploadRoot root) throws Exception {
        super.upload(root, UploadType.DELETE);
    }

    @Override
    public void upload(Worktime w) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
