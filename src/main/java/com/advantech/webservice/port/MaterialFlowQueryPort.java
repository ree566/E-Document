/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.MaterialFlowQueryRoot;
import com.advantech.webservice.root.Section;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import com.advantech.webservice.unmarshallclass.MaterialFlows;
import java.util.HashMap;
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
public class MaterialFlowQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(MaterialFlowQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(MaterialFlowQueryRoot.class, MaterialFlows.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public List query(Worktime w) throws Exception {
        return (List<MaterialFlow>) super.query(w);
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();

        for (Section section : Section.values()) {
            MaterialFlowQueryRoot root = new MaterialFlowQueryRoot(section.getCode(), w.getModelName());
            xmlResults.put("materialFlow" + section.toString(), super.generateXmlString(root));
        }

        return xmlResults;
    }

}
