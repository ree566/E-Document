/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Identit;
import com.advantech.model.IdentitDAO;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class IdentitService {

    @Autowired
    private IdentitDAO identitDAO;

    public Identit getIdentit(String jobnumber) {
        return identitDAO.getIdentit(jobnumber);
    }

    public List<Map> getIdentitView() {
        return identitDAO.getIdentitView();
    }

    public List<Map> getIdentitViewByPermission() {
        return identitDAO.getIdentitViewByPermission();
    }

    public List<Map> getIdentitViewByPermission(int sitefloor) {
        return identitDAO.getIdentitViewByPermission(sitefloor);
    }

}
