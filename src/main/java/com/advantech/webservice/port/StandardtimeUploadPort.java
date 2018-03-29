/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.helper.SpringExpressionUtils;
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
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng CRUD都用同個XML & 相同 UploadType "A"
 */
@Component
public class StandardtimeUploadPort extends BasicUploadPort implements UploadPort {

    private static final Logger logger = LoggerFactory.getLogger(StandardtimeUploadPort.class);
    private List<WorktimeAutouploadSetting> settings;

    @Autowired
    private SpringExpressionUtils expressionUtils;

    @Autowired
    private WorktimeAutouploadSettingService settingService;

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(StandardtimeRoot.class); //To change body of generated methods, choose Tools | Templates.
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
    public void insert(Worktime w) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Worktime w) throws Exception {
        Map<String, String> errorFields = new HashMap();
        for (WorktimeAutouploadSetting setting : settings) {
            try {
                this.generateRootAndUpload(setting, w);
            } catch (Exception e) {
                errorFields.put(setting.getColumnName(), e.getMessage());
            }
        }
        if (!errorFields.isEmpty()) {
            throw new Exception(errorFields.toString());
        }
    }

    @Override
    public void delete(Worktime w) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void generateRootAndUpload(WorktimeAutouploadSetting setting, Worktime w) throws Exception {
        String columnUnit = setting.getColumnUnit();
        BigDecimal totalCt = (BigDecimal) expressionUtils.getValueFromFormula(w, setting.getFormula());
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

//        if ("B".equals(columnUnit) && setting.getStationId() != null) {
//            swt.setMACHINECNT(w.getAssyStation());
//            swt.setOPCNT(w.getAssyStation());
//        } else if ("P".equals(columnUnit) && setting.getStationId() != null) {
//            swt.setMACHINECNT(w.getPackingStation());
//            swt.setOPCNT(2);
//        } else {
        swt.setMACHINECNT(0);
        swt.setOPCNT(1);
//        }
        super.upload(root, UploadType.INSERT);
    }

}
