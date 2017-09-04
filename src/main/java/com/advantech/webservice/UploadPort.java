/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.webservice.ie.StandardtimeRoot;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
public abstract class UploadPort {

    private Marshaller jaxbMarshaller;

    @Autowired
    private WsClient client;

    protected void initJaxbMarshaller(Class persistClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(persistClass);
        jaxbMarshaller = jaxbContext.createMarshaller();
    }

    protected void upload(Object bean, List settings) throws Exception {

        Map<String, String> xmlResults = this.transformData(bean, settings);
        List<String> errorFields = new ArrayList();
        for (Map.Entry<String, String> entry : xmlResults.entrySet()) {
            String field = entry.getKey();
            String xmlString = entry.getValue();
            TxResponse response = client.simpleTxSendAndReceive(xmlString);
            if (!"OK".equals(response.getTxResult())) {
                errorFields.add(field);
            }
        }
        if (!errorFields.isEmpty()) {
            throw new Exception("Error on saving xml result to MES on field " + errorFields.toString());
        }
    }

    protected abstract Map<String, String> transformData(Object bean, List settings);
    
    protected String generateXmlString(StandardtimeRoot root) throws JAXBException {
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(root, sw);
        String xmlString = sw.toString();
        return xmlString;
    }

}
