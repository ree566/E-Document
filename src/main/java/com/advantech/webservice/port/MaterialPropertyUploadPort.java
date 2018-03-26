/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.helper.SpringExpressionUtils;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import com.advantech.service.PendingService;
import com.advantech.service.WorktimeMaterialPropertyUploadSettingService;
import com.advantech.webservice.root.MaterialPropertyBatchUploadRoot;
import com.advantech.webservice.unmarshallclass.MaterialProperty;
import com.advantech.webservice.unmarshallclass.MaterialPropertyUserPermission;
import com.advantech.webservice.unmarshallclass.MaterialPropertyValue;
import static com.google.common.base.Preconditions.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class MaterialPropertyUploadPort extends BasicUploadPort implements UploadPort {
    
    private static final Logger logger = LoggerFactory.getLogger(MaterialPropertyUploadPort.class);
    
    @Autowired
    private MaterialPropertyQueryPort materialPropertyQueryPort;
    
    @Autowired
    private MaterialPropertyValueQueryPort materialPropertyValueQueryPort;
    
    @Autowired
    private MaterialPropertyUserPermissionQueryPort permissionQueryPort;
    
    @Autowired
    private WorktimeMaterialPropertyUploadSettingService propSettingService;
    
    @Autowired
    private PendingService pendingService;
    
    @Autowired
    private SpringExpressionUtils expressionUtils;
    
    private List<MaterialProperty> temp_MaterialPropertys = new ArrayList();
    private List<MaterialPropertyUserPermission> temp_MaterialPropertyUserPermissions = new ArrayList();
    private List<WorktimeMaterialPropertyUploadSetting> settings = new ArrayList();
    
    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(MaterialPropertyBatchUploadRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }
    
    public void initSetting() throws Exception {
        temp_MaterialPropertys = materialPropertyQueryPort.query("");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            temp_MaterialPropertyUserPermissions = permissionQueryPort.query("A-7568");
        } else {
            User user = (User) auth.getPrincipal();
            temp_MaterialPropertyUserPermissions = permissionQueryPort.query(user.getJobnumber());
        }
        settings = propSettingService.findAll();
    }
    
    @Override
    public void insert(Worktime w) throws Exception {
        //Query to prevent override the exists setting on MES.
        List<MaterialPropertyValue> remotePropSettings = materialPropertyValueQueryPort.query(w);
        MaterialPropertyBatchUploadRoot root = this.checkDifferenceAndGenerateRoot(remotePropSettings, w);
        super.upload(root, UploadType.UPDATE);
    }
    
    @Override
    public void update(Worktime w) throws Exception {
        List<MaterialPropertyValue> remotePropSettings = materialPropertyValueQueryPort.query(w);
        MaterialPropertyBatchUploadRoot root = this.checkDifferenceAndGenerateRoot(remotePropSettings, w);
        super.upload(root, UploadType.UPDATE);
    }
    
    @Override
    public void delete(Worktime w) throws Exception {
        //因為要刪除全部設定，固直接將遠端setting丟給checkMatPermission檢查即可
        List<MaterialPropertyValue> remotePropSettings = materialPropertyValueQueryPort.query(w);
        this.checkMatPermission(remotePropSettings);

        //傳入空的陣列即可批次將底下的屬性值刪除
        remotePropSettings = new ArrayList();
        MaterialPropertyBatchUploadRoot root = toJaxbElement(remotePropSettings);
        root.getMATVALUE().setITEMNO(w.getModelName());
        super.upload(root, UploadType.UPDATE);
    }

    //找出Remote & local difference, 差異如果屬性值在local setting則update, else keep and don't change its value
    //已存在遠端的setting只許更新其值
    public MaterialPropertyBatchUploadRoot checkDifferenceAndGenerateRoot(List<MaterialPropertyValue> remotePropSettings, Worktime w) throws Exception {
        checkArgument(!settings.isEmpty(), "Can't find any upload setting in WorktimeMaterialPropertyUploadSetting table");
        
        w.setPending(pendingService.findByPrimaryKey(w.getPending().getId()));
        
        Set<String> localMatPropNo = settings.stream()
                .map(WorktimeMaterialPropertyUploadSetting::getMatPropNo)
                .collect(Collectors.toSet());

        //Find setting not in local
        List<MaterialPropertyValue> propNotSettingInLocal = remotePropSettings.stream()
                .filter(r -> Objects.equals(w.getModelName(), r.getItemNo()) && !localMatPropNo.contains(r.getMatPropertyNo()))
                .collect(Collectors.toList());
        
        List<MaterialPropertyValue> propSettingInLocal = new ArrayList();
        List<MaterialPropertyValue> updatedMatProps = new ArrayList();
        
        settings.forEach((setting) -> {
            boolean isUpdated = false;
            //先計算，等於初始值者跳過
            //省去找MaterialPropertyValue的時間
            String mainValue = getValueFromFormula(w, setting.getFormula());
            String secondValue = getValueFromFormula(w, setting.getAffFormula());
            
            if (!Objects.equals(mainValue, setting.getDefaultValue()) || (Objects.equals(mainValue, setting.getDefaultValue()) && setting.getUploadWhenDefault() == 1)) {
                
                MaterialPropertyValue mp = remotePropSettings.stream()
                        .filter(r -> Objects.equals(w.getModelName(), r.getItemNo()) && Objects.equals(setting.getMatPropNo(), r.getMatPropertyNo()))
                        .findFirst().orElse(null);

                //屬性值不在MES
                if (mp == null) {
                    mp = new MaterialPropertyValue();
                    mp.setMatPropertyNo(setting.getMatPropNo());
                    MaterialProperty prop = findMatPropInfo(setting.getMatPropNo());
                    mp.setAffPropertyType(prop.getAffPropertyType());
                    isUpdated = true;
                } else if (ObjectUtils.compare(mp.getValue(), mainValue) != 0 || ObjectUtils.compare(mp.getAffPropertyValue(), secondValue) != 0) {
                    isUpdated = true;
                }

                //不等於null只蓋掉其value，剩下保留
                mp.setValue(mainValue);
                mp.setAffPropertyValue(secondValue);
                propSettingInLocal.add(mp);
                
                if (isUpdated) {
                    updatedMatProps.add(mp);
                }
            }
        });
        this.checkMatPermission(updatedMatProps);
        List<MaterialPropertyValue> allProp = Stream.concat(propSettingInLocal.stream(), propNotSettingInLocal.stream())
                .collect(Collectors.toList());
        MaterialPropertyBatchUploadRoot root = this.toJaxbElement(allProp);
        root.getMATVALUE().setITEMNO(w.getModelName());
        return root;
    }
    
    private MaterialPropertyBatchUploadRoot toJaxbElement(List<MaterialPropertyValue> l) {
        MaterialPropertyBatchUploadRoot root = new MaterialPropertyBatchUploadRoot();
        MaterialPropertyBatchUploadRoot.MATVALUE matvalue = root.getMATVALUE();
        List<MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL> details = new ArrayList();
        for (MaterialPropertyValue prop : l) {
            MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL detail = new MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL();
            detail.setMATPROPERTYNO(prop.getMatPropertyNo());
            detail.setVALUE(prop.getValue());
            detail.setAFFPROTYPE(prop.getAffPropertyType());
            detail.setAFFVALUE(prop.getAffPropertyValue());
            detail.setMEMO(prop.getMemo());
            details.add(detail);
        }
        matvalue.setMATVALUEDETAIL(details);
        return root;
    }
    
    private String getValueFromFormula(Worktime w, String formula) {
        Object o = expressionUtils.getValueFromFormula(w, formula);
        
        if (o != null) {
            if (o instanceof BigDecimal && ((BigDecimal) o).signum() == 0) {
                return "0";
            } else if ((o instanceof Character || o instanceof String) && "".equals(o.toString().trim())) {
                return "";
            } else {
                return o.toString();
            }
        }
        return null;
    }
    
    private void checkMatPermission(List<MaterialPropertyValue> changedMatProp) {
        List<String> noPermissionMatProp = new ArrayList();
        changedMatProp.forEach(prop -> {
            boolean hasMatPermission = temp_MaterialPropertyUserPermissions.stream()
                    .anyMatch(u -> ObjectUtils.compare(u.getMaterialPropertyNo(), prop.getMatPropertyNo()) == 0);
            if (!hasMatPermission) {
                MaterialProperty propInfo = findMatPropInfo(prop.getMatPropertyNo());
                noPermissionMatProp.add(propInfo.getMatPropertyName());
            }
        });
        checkState(noPermissionMatProp.isEmpty(), "您在MES中沒有權限更新下列屬性值欄位: " + noPermissionMatProp.toString());
    }
    
    private MaterialProperty findMatPropInfo(String matPropNo) {
        MaterialProperty propInfo = temp_MaterialPropertys.stream()
                .filter(tM -> ObjectUtils.compare(tM.getMatPropertyNo(), matPropNo) == 0)
                .findFirst()
                .orElse(null);
        checkArgument(propInfo != null, "Can't find materialProperty: " + matPropNo);
        return propInfo;
    }
}
