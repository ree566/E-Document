/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.BasicDAO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public interface BasicService<PK extends Serializable, T> extends BasicDAO<PK, T> {

    public int insert(List<T> pojo);

    public int update(List<T> pojo);

    public int delete(List<T> pojo);
}
