/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.MaterialPropertyQueryRoot;
import com.advantech.webservice.unmarshallclass.MaterialPropertys;
import java.util.HashMap;
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
public class MaterialPropertyQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(MaterialPropertyQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(MaterialPropertyQueryRoot.class, MaterialPropertys.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        MaterialPropertyQueryRoot root = new MaterialPropertyQueryRoot();
        MaterialPropertyQueryRoot.MATPROPERTYVALUE prop = root.getMATPROPERTYVALUE();
        prop.setITEMNO(w.getModelName());
        xmlResults.put("materialPropertyQuery", super.generateXmlString(root));
        return xmlResults;
    }
}
