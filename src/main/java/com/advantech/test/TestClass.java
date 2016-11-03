/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.BasicDAO;
import com.advantech.quartzJob.CountermeasureAlarm;
import com.advantech.service.BasicService;
import com.advantech.service.LineOwnerMappingService;
import static java.lang.System.out;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) throws Exception {
        BasicDAO.dataSourceInit1();
        new CountermeasureAlarm().sendMail();
    }
}
