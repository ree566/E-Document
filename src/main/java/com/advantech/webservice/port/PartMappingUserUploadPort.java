/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.webservice.root.PartMappingUserRoot;
import com.advantech.model.Worktime;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng 料號負責人關係設定
 */
@Component
public class PartMappingUserUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(PartMappingUserUploadPort.class);

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(PartMappingUserRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void upload(Worktime w) throws Exception {
//        super.upload(w); //To change body of generated methods, choose Tools | Templates.
        Map result = this.transformData(w);
        System.out.println(new Gson().toJson(result));
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        //SpeUser, EeUser, QcUser
        
        Map<String, String> xmlResults = new HashMap();
        PartMappingUserRoot root = new PartMappingUserRoot();
        root.setPARTNO("a"); //機種
        root.setUSERIDs("b"); //人員代碼
        root.setTYPE(""); //寫死
        xmlResults.put("flow", this.generateXmlString(root));
        return xmlResults;
    }

}
