/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.FlowRuleQueryRoot;
import com.advantech.webservice.unmarshallclass.FlowRule;
import com.advantech.webservice.unmarshallclass.FlowRules;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class FlowRuleQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(FlowRuleQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(FlowRuleQueryRoot.class, FlowRules.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    public FlowRule query(String unitNo, String flowName) throws Exception {
        List<FlowRule> l = this.query(new FlowRuleQueryRoot(unitNo, flowName));
        return l.isEmpty() ? null : l.get(0);
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        throw new UnsupportedOperationException();
    }

}
