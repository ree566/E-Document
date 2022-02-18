/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.MtdTestIntegrityUploadRoot;
import com.advantech.webservice.unmarshallclass.MtdTestIntegrity;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng 測試訊息資料維護
 */
@Component
public class MtdTestIntegrityUploadPort extends BasicUploadPort implements UploadPort {

    private static final Logger logger = LoggerFactory.getLogger(MtdTestIntegrityUploadPort.class);

    @Autowired
    private MtdTestIntegrityQueryPort mtdTestIntegrityQueryPort;

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(MtdTestIntegrityUploadRoot.class);
        } catch (JAXBException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void insert(Worktime w) throws Exception {
        generateRootAndUpload(w, UploadType.INSERT);
    }

    @Override
    public void update(Worktime w) throws Exception {
        generateRootAndUpload(w, UploadType.UPDATE);
    }

    @Override
    public void delete(Worktime w) throws Exception {
        generateRootAndUpload(w, UploadType.DELETE);
    }

    private void generateRootAndUpload(Worktime w, UploadType type) throws Exception {
        try {
            List<MtdTestIntegrity> l = mtdTestIntegrityQueryPort.query(w);
            MtdTestIntegrity t1TestIntegrity = l.stream().filter(t -> "T1".equals(t.getStationName())).findFirst().orElse(null);
            MtdTestIntegrity t2TestIntegrity = l.stream().filter(t -> "T2".equals(t.getStationName())).findFirst().orElse(null);

            checkAndUpload(t1TestIntegrity, w.getModelName(), "T1", w.getT1StatusQty(), w.getT1ItemsQty(), type);
            checkAndUpload(t2TestIntegrity, w.getModelName(), "T2", w.getT2StatusQty(), w.getT2ItemsQty(), type);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void checkAndUpload(MtdTestIntegrity mesData, String modelName, String stationName, Integer statusQty, Integer itemQty, UploadType type) throws Exception {
        //Don't upload anything when local data not present
        if (statusQty == null && itemQty == null) {
            return;
        }
        if (statusQty == 0 && itemQty == 0) {
            if (mesData == null) {
                return;
            }
            statusQty = mesData.getStateCnt();
            itemQty = mesData.getItemCnt();
            type = UploadType.DELETE;
        }

        MtdTestIntegrityUploadRoot root = new MtdTestIntegrityUploadRoot();
        MtdTestIntegrityUploadRoot.MTDTESTINTEGRITY t = root.getMTDTESTINTEGRITY();
        //Get T1 T2 state & items cnt from worktime field
        t.setDUTPARTNO(modelName);
        t.setSTATIONNAME(stationName);
        t.setTOTALSTATE(nullIntegerToString(statusQty));
        t.setTOTALTESTITEM(nullIntegerToString(itemQty));
        if (mesData == null && type == UploadType.UPDATE) {
            super.upload(root, UploadType.INSERT);
        } else if (mesData != null && type == UploadType.INSERT) {
            super.upload(root, UploadType.UPDATE);
        } else {
            super.upload(root, type);
        }
    }

    private String nullIntegerToString(Integer i) {
        i = i == null ? 0 : i;
        return i.toString();
    }
}
