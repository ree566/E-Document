/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.service;

import com.advantech.dao.BasicDAOImpl;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public abstract class BasicServiceImpl<PK extends Serializable, T> implements BasicService<PK, T> {

    @Value("${HIBERNATE.JDBC.BATCHSIZE}")
    private Integer batchSize;

    protected abstract BasicDAOImpl getDao();

    @Override
    public int insert(List<T> pojo) {
        batchAction(pojo, e -> getDao().insert(e));
        return 1;
    }

    @Override
    public int update(List<T> pojo) {
        batchAction(pojo, e -> getDao().update(e));
        return 1;
    }
    
    public int merge(List<T> pojo) {
        batchAction(pojo, e -> getDao().merge(e));
        return 1;
    }

    @Override
    public int delete(List<T> pojo) {
        batchAction(pojo, e -> getDao().delete(e));
        return 1;
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public T findByPrimaryKey(PK obj_id) {
        return (T) getDao().findByPrimaryKey(obj_id);
    }

    public List<T> findByPrimaryKeys(PK... id) {
        return getDao().findByPrimaryKeys(id);
    }

    @Override
    public int insert(T pojo) {
        return getDao().insert(pojo);
    }

    @Override
    public int update(T pojo) {
        return getDao().update(pojo);
    }

    @Override
    public int delete(T pojo) {
        return getDao().delete(pojo);
    }

    private int batchAction(List<T> pojo, Function<T, Integer> func) {
        int i = 1;
        for (T w : pojo) {
            func.apply(w);
            flushIfReachFetchSize(i++);
        }
        return 1;
    }

    protected void flushIfReachFetchSize(int currentRow) {
        if (currentRow % batchSize == 0 && currentRow > 0) {
            getDao().flushSession();
        }
    }

}
