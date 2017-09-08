/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.User;
import com.advantech.webservice.root.PartMappingUserRoot;
import com.advantech.model.Worktime;
import java.util.HashMap;
import java.util.List;
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
        super.upload(w); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        //SpeUser, EeUser, QcUser
        Map<String, String> owners = this.getWorktimeOwners(w);
        Map<String, String> xmlResults = new HashMap();

        for (String key : owners.keySet()) {
            String jobnumber = owners.get(key);
            PartMappingUserRoot root = new PartMappingUserRoot();
            root.setPARTNO(w.getModelName()); //機種
            root.setUSERIDs(jobnumber); //人員代碼
            root.setTYPE(""); //寫死
            xmlResults.put(key, this.generateXmlString(root));
        }
        return xmlResults;
    }

    @SuppressWarnings("null")
    public Map<String, String> getWorktimeOwners(Worktime w) {
        User speOwner = w.getUserBySpeOwnerId();
        User eeOwner = w.getUserByEeOwnerId();
        User qcOwner = w.getUserByQcOwnerId();
        Map<String, String> m = new HashMap();
        if (speOwner != null) {
            m.put("speOwner", speOwner.getJobnumber());
        }
        if (eeOwner != null) {
            m.put("eeOwner", eeOwner.getJobnumber());
        }
        if (qcOwner != null) {
            m.put("qcOwner", qcOwner.getJobnumber());
        }
        return m;
    }

}
