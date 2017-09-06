/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.SopRoot;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng SOP資料設定
 */
@Component
public class SopUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(SopUploadPort.class);

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(SopRoot.class);
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
        //ASSY, BI, PASSY, PKG, RI, T1, T2
        
        Map<String, String> xmlResults = new HashMap();
        SopRoot root = new SopRoot();
        SopRoot.SOPINFO info = root.getSOPINFO();
        info.setPARTNO("a"); //機種
        info.setTYPENO("b"); //站別
        info.setITEMNO("c"); //機種
        info.setSOPNAME("d"); //SOP文號
        info.setSTATIONNO(""); //寫死
        info.setSOPPAGENO(""); //寫死
        info.setTYPENOOLD("g"); //上一次站別
        info.setITEMNOOLD("h"); //上一次機種
        info.setSOPNAMEOLD("i"); //上一次SOP文號
        xmlResults.put("flow", this.generateXmlString(root));
        return xmlResults;
    }

}
