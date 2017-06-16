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
public interface AuditAction {

    public List findAll(Class clz);

    public List findByPrimaryKey(Class clz, Object id);

    public Object findByPrimaryKeyAndVersion(Class clz, Object id, int version);

    public List<Number> findRevisions(Class clz, Object id);
    
    public List forEntityAtRevision(Class clz, int version);
}
