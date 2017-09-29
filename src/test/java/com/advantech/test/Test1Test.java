/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import static com.google.common.collect.Sets.newHashSet;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author Wei.Cheng
 */
public class Test1Test {

    

    @Test
    public void test() {
        Set<String> st = new HashSet();
        Set<String> st2 = new HashSet();
        st2.add("b");
        st2.add("c");
        System.out.println(this.findDifference(st2, st));
    }

    private Set<String> findDifference(Set<String> s1, Set<String> s2) {
        return newHashSet(Collections2.filter(s1, Predicates.not(Predicates.in(s2))));
    }
    
}
