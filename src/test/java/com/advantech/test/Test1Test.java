/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.PreAssy;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() {
        PreAssy p1 = new PreAssy();
        PreAssy p2 = new PreAssy();
        
        System.out.println(ObjectUtils.compare(p1, p2));
    }

}
