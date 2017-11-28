/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.model.WorktimeAutouploadSetting;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.webservice.root.StandardtimeRoot;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class StandardtimeUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(StandardtimeUploadPort.class);
    private ExpressionParser parser;
    private List<WorktimeAutouploadSetting> settings;

    @Autowired
    private WorktimeAutouploadSettingService settingService;

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(StandardtimeRoot.class); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void upload(Worktime w) throws Exception {
        super.upload(w, UploadType.INSERT);
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();
        for (WorktimeAutouploadSetting setting : settings) {
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

}
