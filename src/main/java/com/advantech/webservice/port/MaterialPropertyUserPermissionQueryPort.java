/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.webservice.root.MaterialPropertyUserPermissionQueryRoot;
import com.advantech.webservice.unmarshallclass.MaterialPropertyUserPermissions;
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
public class MaterialPropertyUserPermissionQueryPort extends BasicQueryPort {

    private static final Logger logger = LoggerFactory.getLogger(MaterialPropertyUserPermissionQueryPort.class);

    @Override
    protected void initJaxb() {
        try {
            super.initJaxb(MaterialPropertyUserPermissionQueryRoot.class, MaterialPropertyUserPermissions.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    public List query(String jobnumber) throws Exception {
        MaterialPropertyUserPermissionQueryRoot root = new MaterialPropertyUserPermissionQueryRoot();
        MaterialPropertyUserPermissionQueryRoot.MATPROPERTYUSER prop = root.getMATPROPERTYUSER();
        prop.setUSERNO(jobnumber);
        HibernateObjectPrinter.print(this.generateXmlString(root));
        return this.query(root);
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        throw new UnsupportedOperationException();
    }

}
