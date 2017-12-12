/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.dao.BasicDAO.SQL;
import com.advantech.model.TagNameComparison;
import com.advantech.model.TagNameComparisonId;
import java.sql.Connection;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class TagNameComparisonDAO extends AbstractDao<TagNameComparisonId, TagNameComparison> implements BasicDAO_1<TagNameComparison> {

    @Override
    public List<TagNameComparison> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public TagNameComparison findByPrimaryKey(Object obj_id) {
        return super.getByKey((TagNameComparisonId) obj_id);
    }
    
    public TagNameComparison findByLampSysTagName(String tagName){
        return (TagNameComparison) super.createEntityCriteria()
                .add(Restrictions.eq("id.lampSysTagName", tagName))
                .uniqueResult();
    }
    
    public TagNameComparison findByLineAndStation(int line_id, int station){
        return (TagNameComparison) super.createEntityCriteria()
                .add(Restrictions.eq("line.id", line_id))
                .add(Restrictions.eq("position", station))
                .uniqueResult();
    }

    @Override
    public int insert(TagNameComparison pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(TagNameComparison pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(TagNameComparison pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
