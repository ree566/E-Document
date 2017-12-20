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
 * @author Wei.Cheng 徒程上傳 CRUD xml相同
 */
@Component
public class FlowUploadPort extends BasicUploadPort implements UploadPort {

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

    @Override
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
                            preAssy = preAssyService.findByPrimaryKey(preAssy.getId());
                            rule = this.getFlowRule(section, preAssy.getName());
                        }
                        break;
                    case BAB:
                        Flow babFlow = w.getFlowByBabFlowId();
                        if (babFlow != null) {
                            babFlow = flowService.findByPrimaryKey(babFlow.getId());
                            if (!"empty".equals(babFlow.getName())) {
                                rule = this.getFlowRule(section, babFlow.getName());
                            }
                        }
                        break;
                    case TEST:
                        Flow testFlow = w.getFlowByTestFlowId();
                        if (testFlow != null) {
                            testFlow = flowService.findByPrimaryKey(testFlow.getId());
                            rule = this.getFlowRule(section, testFlow.getName());
                        }
                        break;
                    case PACKAGE:
                        Flow packingFlow = w.getFlowByPackingFlowId();
                        if (packingFlow != null) {
                            packingFlow = flowService.findByPrimaryKey(packingFlow.getId());
                            rule = this.getFlowRule(section, packingFlow.getName());
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
                    super.upload(root, UploadType.INSERT);
                }
            } catch (Exception e) {
                errorFields.put(section + "_Flow", e.getMessage());
            }
        }
        if (!errorFields.isEmpty()) {
            throw new Exception(w.getModelName() + " 徒程新增至MES失敗: " + errorFields.toString());
        }
    }

    @Override
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
            super.upload(root, UploadType.INSERT);
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
                super.upload(root, UploadType.UPDATE);
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
            super.upload(root, UploadType.DELETE);
        }
    }

    private FlowRule getFlowRule(Section section, String flowName) throws Exception {
        FlowRule rule = flowRuleQueryPort.query(section.getCode(), flowName);
        if (rule == null) {
            throw new Exception("Can't found flow name: " + flowName + " on MES");
        }
        return rule;
    }

    @Override
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
                super.upload(root, UploadType.DELETE);
            } catch (Exception e) {
                errorFields.put(Section.getByCode(mf.getUnitNo()) + "_Flow", e.getMessage());
            }
        });

        if (!errorFields.isEmpty()) {
            throw new Exception(w.getModelName() + " 徒程刪除至MES失敗: " + errorFields.toString());
        }
    }

}
