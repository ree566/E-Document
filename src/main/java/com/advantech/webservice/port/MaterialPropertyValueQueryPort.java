/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.MaterialPropertyValueQueryRoot;
import com.advantech.webservice.unmarshallclass.MaterialPropertyValue;
import com.advantech.webservice.unmarshallclass.MaterialPropertyValues;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class MaterialPropertyValueQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(MaterialPropertyValueQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(MaterialPropertyValueQueryRoot.class, MaterialPropertyValues.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public List query(Worktime w) throws Exception {
        List<MaterialPropertyValue> l = super.query(w);
        //因接口採模糊查詢，相似機種會被撈出
        l = l.stream()
                .filter(r -> r.getItemNo().equals(w.getModelName()))
                .collect(Collectors.toList());
        return l;
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        MaterialPropertyValueQueryRoot root = new MaterialPropertyValueQueryRoot();
        MaterialPropertyValueQueryRoot.MATPROPERTYVALUE prop = root.getMATPROPERTYVALUE();
        prop.setITEMNO(w.getModelName());
        xmlResults.put("materialPropertyValueQuery", super.generateXmlString(root));
        return xmlResults;
    }
}
