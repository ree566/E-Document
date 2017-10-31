/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.TagNameComparison;
import com.advantech.model.TagNameComparisonDAO;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class TagNameComparisonService {

    @Autowired
    private TagNameComparisonDAO tagNameComparisonDAO;

    public List<TagNameComparison> getAll() {
        return tagNameComparisonDAO.getAll();
    }

    public List<TagNameComparison> getOne(String orginTagName) {
        return tagNameComparisonDAO.getOne(orginTagName);
    }

    public List<TagNameComparison> getByLine(int lineId) {
        return tagNameComparisonDAO.getByLine(lineId);
    }

    public boolean insert(List<TagNameComparison> l) {
        return tagNameComparisonDAO.insert(l);
    }

    public boolean update(List<TagNameComparison> l) {
        return tagNameComparisonDAO.update(l);
    }

    public boolean delete(List<TagNameComparison> l) {
        return tagNameComparisonDAO.delete(l);
    }

    public boolean deleteOne(TagNameComparison tagNameComparison) {
        return tagNameComparisonDAO.deleteOne(tagNameComparison);
    }
    
    public boolean sensorStationInit(){
        return tagNameComparisonDAO.sensorStationInit();
    }
    
}
