/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.webservice.root.PartMappingUserRoot;
import com.advantech.model.Worktime;
import com.advantech.webservice.unmarshallclass.MesUserInfo;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng 料號負責人關係設定
 */
@Component
public class ModelResponsorUploadPort extends BasicUploadPort implements UploadPort {

    private static final Logger logger = LoggerFactory.getLogger(ModelResponsorUploadPort.class);

    @Autowired
    private MesUserInfoQueryPort mesUserInfoQueryPort;

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(PartMappingUserRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void insert(Worktime w) throws Exception {
        this.update(w);
    }

    /*
        抓到MES該機種所有負責人
        抓取本地該機種負責人
        比對看是否有更新，有則去MES抓人員ID作update
        假使是新增則呼叫新增UploadType
     */
    @Override
    public void update(Worktime w) throws Exception {
        try {
            String userIdsString = concatUserId(getWorktimeOwners(w));
            PartMappingUserRoot root = new PartMappingUserRoot();
            root.setPARTNO(w.getModelName()); //機種
            root.setUSERIDs(userIdsString); //人員代碼
            super.upload(root, UploadType.UPDATE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /*
        因接口提供批次更新，故此delete不用像Sop的port一樣判斷新增刪除的資料(判斷是為了減少與接口溝通的次數)
        直接丟空字串當刪除即可
     */
    @Override
    public void delete(Worktime w) throws Exception {
        try {
            PartMappingUserRoot root = new PartMappingUserRoot();
            root.setPARTNO(w.getModelName()); //機種
            root.setUSERIDs(""); //人員代碼
            super.upload(root, UploadType.UPDATE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private List<MesUserInfo> getWorktimeOwners(Worktime w) throws Exception {
        List<MesUserInfo> l = mesUserInfoQueryPort.query(w);
        return l;
    }

    private String concatUserId(List<MesUserInfo> l) {
        StringBuilder sb = new StringBuilder();
        l.forEach((info) -> {
            sb.append("/");
            sb.append(info.getId());
        });
        return sb.toString();
    }

}
