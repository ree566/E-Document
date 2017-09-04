/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.ie;

import com.advantech.model.Worktime;
import com.advantech.model.WorktimeAutouploadSetting;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.webservice.WsClient;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class WorktimeStandardtimeUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(WorktimeStandardtimeUploadPort.class);
    private Marshaller jaxbMarshaller;
    private ExpressionParser parser;
    private List<WorktimeAutouploadSetting> settings;

    @Autowired
    private WsClient client;

    @Autowired
    private WorktimeAutouploadSettingService settingService;

    @PostConstruct
    public void init() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(StandardtimeRoot.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            parser = new SpelExpressionParser();
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    //Don't forget init setting everytime.
    public void initSettings() {
        settings = settingService.findAll();
    }

    public void initSettings(List<WorktimeAutouploadSetting> settings) {
        this.settings = settings;
    }

    public void uploadStandardTime(Worktime w) throws Exception {
        if (settings == null) {
            throw new Exception("The upload settings is not initialized.");
        }

        Map<String, String> xmlResults = this.transformData(w, settings);
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
            throw new Exception(w.getModelName() + ": Error on saving xml result to MES on field " + errorFields.toString());
        }
    }

    public Map<String, String> transformData(Worktime w, List<WorktimeAutouploadSetting> l) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        for (WorktimeAutouploadSetting setting : l) {
            String columnUnit = setting.getColumnUnit();
            String columnName = setting.getColumnName();
            try {
                BigDecimal totalCt = getValueFromFormula(w, setting.getFormula());
                StandardtimeRoot root = new StandardtimeRoot();
                StandardtimeRoot.STANDARDWORKTIME swt = root.getSTANDARDWORKTIME();
                swt.setUNITNO(columnUnit);
                swt.setSTATIONID(setting.getStationId());
                swt.setLINEID(setting.getLineId());
                swt.setITEMNO(w.getModelName());
                swt.setTOTALCT(totalCt);
                swt.setFIRSTTIME(BigDecimal.ZERO);
                swt.setCT(setting.getCt());
                swt.setSIDE(5010);
                swt.setMIXCT(totalCt); //Temporarily set this column equal to totalCt

                if ("B".equals(columnUnit) && setting.getStationId() != null) {
                    swt.setMACHINECNT(w.getAssyStation());
                    swt.setOPCNT(w.getAssyStation());
                } else if ("P".equals(columnUnit) && setting.getStationId() != null) {
                    swt.setMACHINECNT(w.getPackingStation());
                    swt.setOPCNT(2);
                } else {
                    swt.setMACHINECNT(0);
                    swt.setOPCNT(1);
                }
                xmlResults.put(columnName, this.generateXmlString(root));
            } catch (Exception e) {
                logger.error(e.toString());
                throw new Exception("Error on create xml at column name " + columnName);
            }
        }
        return xmlResults;
    }

    private BigDecimal getValueFromFormula(Worktime w, String formula) {
        Expression exp = parser.parseExpression(formula);
        BigDecimal result = (BigDecimal) exp.getValue(w);
        return result;
    }

    public String generateXmlString(StandardtimeRoot root) throws JAXBException {
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(root, sw);
        String xmlString = sw.toString();
        return xmlString;
    }

}
