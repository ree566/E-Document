/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.StandardWorkReasonQueryRoot;
import com.advantech.webservice.root.StandardWorktimeQueryRoot;
import com.advantech.webservice.unmarshallclass.StandardWorkTime;
import com.advantech.webservice.unmarshallclass.StandardWorkTimes;
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
public class StandardWorkTimeQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(StandardWorkTimeQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(StandardWorktimeQueryRoot.class, StandardWorkTimes.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    public List<StandardWorkTime> query(String modelName) throws Exception {
        return this.query(modelName, null);
    }

    public List<StandardWorkTime> query(String modelName, String unitNo) throws Exception {
        StandardWorktimeQueryRoot root = new StandardWorktimeQueryRoot();
        root.getSTANDARDWORKTIME().setITEMNO(modelName);
        root.getSTANDARDWORKTIME().setUNITNO(unitNo);
        return this.query(root);
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        throw new UnsupportedOperationException();
    }

}
