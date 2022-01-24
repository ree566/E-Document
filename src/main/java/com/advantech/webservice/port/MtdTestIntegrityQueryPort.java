/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.MtdTestIntegrityQueryRoot;
import com.advantech.webservice.unmarshallclass.MtdTestIntegritys;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author MFG.ESOP
 */
@Component
public class MtdTestIntegrityQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(MtdTestIntegrityQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(MtdTestIntegrityQueryRoot.class, MtdTestIntegritys.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();

        MtdTestIntegrityQueryRoot root = new MtdTestIntegrityQueryRoot(w.getModelName(), null);
        
        xmlResults.put("modelName", super.generateXmlString(root));

        return xmlResults;
    }

}
