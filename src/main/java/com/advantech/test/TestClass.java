/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.entity.User;
import com.advantech.webservice.WebServiceRV;
import com.advantech.webservice.WebServiceTX;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String[] args) {
//        WebServiceTX tx = WebServiceTX.getInstance();
//        try {
//            tx.kanbanUserLogin("A-P03539");
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
        WebServiceRV rv = WebServiceRV.getInstance();
        User user = rv.getMESUser("A-F0107");
        System.out.println(new Gson().toJson(user));
        
        WebServiceTX tx = WebServiceTX.getInstance();
        try {
            tx.kanbanUserLogin("A-F0107");
            System.out.println("success");
        } catch (Exception ex) {
            System.out.println("fail");
        }
    }

}

