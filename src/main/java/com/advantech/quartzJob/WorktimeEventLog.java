/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.MailManager;
import com.advantech.model.AuditedRevisionEntity;
import com.advantech.model.Flow;
import com.advantech.model.PreAssy;
import com.advantech.model.Worktime;
import java.util.List;
import java.util.Objects;
import javax.mail.MessagingException;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditDisjunction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Component
@Transactional
public class WorktimeEventLog {

    private static final Logger log = LoggerFactory.getLogger(WorktimeEventLog.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private MailManager mailManager;

    @Value("#{contextParameters[pageTitle] ?: ''}")
    private String pageTitle;

    private AuditReader getReader() {
        Session session = sessionFactory.getCurrentSession();
        return AuditReaderFactory.get(session);
    }

    public void execute() {
        String[] to = {"Wei.Cheng@advantech.com.tw"};
        String[] cc = new String[0];

        String subject = "【" + pageTitle + "系統訊息】大表Update log";
        String text = generateMailBody();

        try {
            mailManager.sendMail(to, cc, subject, text);
        } catch (MessagingException ex) {
            log.error("Send worktime log job fail." + ex.toString());
        }
    }

    private String generateMailBody() {
        StringBuilder sb = new StringBuilder();

        String[] checkFields = {
            "totalModule", "cleanPanel", "assy", "t1", "t2", "t3", "t4", "packing",
            "flowByTestFlowId", "flowByPackingFlowId", "flowByBabFlowId", "preAssy"
        };
        /*
            Send mail on 11:00 and 16:00
            When 11:00, time range prevDay 16:00 to currentDay 11:00
            When 16:00, time range currentDay 11:00 to currentDay 16:00
            Don't send mail when nothing has changed
         */

        DateTime now = new DateTime().withHourOfDay(9).withMinuteOfHour(0).withSecondOfMinute(0);
        int currentHour = now.getHourOfDay();

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy/M/d HH:mm:SS");
        DateTime sD = currentHour < 11 ? now.minusDays(1).withHourOfDay(16) : now.withHourOfDay(11);
        DateTime eD = currentHour < 11 ? now.withHourOfDay(11) : now.withHourOfDay(16);

        sb.append("Date ");
        sb.append(dtfOut.print(sD));
        sb.append(" - ");
        sb.append(dtfOut.print(eD));

        AuditReader reader = this.getReader();

        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, false);

        AuditDisjunction disjunction = AuditEntity.disjunction();

        for (String field : checkFields) {
            disjunction.add(AuditEntity.property(field).hasChanged());
        }

        q.add(disjunction);
        q.add(AuditEntity.revisionType().eq(RevisionType.MOD));
        q.add(AuditEntity.revisionProperty("REVTSTMP").between(sD.getMillis(), eD.getMillis()));

        List<Object[]> ws = q.getResultList();

        for (int i = 0; i < ws.size(); i++) {
            sb.append("------------------");
            Object[] triplet = ws.get(i);

            Worktime entity = (Worktime) triplet[0];
            AuditedRevisionEntity revisionEntity = (AuditedRevisionEntity) triplet[1];
            RevisionType revisionType = (RevisionType) triplet[2];

            Worktime prevEntity = this.getPreviousVersion(entity, revisionEntity.getREV());

            sb.append(String.format("ModelName: %s, AUD: %d, ChangeBy: %s, At: %s <br/>", entity.getModelName(),
                    revisionEntity.getREV(), revisionEntity.getUsername(),
                    dtfOut.print(new DateTime(revisionEntity.getREVTSTMP()))));

            for (String field : checkFields) {
                try {
                    Object o1 = PropertyUtils.getProperty(entity, field);
                    Object o2 = PropertyUtils.getProperty(prevEntity, field);
                    if (!Objects.equals(o1, o2)) {
                        if (o1 instanceof Flow) {
                            if (equalsWithId(o1, o2) == false) {
                                sb.append(String.format("Different on %s %s -> %s <br/>", field, o2 == null ? "null" : ((Flow) o2).getName(), o1 == null ? "null" : ((Flow) o1).getName()));
                            }
                        } else if (o1 instanceof PreAssy) {
                            if (equalsWithId(o1, o2) == false) {
                                sb.append(String.format("Different on %s %s -> %s <br/>", field, o2 == null ? "null" : ((PreAssy) o2).getName(), o1 == null ? "null" : ((PreAssy) o1).getName()));
                            }
                        } else {
                            sb.append(String.format("Different on %s %s -> %s <br/>", field, o2 == null ? "null" : o2.toString(), o1 == null ? "null" : o1.toString()));
                        }
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }

            }
            sb.append("------------------");
        }
        return sb.toString();
    }

    private boolean equalsWithId(Object o1, Object o2) throws Exception {
        return Objects.equals(PropertyUtils.getProperty(o1, "id"), PropertyUtils.getProperty(o2, "id"));
    }

    private Worktime getPreviousVersion(Worktime entity, int current_rev) {

        AuditReader reader = this.getReader();

        Number prior_revision = (Number) reader.createQuery()
                .forRevisionsOfEntity(entity.getClass(), false, true)
                .addProjection(AuditEntity.revisionNumber().max())
                .add(AuditEntity.id().eq(entity.getId()))
                .add(AuditEntity.revisionNumber().lt(current_rev))
                .getSingleResult();

        if (prior_revision != null) {
            return reader.find(entity.getClass(), entity.getId(), prior_revision);
        } else {
            return null;
        }
    }
}
