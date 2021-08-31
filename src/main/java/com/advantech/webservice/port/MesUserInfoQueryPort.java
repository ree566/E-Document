/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.UserService;
import com.advantech.webservice.root.MesUserInfoQueryRoot;
import com.advantech.webservice.unmarshallclass.MesUserInfo;
import com.advantech.webservice.unmarshallclass.MesUserInfos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class MesUserInfoQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(MesUserInfoQueryPort.class);

    @Autowired
    private UserService userService;

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(MesUserInfoQueryRoot.class, MesUserInfos.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public List query(Worktime w) throws Exception {
        return (List<MesUserInfo>) super.query(w); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        Map<String, String> m = this.getJobnumber(w);
        for (Map.Entry<String, String> entry : m.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            MesUserInfoQueryRoot root = new MesUserInfoQueryRoot();
            MesUserInfoQueryRoot.USERS users = root.getUSERS();
            users.setUSERNO(value);
            xmlResults.put(key, super.generateXmlString(root));
        }
        return xmlResults;
    }

    public Map<String, String> getJobnumber(Worktime w) {
        Map<String, String> m = new HashMap();
        User speOwner = w.getUserBySpeOwnerId();
        User eeOwner = w.getUserByEeOwnerId();
        User qcOwner = w.getUserByQcOwnerId();
        User mpmOwner = w.getUserByMpmOwnerId();

        if (eeOwner != null) {
            m.put("eeOwner", userService.findByPrimaryKey(eeOwner.getId()).getJobnumber());
        }
        
        if (mpmOwner != null) {
            m.put("mpmOwner", userService.findByPrimaryKey(mpmOwner.getId()).getJobnumber());
        }
        
        if (eeOwner != null) {
            m.put("eeOwner", userService.findByPrimaryKey(eeOwner.getId()).getJobnumber());
        }

        m.put("speOwner", userService.findByPrimaryKey(speOwner.getId()).getJobnumber());
        m.put("qcOwner", userService.findByPrimaryKey(qcOwner.getId()).getJobnumber());

        return m;
    }
}
