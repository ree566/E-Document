/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Pending;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PendingDAO extends BasicDAOImpl<Integer, Pending> {

    public List<Pending> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

}
