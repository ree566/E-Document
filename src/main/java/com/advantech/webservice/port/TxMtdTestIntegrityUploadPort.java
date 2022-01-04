/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.TxMtdTestIntegrityUploadRoot;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng 測試訊息資料維護
 */
@Component
public class TxMtdTestIntegrityUploadPort extends BasicUploadPort implements UploadPort {

    private static final Logger logger = LoggerFactory.getLogger(TxMtdTestIntegrityUploadPort.class);

    @Override
    protected void initJaxbMarshaller() {
        try {
            super.initJaxbMarshaller(TxMtdTestIntegrityUploadRoot.class);
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
            TxMtdTestIntegrityUploadRoot root = new TxMtdTestIntegrityUploadRoot();

            TxMtdTestIntegrityUploadRoot.MTDTESTINTEGRITY t = root.getMTDTESTINTEGRITY();
            //Get T1 T2 state & items cnt from worktime field
            t.setDUTPARTNO(w.getModelName());
            t.setSTATIONNAME("T1");
            t.setTOTALSTATE(nullIntegerToString(w.getT1StatusQty()));
            t.setTOTALTESTITEM(nullIntegerToString(w.getT1ItemsQty()));
            super.upload(root, type);
            
            t.setSTATIONNAME("T2");
            t.setTOTALSTATE(nullIntegerToString(w.getT2StatusQty()));
            t.setTOTALTESTITEM(nullIntegerToString(w.getT2ItemsQty()));
            super.upload(root, type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private String nullIntegerToString(Integer i) {
        i = i == null ? 0 : i;
        return i.toString();
    }
}
