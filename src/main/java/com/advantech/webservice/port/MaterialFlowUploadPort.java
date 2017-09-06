/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.webservice.root.MaterialFlowRoot;
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
 * @author Wei.Cheng 徒程上傳
 */
@Component
public class MaterialFlowUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(MaterialFlowUploadPort.class);

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(MaterialFlowRoot.class);
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
        //PreAssy, Bab, Test, Pkg
 
        Map<String, String> xmlResults = new HashMap();
        MaterialFlowRoot root = new MaterialFlowRoot();
        MaterialFlowRoot.MATERIALFLOW flowInfo = root.getMATERIALFLOW();
        flowInfo.setMFID("a"); //機種流程代碼
        flowInfo.setITEMNO("b"); //機種
        flowInfo.setFLOWRULEID("c"); //流程代碼
        flowInfo.setFLOWSEQ(1); //寫死
        flowInfo.setITEMID("d"); //機種代碼
        flowInfo.setUNITNO("e"); //A, B, T, P
        flowInfo.setUPDATEFLOWFLAG("N"); //寫死
        xmlResults.put("flow", this.generateXmlString(root));
        return xmlResults;
    }

}
