/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 */
public interface BasicDAO {

    public Collection findAll();

    public Object findByPrimaryKey(Object obj_id);

    public int insert(Object obj);

    public int update(Object obj);

    public int delete(Object pojo);
}
