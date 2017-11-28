/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;

/**
 *
 * @author Wei.Cheng
 */
public interface UploadPort {

    public void insert(Worktime w) throws Exception;

    public void update(Worktime w) throws Exception;

    public void delete(Worktime w) throws Exception;
}
