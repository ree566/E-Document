/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.helper.HibernateObjectPrinter;
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
public class FlowUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(FlowUploadPort.class);

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private MaterialFlowQueryPort materialFlowQueryPort;

    @Autowired
    private FlowRuleQueryPort flowRuleQueryPort;

    @Autowired
    private FlowUploadPort.InsertPort insertPort;

    @Autowired
    private FlowUploadPort.UpdatePort updatePort;

    @Autowired
    private FlowUploadPort.DeletePort deletePort;

    public void insert(Worktime w) throws Exception {
        try {
            insertPort.upload(w);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public void update(Worktime w) throws Exception {
        try {
            updatePort.upload(w);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public void delete(Worktime w) throws Exception {
        try {
            deletePort.upload(w);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Component
    public static class InsertPort extends BasicUploadPort {

        @Autowired
        private FlowUploadPort outer;

        @Override
        protected void initJaxbMarshaller() {
            try {
                super.initJaxbMarshaller(FlowUploadRoot.class);
            } catch (JAXBException e) {
                logger.error(e.toString());
            }
        }

        @Override
        public void upload(Worktime w) throws Exception {
            super.upload(w, UploadType.INSERT);
        }

        @Override
        public Map<String, String> transformData(Worktime w) throws Exception {
            Map<String, String> xmlResults = new HashMap();
            for (Section section : Section.values()) {
                FlowRule rule = null;
                switch (section) {
                    case PREASSY:
                        if (w.getPreAssy() != null) {
                            PreAssy p = outer.preAssyService.findByPrimaryKey(w.getPreAssy().getId());
                            rule = outer.flowRuleQueryPort.query(section.getCode(), p.getName());
                        }
                        break;
                    case BAB:
                        if (w.getFlowByBabFlowId() != null) {
                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByBabFlowId().getId());
                            rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
                        }
                        break;
                    case TEST:
                        if (w.getFlowByTestFlowId() != null) {
                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByTestFlowId().getId());
                            rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
                        }
                        break;
                    case PACKAGE:
                        if (w.getFlowByPackingFlowId() != null) {
                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByPackingFlowId().getId());
                            rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
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
                    xmlResults.put(section + "_Flow", this.generateXmlString(root));
                }
            }
            HibernateObjectPrinter.print(w);
            HibernateObjectPrinter.print(xmlResults);
            return xmlResults;
        }
    }

    @Component
    public static class UpdatePort extends BasicUploadPort {

        @Autowired
        private FlowUploadPort outer;

        @Override
        protected void initJaxbMarshaller() {
            try {
                super.initJaxbMarshaller(FlowUploadRoot.class);
            } catch (JAXBException e) {
                logger.error(e.toString());
            }
        }

        @Override
        public void upload(Worktime w) throws Exception {
//            List<MaterialFlow> l = outer.materialFlowQueryPort.query(w);
//            
//            for (Section section : Section.values()) {
//                switch (section) {
//                    case PREASSY:
//                        if (w.getPreAssy() != null) {
//                            PreAssy p = outer.preAssyService.findByPrimaryKey(w.getPreAssy().getId());
//                            rule = outer.flowRuleQueryPort.query(section.getCode(), p.getName());
//                        }
//                        break;
//                    case BAB:
//                        if (w.getFlowByBabFlowId() != null) {
//                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByBabFlowId().getId());
//                            rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
//                        }
//                        break;
//                    case TEST:
//                        if (w.getFlowByTestFlowId() != null) {
//                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByTestFlowId().getId());
//                            rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
//                        }
//                        break;
//                    case PACKAGE:
//                        if (w.getFlowByPackingFlowId() != null) {
//                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByPackingFlowId().getId());
//                            rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//            super.upload(w, UploadType.UPDATE);
        }

        @Override
        public Map<String, String> transformData(Worktime w) throws Exception {
            List<MaterialFlow> l = outer.materialFlowQueryPort.query(w);
            Map<String, String> xmlResults = new HashMap();
            for (MaterialFlow mf : l) {
                final Section section = Section.getByCode(mf.getUnitNo());
                FlowRule rule = null;
                switch (section) {
                    case PREASSY:
                        if (w.getPreAssy() != null) {
                            PreAssy p = outer.preAssyService.findByPrimaryKey(w.getPreAssy().getId());
                            if (isFlowChanged(p.getName(), mf.getFlowRuleName())) {
                                rule = outer.flowRuleQueryPort.query(section.getCode(), p.getName());
                            }
                        }
                        break;
                    case BAB:
                        if (w.getFlowByBabFlowId() != null) {
                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByBabFlowId().getId());
                            if (isFlowChanged(f.getName(), mf.getFlowRuleName())) {
                                rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
                            }
                        }
                        break;
                    case TEST:
                        if (w.getFlowByTestFlowId() != null) {
                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByTestFlowId().getId());
                            if (isFlowChanged(f.getName(), mf.getFlowRuleName())) {
                                rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
                            }
                        }
                        break;
                    case PACKAGE:
                        if (w.getFlowByTestFlowId() != null) {
                            Flow f = outer.flowService.findByPrimaryKey(w.getFlowByPackingFlowId().getId());
                            if (isFlowChanged(f.getName(), mf.getFlowRuleName())) {
                                rule = outer.flowRuleQueryPort.query(section.getCode(), f.getName());
                            }
                        }
                        break;
                    default:
                        break;
                }
                if (rule != null) {
                    FlowUploadRoot root = new FlowUploadRoot();
                    FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
                    flow.setMFID(mf.getId());
                    flow.setITEMNO(w.getModelName());
                    flow.setFLOWRULEID(rule.getId());
                    flow.setITEMID(mf.getItemId());
                    flow.setUNITNO(section.getCode());
                    xmlResults.put(section + "_Flow", this.generateXmlString(root));
                }
            }
            HibernateObjectPrinter.print(xmlResults);
            return xmlResults;
        }

        private boolean isFlowChanged(String localFlowName, String remoteFlowName) {
            return !Objects.equals(localFlowName, remoteFlowName);
        }
    }

    @Component
    public static class DeletePort extends BasicUploadPort {

        @Autowired
        private FlowUploadPort outer;

        @Override
        protected void initJaxbMarshaller() {
            try {
                super.initJaxbMarshaller(FlowUploadRoot.class);
            } catch (JAXBException e) {
                logger.error(e.toString());
            }
        }

        @Override
        public void upload(Worktime w) throws Exception {
            super.upload(w, UploadType.DELETE);
        }

        @Override
        public Map<String, String> transformData(Worktime w) throws Exception {
            Map<String, String> xmlResults = new HashMap();
            List<MaterialFlow> l = outer.materialFlowQueryPort.query(w);
            for (MaterialFlow mf : l) {
                FlowUploadRoot root = new FlowUploadRoot();
                FlowUploadRoot.MATERIALFLOW flow = root.getMATERIALFLOW();
                flow.setMFID(mf.getId());
                flow.setITEMNO(w.getModelName());
                flow.setFLOWRULEID(mf.getFlowRuleId());
                flow.setITEMID(mf.getItemId());
                flow.setUNITNO(mf.getUnitNo());
                xmlResults.put(Section.getByCode(mf.getUnitNo()) + "_Flow", this.generateXmlString(root));
            }
            return xmlResults;
        }
    }

}
