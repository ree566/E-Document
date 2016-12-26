/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import java.util.Collection;

/**
 *
 * @author Wei.Cheng
 * @param <T>
 */
public class TestClass<T> {

    public TestClass(T obj) {//  泛型（支援任何陣列、集合、字串）

        if (obj instanceof Collection) {// 如果是集合，就轉成Object陣列

            Object[] o = new Object[((Collection) obj).size()];
            int i = 0;
            for (Object x : (Collection) obj) {
                o[i++] = x;
            }

            int len = ((Collection) obj).size() - 1;
            perm(o, 0, len);

        } else if (obj instanceof String) {//如果是字串，就切割成Object陣列

            String[] s = ((String) obj).split("");

//            String[] s2 = new String[s.length - 1];//第一個是空白，所以必須去掉
//            for (int i = 0; i < s2.length; i++) {
//                s2[i] = s[i + 1];
//            }
//
//            int len = ((Object[]) s2).length - 1;
            perm((Object[]) s, 0, s.length - 1);

        } else {//  任何陣列都可以直接轉換成Object陣列

            int len = ((Object[]) obj).length - 1;
            perm((Object[]) obj, 0, len);

        }
    }

    private void perm(Object[] list, int k, int m) {

        if (k == m) {//  印出行

            for (int i = 0; i <= m; i++) {
                System.out.print(list[i] + "　");
            }
            System.out.println();

        } else {//  繼續交換

            for (int i = k; i <= m; i++) {
                swap(list, k, i);  //  交換
                perm(list, k + 1, m);  //  遞迴，後面2個參數相等表示可印出
                swap(list, k, i);  //  （因為是傳址呼叫，所以要換回來）
            }

        }
    }

    private void swap(Object[] array, int x1, int x2) {//  交換
        Object z = array[x1];
        array[x1] = array[x2];
        array[x2] = z;
    }
}
