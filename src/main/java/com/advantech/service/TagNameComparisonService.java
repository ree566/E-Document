/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.TagNameComparison;
import com.advantech.dao.TagNameComparisonDAO;
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

    public List<TagNameComparison> findAll() {
        return tagNameComparisonDAO.findAll();
    }

    public TagNameComparison findByPrimaryKey(Object obj_id) {
        return tagNameComparisonDAO.findByPrimaryKey(obj_id);
    }

    public TagNameComparison findByLampSysTagName(String tagName) {
        return tagNameComparisonDAO.findByLampSysTagName(tagName);
    }

    public TagNameComparison findByLineAndStation(int line_id, int station) {
        return tagNameComparisonDAO.findByLineAndStation(line_id, station);
    }

    public int insert(TagNameComparison pojo) {
        return tagNameComparisonDAO.insert(pojo);
    }

    public int update(TagNameComparison pojo) {
        return tagNameComparisonDAO.update(pojo);
    }

    public int delete(TagNameComparison pojo) {
        return tagNameComparisonDAO.delete(pojo);
    }

}
