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
import com.advantech.webservice.root.FlowRuleQueryRoot;
import com.advantech.webservice.root.Section;
import com.advantech.webservice.unmarshallclass.FlowRule;
import com.advantech.webservice.unmarshallclass.FlowRules;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class FlowRuleQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(FlowRuleQueryPort.class);

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private FlowService flowService;

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(FlowRuleQueryRoot.class, FlowRules.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public List query(Worktime w) throws Exception {
        return (List<FlowRule>) super.query(w); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        Map<Section, String> m = this.getFlowSettings(w);
        for (Map.Entry<Section, String> entry : m.entrySet()) {
            Section key = entry.getKey();
            String value = entry.getValue();
            FlowRuleQueryRoot root = new FlowRuleQueryRoot(key.getCode(), value);
            xmlResults.put(key.toString(), super.generateXmlString(root));
        }
        return xmlResults;
    }

    private Map<Section, String> getFlowSettings(Worktime w) {
        Map<Section, String> m = new HashMap();
        PreAssy preAssy = w.getPreAssy();
        Flow babFlow = w.getFlowByBabFlowId();
        Flow testFlow = w.getFlowByTestFlowId();
        Flow packingFlow = w.getFlowByPackingFlowId();

        if (preAssy != null) {
            m.put(
                    Section.PREASSY,
                    preAssy.getName() != null
                    ? preAssy.getName()
                    : preAssyService.findByPrimaryKey(preAssy.getId()).getName()
            );
        }

        if (babFlow != null) {
            m.put(
                    Section.BAB,
                    babFlow.getName() != null
                    ? babFlow.getName()
                    : flowService.findByPrimaryKey(babFlow.getId()).getName());
        }
        if (testFlow != null) {
            m.put(
                    Section.TEST,
                    testFlow.getName() != null
                    ? testFlow.getName()
                    : flowService.findByPrimaryKey(testFlow.getId()).getName()
            );
        }
        if (packingFlow != null) {
            m.put(
                    Section.PACKAGE,
                    packingFlow.getName() != null
                    ? packingFlow.getName()
                    : flowService.findByPrimaryKey(packingFlow.getId()).getName()
            );
        }

        return m;
    }

}
