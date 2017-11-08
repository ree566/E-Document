/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.util.List;

/**
 *
 * @author Wei.Cheng
 * @param <T>
 */
public interface BasicDAO_1<T> {

    public List<T> findAll();

    public T findByPrimaryKey(Object obj_id);

    public int insert(T pojo);

    public int update(T pojo);

    public int delete(T pojo);
}
