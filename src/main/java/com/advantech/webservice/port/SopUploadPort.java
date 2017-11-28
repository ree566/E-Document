/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.advantech.webservice.root.SopBatchInsertRoot;
import com.advantech.webservice.unmarshallclass.SopInfo;
import com.advantech.webservice.root.SopRoot;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import static com.google.common.collect.Sets.newHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng SOP資料設定 因insert & delete所用的xml不同，固用subclass處理
 */
@Component
public class SopUploadPort implements UploadPort {

    private static final Logger logger = LoggerFactory.getLogger(SopUploadPort.class);

    private final String regex = "[(\\r\\n|\\n),\" ]+";

    private final String[] assyType = {"ASSY", "PKG"};
    private final String[] testType = {"T1", "T2"};

    @Autowired
    private SopQueryPort sopQueryPort;

    @Autowired
    private SopUploadPort.InsertPort insertPort;

    @Autowired
    private SopUploadPort.DeletePort deletePort;

    @Override
    public void insert(Worktime w) throws Exception {
        insertPort.upload(w);
    }

    @Override
    public void update(Worktime w) throws Exception {
        /*
                請先刪除後新增
                因MES空白SOP卡在製程段時，該製成只能維持單一SOP
                故先刪除時，可先將空白SOP刪除
                先新增的話，就會因上述原因產生exception
         */
        deletePort.upload(w);
        insertPort.upload(w);
    }

    @Override
    public void delete(Worktime w) throws Exception {
        w.setAssyPackingSop("");
        w.setTestSop("");
        this.deletePort.upload(w);
    }

    private Map<String, String> serializeSops(Worktime w) {
        Map sopMap = new HashMap();

        String assyPkgSop = w.getAssyPackingSop();
        String testSop = w.getTestSop();

        for (String st : assyType) {
            sopMap.put(st, assyPkgSop);
        }

        for (String st : testType) {
            sopMap.put(st, testSop);
        }

        return sopMap;

    }

    private Set<String> toSops(List<SopInfo> l) {
        Set s = new HashSet();
        l.forEach((info) -> {
            s.add(info.getSopName());
        });
        return s;
    }

    private Set<String> toSops(String st) {
        Set s = new HashSet();
        if (st != null && !"".equals(st)) {
            String[] sops = st.split(regex);
            s = new HashSet(Arrays.asList(sops));
        }
        return s;
    }

    private Set<String> findDifference(Set<String> s1, Set<String> s2) {
        return newHashSet(Collections2.filter(s1, Predicates.not(Predicates.in(s2))));
    }

    @Component
    public static class InsertPort extends BasicUploadPort {

        @Autowired
        private SopUploadPort outer;

        @Override
        protected void initJaxbMarshaller() {
            try {
                super.initJaxbMarshaller(SopBatchInsertRoot.class);
            } catch (JAXBException e) {
                logger.error(e.toString());
            }
        }

        public void upload(Worktime w) throws Exception {
            Map<String, String> serializeSops = outer.serializeSops(w);

            for (Map.Entry<String, String> entry : serializeSops.entrySet()) {
                String type = entry.getKey();
                String sopInLocal = entry.getValue();

                Set<String> sops = outer.toSops(sopInLocal);
                //當本地sop為空，一定沒有新增的資料(可省去多query webservice & 比對差異一次)
                if (sops.isEmpty()) {
                    continue;
                }

                outer.sopQueryPort.setTypes(type);
                Set mesSops = outer.toSops(outer.sopQueryPort.query(w));
                Set<String> insertedSops = outer.findDifference(sops, mesSops);

                generateRootAndUpload(w, type, insertedSops);
            }
        }

        private void generateRootAndUpload(Worktime w, String lineType, Set<String> insertedSops) throws Exception {
            if (!insertedSops.isEmpty()) {
                SopBatchInsertRoot root = new SopBatchInsertRoot();
                SopBatchInsertRoot.SOPINFO sopInfo = root.getSOPINFO();
                sopInfo.setTYPENO(lineType);
                sopInfo.setITEMNO(w.getModelName());
                SopBatchInsertRoot.SOPINFO.PARTNO partNo = new SopBatchInsertRoot.SOPINFO.PARTNO();
                List<SopBatchInsertRoot.SOPINFO.PARTNO.KEYNO> l = new ArrayList();

                for (String sop : insertedSops) {
                    SopBatchInsertRoot.SOPINFO.PARTNO.KEYNO keyNo = new SopBatchInsertRoot.SOPINFO.PARTNO.KEYNO();
                    keyNo.setITEMNO(w.getModelName());
                    keyNo.setTYPENO(lineType);
                    keyNo.setSOPNAME(sop);
                    l.add(keyNo);
                }
                partNo.setKEYNO(l);
                sopInfo.setPARTNO(partNo);
                super.upload(root, UploadType.INSERT);
            }
        }
    }

    @Component
    public static class DeletePort extends BasicUploadPort {

        @Autowired
        private SopUploadPort outer;

        @Override
        protected void initJaxbMarshaller() {
            try {
                super.initJaxbMarshaller(SopRoot.class);
            } catch (JAXBException e) {
                logger.error(e.toString());
            }
        }

        /**
         *
         * @param w
         * @throws Exception 考慮減少與接口溝通的次數未撰寫刪除all功能
         * 故刪除前須將Worktime的sop字串設為""，將空值upload至MES中即等於刪除
         */
        public void upload(Worktime w) throws Exception {
            Map<String, String> serializeSops = outer.serializeSops(w);
            for (Map.Entry<String, String> entry : serializeSops.entrySet()) {
                String type = entry.getKey();
                String sopInLocal = entry.getValue();
                Set<String> sops = outer.toSops(sopInLocal);
                outer.sopQueryPort.setTypes(type);
                Set mesSops = outer.toSops(outer.sopQueryPort.query(w));
                Set<String> deletedSops = outer.findDifference(mesSops, sops);
                generateRootAndUpload(w, type, deletedSops);
            }
        }

        private void generateRootAndUpload(Worktime w, String lineType, Set<String> deletedSops) throws Exception {
            for (String sop : deletedSops) {
                SopRoot root = new SopRoot();
                SopRoot.SOPINFO info = root.getSOPINFO();
                info.setTYPENO(lineType); //站別
                info.setITEMNO(w.getModelName()); //機種
                info.setSOPNAME(sop); //SOP文號
                info.setTYPENOOLD(lineType); //上一次站別
                info.setITEMNOOLD(w.getModelName()); //上一次機種
                info.setSOPNAMEOLD(sop); //上一次SOP文號
                super.upload(root, UploadType.DELETE);
            }
        }

    }

}
