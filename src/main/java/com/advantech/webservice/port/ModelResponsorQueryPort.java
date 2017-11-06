/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.unmarshallclass.ModelResponsor;
import com.advantech.webservice.root.ModelResponsorQueryRoot;
import com.advantech.webservice.unmarshallclass.ModelResponsors;
import com.advantech.webservice.root.SopQueryRoot;
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
public class ModelResponsorQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(ModelResponsorQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(ModelResponsorQueryRoot.class, ModelResponsors.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public List query(Worktime w) throws Exception {
        return (List<ModelResponsor>) super.query(w); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();

        ModelResponsorQueryRoot root = new ModelResponsorQueryRoot();
        root.setPARTNO(w.getModelName());
        xmlResults.put("modelName", super.generateXmlString(root));

        return xmlResults;
    }

}
