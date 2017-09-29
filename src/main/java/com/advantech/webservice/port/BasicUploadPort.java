/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.WsClient;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
public abstract class BasicUploadPort {

    private Marshaller jaxbMarshaller;

    @Autowired
    private WsClient client;

    @PostConstruct
    protected abstract void initJaxbMarshaller();

    protected void initJaxbMarshaller(Class persistClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(persistClass);
        jaxbMarshaller = jaxbContext.createMarshaller();
    }

    public abstract void upload(Worktime w) throws Exception;

    protected void upload(Worktime w, UploadType type) throws Exception {
        Map<String, String> xmlResults = transformData(w);
        Map<String, String> errorFields = new HashMap();
        for (Map.Entry<String, String> entry : xmlResults.entrySet()) {
            String field = entry.getKey();
            String xmlString = entry.getValue();
            TxResponse response = client.simpleTxSendAndReceive(xmlString, type);
            if (!"OK".equals(response.getTxResult())) {
                errorFields.put(field, response.getTxResult());
            }
        }
        if (!errorFields.isEmpty()) {
            throw new Exception(w.getModelName() + ": Error on saving xml result to MES on field " + errorFields.toString());
        }
    }

    /**
     *
     * @param w
     * @return Field name as key and xml generate result as value.
     * @throws java.lang.Exception
     */
    public abstract Map<String, String> transformData(Worktime w) throws Exception;

    protected String generateXmlString(Object jaxbElement) throws JAXBException {
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(jaxbElement, sw);
        String xmlString = sw.toString();
        return xmlString;
    }

}
