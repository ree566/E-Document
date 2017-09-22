/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.SopRoot;
import com.google.common.collect.ImmutableMap;
import static com.google.common.collect.Maps.newHashMap;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
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

    private Map<String, String> type;

    private final String assyPkg = "assyPkg";
    private final String test = "test";

    @Override
    protected void initJaxbMarshaller() {
        type = newHashMap(ImmutableMap.<String, String>builder()
                .put("ASSY", assyPkg)
                //                .put("PKG", assyPkg)
                .put("T1", test)
                //                .put("T2", test)
                .build());
        try {
            super.initJaxbMarshaller(SopRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void upload(Worktime w) throws Exception {
        try (FileWriter writer = new FileWriter("C:\\Users\\Wei.Cheng\\Desktop\\MyFile.txt", true)) {
            try {
                super.upload(w, UploadType.UPDATE); //To change body of generated methods, choose Tools | Templates.
            } catch (Exception e) {
                writer.write(e.getMessage());
                writer.write("\r\n");
            }
        }
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        //ASSY, BI, PASSY, PKG, RI, T1, T2

        Map<String, String> xmlResults = new HashMap();
        for (Map.Entry<String, String> entry : type.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                xmlResults.put(key, setToXml(key, value, w));
            } catch (Exception e) {
                logger.error(e.toString());
                throw new Exception("Error on create xml at column name " + key);
            }
        }
        return xmlResults;
    }

    private String setToXml(String typeNo, String type, Worktime w) throws JAXBException {
        String sop = assyPkg.equals(type) ? replaceNull(w.getAssyPackingSop()) : replaceNull(w.getTestSop());

        SopRoot root = new SopRoot();
        SopRoot.SOPINFO info = root.getSOPINFO();
        info.setTYPENO(typeNo); //站別
        info.setITEMNO(w.getModelName()); //機種
        info.setSOPNAME(sop); //SOP文號
        info.setTYPENOOLD(typeNo); //上一次站別
        info.setITEMNOOLD(w.getModelName()); //上一次機種
        info.setSOPNAMEOLD(sop); //上一次SOP文號

        return this.generateXmlString(root);
    }

    private String replaceNull(String st) {
        return st == null ? "" : st;
    }

}
