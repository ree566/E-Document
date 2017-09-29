/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.SopInfos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sopQuery.SopQueryRoot;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class SopQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(SopQueryPort.class);

    private String[] types = {};

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String... types) {
        this.types = types;
    }

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(SopQueryRoot.class, SopInfos.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public List query(Worktime w) throws Exception {
        return super.query(w); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        for (String type : types) {
            SopQueryRoot root = new SopQueryRoot();
            SopQueryRoot.SOPINFO sopInfo = root.getSOPINFO();
            sopInfo.setITEMNO(w.getModelName());
            sopInfo.setTYPENO(type);
            xmlResults.put(type, super.generateXmlString(root));
        }
        return xmlResults;
    }

}
