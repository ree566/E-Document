/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Identit;
import com.advantech.model.IdentitDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class IdentitService {

    private final IdentitDAO identitDAO;

    protected IdentitService() {
        identitDAO = new IdentitDAO();
    }

    public Identit getIdentit(String jobnumber) {
        return identitDAO.getIdentit(jobnumber);
    }

    public boolean getIdentit(String jobnumber, String password) {
        return identitDAO.userLogin(jobnumber, password);
    }

    public boolean newIdentit(Identit i) {
        return identitDAO.newIdentit(packageObjectToList(i));
    }

    public boolean updateIdentit(Identit i) {
        return identitDAO.updateIdentit(packageObjectToList(i));
    }

    private List packageObjectToList(Object... o) {
        List l = new ArrayList();
        l.addAll(Arrays.asList(o));
        return l;
    }

    public boolean updatePassword(int userNo, String password) {
        return identitDAO.updateUsersPassword(userNo, password);
    }

    public boolean deleteIdentit(int userNo) {
//        return identitDAO.updateIdentitServingStatus(leaveSign, userNo);
        throw new UnsupportedOperationException();
    }
}
