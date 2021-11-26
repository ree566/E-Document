/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowPermutations;
import com.advantech.model.PreAssy;
import java.util.List;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FlowPermutationsDAO extends AbstractDao<Integer, FlowPermutations> implements BasicDAO<FlowPermutations> {

    @Override
    public List<FlowPermutations> findAll() {
        return createEntityCriteria().list();
    }

    public List<FlowPermutations> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    public String findLastCode() {
        return (String) super.getSession().createSQLQuery("SELECT  top 1 code "
                + "FROM Flow_Permutations "
                + "order by CONVERT(INT, SUBSTRING(code,PATINDEX('%[0-9]%',code),"
                + "LEN(code))) desc")
                .uniqueResult();
    }

    @Override
    public FlowPermutations findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(FlowPermutations pojo) {
        getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(FlowPermutations pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(FlowPermutations pojo) {
        getSession().delete(pojo);
        return 1;
    }

}
