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
 * @author Wei.Cheng SOP資料設定
 */
@Component
public class SopUploadPort {

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

    public void update(Worktime w) throws Exception {
        try {
            /*
                請先刪除後新增
                因MES空白SOP卡在製程段時，該製成只能維持單一SOP
                故先刪除時，可先將空白SOP刪除
                先新增的話，就會因上述原因產生exception
             */
            deletePort.upload(w);
            insertPort.upload(w);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public void delete(Worktime w) throws Exception {
        try {
            w.setAssyPackingSop("");
            w.setTestSop("");
            this.deletePort.upload(w);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
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
        for (SopInfo info : l) {
            s.add(info.getSopName());
        }
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

        @Override
        public void upload(Worktime w) throws Exception {
            super.upload(w, UploadType.INSERT);
        }

        @Override
        public Map<String, String> transformData(Worktime w) throws Exception {
            Map<String, String> xmlStrings = new HashMap();
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

                if (!insertedSops.isEmpty()) {
                    SopBatchInsertRoot root = new SopBatchInsertRoot();
                    SopBatchInsertRoot.SOPINFO sopInfo = root.getSOPINFO();
                    sopInfo.setTYPENO(type);
                    sopInfo.setITEMNO(w.getModelName());
                    SopBatchInsertRoot.SOPINFO.PARTNO partNo = new SopBatchInsertRoot.SOPINFO.PARTNO();
                    List<SopBatchInsertRoot.SOPINFO.PARTNO.KEYNO> l = new ArrayList();

                    for (String sop : insertedSops) {
                        SopBatchInsertRoot.SOPINFO.PARTNO.KEYNO keyNo = new SopBatchInsertRoot.SOPINFO.PARTNO.KEYNO();
                        keyNo.setITEMNO(w.getModelName());
                        keyNo.setTYPENO(type);
                        keyNo.setSOPNAME(sop);
                        l.add(keyNo);
                    }
                    partNo.setKEYNO(l);
                    sopInfo.setPARTNO(partNo);

                    xmlStrings.put(type, this.generateXmlString(root));
                }
            }
            return xmlStrings;
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

        @Override
        public void upload(Worktime w) throws Exception {
            super.upload(w, UploadType.DELETE);
        }

        @Override
        public Map<String, String> transformData(Worktime w) throws Exception {
            Map<String, String> xmlStrings = new HashMap();
            Map<String, String> serializeSops = outer.serializeSops(w);

            for (Map.Entry<String, String> entry : serializeSops.entrySet()) {

                String type = entry.getKey();
                String sopInLocal = entry.getValue();

                Set<String> sops = outer.toSops(sopInLocal);

                outer.sopQueryPort.setTypes(type);
                Set mesSops = outer.toSops(outer.sopQueryPort.query(w));

                Set<String> deletedSops = outer.findDifference(mesSops, sops);

                int i = 1;
                for (String sop : deletedSops) {
                    SopRoot root = new SopRoot();
                    SopRoot.SOPINFO info = root.getSOPINFO();
                    info.setTYPENO(type); //站別
                    info.setITEMNO(w.getModelName()); //機種
                    info.setSOPNAME(sop); //SOP文號
                    info.setTYPENOOLD(type); //上一次站別
                    info.setITEMNOOLD(w.getModelName()); //上一次機種
                    info.setSOPNAMEOLD(sop); //上一次SOP文號
                    xmlStrings.put(type + i++, this.generateXmlString(root));
                }
            }
            return xmlStrings;
        }

    }

}
