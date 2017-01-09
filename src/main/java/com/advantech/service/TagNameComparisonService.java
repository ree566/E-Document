/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.TagNameComparison;
import com.advantech.model.TagNameComparisonDAO;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class TagNameComparisonService{

    private final TagNameComparisonDAO tagNameComparisonDAO;

    public TagNameComparisonService() {
        this.tagNameComparisonDAO = new TagNameComparisonDAO();
    }

    public List<TagNameComparison> getAll() {
        return tagNameComparisonDAO.getAll();
    }

    public List<TagNameComparison> getOne(String orginTagName) {
        return tagNameComparisonDAO.getOne(orginTagName);
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
}
