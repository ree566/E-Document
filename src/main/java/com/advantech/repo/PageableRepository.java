/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.jqgrid.PageInfo;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author MFG.ESOP
 * @param <T>
 * @param <PK>
 */
public interface PageableRepository<T, PK extends Serializable> {

    public List<T> findAll(PageInfo info);

}
