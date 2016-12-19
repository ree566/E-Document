/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String arg0[]) {
        Map<String, Integer[]> m = new HashMap();

        Integer[] arr1 = {11, 35, 21, 18, 37, 20, 8}, arr2 = {1, 7, 25, 36, 21, 35, 39}, arr3 = {7, 19, 17, 30, 37, 32, 3};

        m.put("0103", arr1);
        m.put("0108", arr2);
        m.put("0112", arr3);

//        Scanner sc = new Scanner(System.in);
//        Integer[] testArr = new Integer[7];
//
//        for (int i = 0; i < testArr.length; i++) {
//            testArr[i] = sc.nextInt();
//        }
        Integer[] testArr = {11, 35, 21, 18, 37, 20, 8};
        List<Integer> testList = Arrays.asList(testArr);

        for (Map.Entry<String, Integer[]> entry : m.entrySet()) {
            List<Integer> subList = Arrays.asList(entry.getValue());
            List<Integer> intersection = (List) CollectionUtils.intersection(testList, subList);
            if (!intersection.isEmpty()) {
                System.out.println(entry.getKey() + " " + intersection);
            }
        }
    }
}
