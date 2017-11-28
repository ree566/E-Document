/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.helper.SpringExpressionUtils;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import com.advantech.service.WorktimeMaterialPropertyUploadSettingService;
import com.advantech.webservice.root.MaterialPropertyUploadRoot;
import com.advantech.webservice.unmarshallclass.MaterialProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
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
public class MaterialPropertyUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(MaterialPropertyUploadPort.class);

    @Autowired
    private MaterialPropertyQueryPort materialPropertyQueryPort;

    @Autowired
    private MaterialPropertyUserPermissionQueryPort permissionQueryPort;

    @Autowired
    private WorktimeMaterialPropertyUploadSettingService propSettingService;

    @Autowired
    private SpringExpressionUtils expressionUtils;

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(MaterialPropertyUploadRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void upload(Worktime w) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        Map<String, String> xmlResults = new HashMap();

        List<MaterialProperty> remotePropSettings = materialPropertyQueryPort.query(w);

        //Foreach the database upload setting.
        List<WorktimeMaterialPropertyUploadSetting> settings = propSettingService.findAll();

        for (WorktimeMaterialPropertyUploadSetting setting : settings) {
            remotePropSettings = remotePropSettings.stream()
                    .map(o -> {
                        //因接口採模糊查詢，相似機種會被撈出造成資料混亂，故取得相同機種名稱屬性做update
                        if (!Objects.equals(o.getItemNo(), w.getModelName())) {
                            return null;
                        } else if (Objects.equals(o.getMatPropertyNo(), setting.getMatPropNo())) {
                            Object mainValue = expressionUtils.getValueFromFormula(w, setting.getFormula());
                            Object secondValue = setting.getAffFormula() == null ? null : expressionUtils.getValueFromFormula(w, setting.getAffFormula());

                            if (mainValue == null && setting.getUploadWhenDefault() == 1) {
                                if (mainValue instanceof String) {
                                    mainValue = "";
                                } else if (mainValue instanceof Integer || mainValue instanceof BigDecimal) {
                                    mainValue = "0";
                                }
                            }

                            //BigDecimal欄位
                            if (mainValue != null && !Objects.equals(mainValue, setting.getDefaultValue())) {
                                o.setValue(mainValue.toString());
                                o.setAffPropertyValue(secondValue == null ? "" : secondValue.toString());
                                return o;
                            } else {
                                return null;
                            }
                        } else {
                            //其他未在DB做設定的屬性值，一併將資訊附加其中(本端不更動其值)
                            return o;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList());
        }
        MaterialPropertyUploadRoot root = this.toJaxbElement(remotePropSettings);
        root.getMATVALUE().setITEMNO(w.getModelName());
        xmlResults.put("materialPropertyUpload", super.generateXmlString(root));
        return xmlResults;
    }

    private MaterialPropertyUploadRoot toJaxbElement(List<MaterialProperty> l) {
        MaterialPropertyUploadRoot root = new MaterialPropertyUploadRoot();
        MaterialPropertyUploadRoot.MATVALUE matvalue = root.getMATVALUE();
        List<MaterialPropertyUploadRoot.MATVALUE.MATVALUEDETAIL> details = new ArrayList();
        for (MaterialProperty prop : l) {
            MaterialPropertyUploadRoot.MATVALUE.MATVALUEDETAIL detail = new MaterialPropertyUploadRoot.MATVALUE.MATVALUEDETAIL();
            detail.setMATPROPERTYNO(prop.getMatPropertyNo());
            detail.setVALUE(prop.getValue());
            detail.setAFFPROTYPE(prop.getAffPropertyType());
            detail.setAFFVALUE(prop.getAffPropertyValue());
            detail.setMEMO("");
            details.add(detail);
        }
        matvalue.setMATVALUEDETAIL(details);
        return root;
    }

}
