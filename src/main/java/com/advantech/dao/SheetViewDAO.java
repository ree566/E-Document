/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.SheetView;
import com.advantech.jqgrid.PageInfo;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 未使用，後續可能要用到view時作參考
 *
 * @author Wei.Cheng
 */
@Repository
public class SheetViewDAO extends BasicDAOImpl<Integer, SheetView> {

    public List<SheetView> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }

    @Override
    public int insert(SheetView pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(SheetView pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(SheetView pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
