/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.dao;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Cobot;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class CobotDAO extends BasicDAOImpl<Integer, Cobot> {

    public List<Cobot> findAll(PageInfo info) {
        return super.getByPaginateInfo(info);
    }
}
