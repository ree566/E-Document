/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.FlowPermutations;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class FlowPermutationsDAO extends BasicDAOImpl<Integer, FlowPermutations> {

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

}
