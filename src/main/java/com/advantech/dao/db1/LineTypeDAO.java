/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.ActionCode;
import com.advantech.model.db1.LineType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class LineTypeDAO extends AbstractDao<Integer, LineType> implements BasicDAO_1<LineType> {

    @Override
    public List<LineType> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public LineType findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public List<LineType> findByPrimaryKeys(Integer... obj_ids) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.in("id", obj_ids));
        return c.list();
    }
    
    public LineType findByName(String lineTypeName){
        return (LineType) super.createEntityCriteria()
                .add(Restrictions.eq("name", lineTypeName))
                .uniqueResult();
    }

    @Override
    public int insert(LineType pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(LineType pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(LineType pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
