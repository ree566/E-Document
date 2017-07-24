/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    @Test
    public void test() throws Exception {
        String revKeyWord = "revision:";
        String revString = "revision:598";
        int currentRev = 599;

        String revNumString = revString.split(revKeyWord)[1];
        if (NumberUtils.isNumber(revNumString)) {
            Integer rev = Integer.parseInt(revNumString);

            if (rev < currentRev) {
                System.out.println("欲更改的資料包含已逾期的資料行，請重新下載excel再上傳.");
            } else {
                System.out.println("Begin update.");
            }
        }

    }

}
