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
 */
public interface AuditAction<T, K extends Object> {

    public List findAll();

    public List findByPrimaryKey(K id);

    public Object findByPrimaryKeyAndVersion(K id, int version);

    public List<Number> findRevisions(K id);
    
    public List forEntityAtRevision(int version);
}
