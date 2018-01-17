/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 *
 * @author Wei.Cheng
 */
public class HibernateObjectPrinter {

    public static void print(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Hibernate5Module hbm = new Hibernate5Module();
            hbm.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
            mapper.registerModule(hbm);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            System.out.println(mapper.writeValueAsString(obj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
