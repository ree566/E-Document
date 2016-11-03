/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.ActionCodeMapping;
import com.advantech.model.ActionCodeMappingDAO;
import com.advantech.model.LineOwnerMappingDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class LineOwnerMappingService {

    private final LineOwnerMappingDAO lineOwnerMappingDAO;

    protected LineOwnerMappingService() {
        lineOwnerMappingDAO = new LineOwnerMappingDAO();
    }

    public List<Map> getLineOwnerMappingView() {
        return lineOwnerMappingDAO.getLineOwnerMappingView();
    }

}
