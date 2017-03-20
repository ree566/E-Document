/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.SheetView;
import com.advantech.helper.PageInfo;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetViewDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    public SheetViewDAO() {

    }

    @Override
    public Collection findAll() {
        return super.findBySQL("SELECT * FROM Sheet_Main_view");
    }

    public Collection findAll(PageInfo info) {
//        return super.findBySQL(info, "SELECT * FROM Sheet_Main_view");
        return super.findByCriteria(SheetView.class, info);
//        return super.findByHQL(
//                "select m, spe, ie, ee, floor, type, pending, labelInfo\n"
//                + "from Model m\n"
//                + "left join m.sheetSpes spe\n"
//                + "left join m.sheetIes ie\n"
//                + "left join m.sheetEes ee\n"
//                + "left join spe.floor floor\n"
//                + "left join spe.type type\n"
//                + "left join spe.pendings pending\n"
//                + "left join spe.labelInfos labelInfo\n",
//                info
//        );
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
