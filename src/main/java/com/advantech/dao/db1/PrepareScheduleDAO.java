/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.Floor;
import com.advantech.model.db1.Line;
import com.advantech.model.db1.LineType;
import com.advantech.model.db1.PrepareSchedule;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PrepareScheduleDAO extends AbstractDao<Integer, PrepareSchedule> implements BasicDAO_1<PrepareSchedule> {

    @Override
    public List<PrepareSchedule> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PrepareSchedule findByPrimaryKey(Object obj_id) {
        return super.getByKey((Integer) obj_id);
    }

    public List<PrepareSchedule> findByDate(DateTime sD) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("onBoardDate", sD.toDate()))
                .list();
    }

    public List<PrepareSchedule> findByLineAndDate(Line line, DateTime sD) {
        return super.createEntityCriteria()
                .add(line == null ? Restrictions.isNull("line") : Restrictions.eq("line", line))
                .add(Restrictions.eq("onBoardDate", sD.toDate()))
                .list();
    }
    
    public List<PrepareSchedule> findByFloorAndLineTypeAndDate(Floor floor, List<LineType> lineType, DateTime sD) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("floor", floor))
                .add(Restrictions.in("lineType", lineType))
                .add(Restrictions.eq("onBoardDate", sD.toDate()))
                .list();
    }

    @Override
    public int insert(PrepareSchedule pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PrepareSchedule pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PrepareSchedule pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
