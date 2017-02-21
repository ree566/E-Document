/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import static java.lang.System.out;
import java.util.Arrays;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass2 {

    public static void main(String[] args) {

        CTriangle[] objArr = new CTriangle[10];

        for (int i = 0; i < 10; i++) {
            int[] y = makeThreeNum();

            try {
                objArr[i] = i == 9 ? new CTriangle(y[0], y[1], y[2]) : new CTriangle(20, 21, 29);
                System.out.print("3邊為\t" + objArr[i].getEdge1() + "\t" + objArr[i].getEdge2() + "\t" + objArr[i].getEdge3() + "\t");
                System.out.println(objArr[i].isRightAngled() ? "是直角三角形" : "不是直角三角形");
            } catch (Exception ex) {
                out.println(ex.getCause());
            }
        }
    }

    public static int[] makeThreeNum() {
        int[] x = new int[3];
        x[0] = (int) (Math.random() * 24 + 5);
        x[1] = (int) (Math.random() * 40 + 9);
        x[2] = (int) (Math.random() * 60 + 29);
        Arrays.sort(x);
        return x;
    }

}

class CTriangle {

    private int edge1, edge2, edge3;

    public CTriangle(int edge1, int edge2, int edge3) throws Exception {

        if (isLegal(edge1, edge2, edge3)) {
            this.edge1 = edge1;
            this.edge2 = edge2;
            this.edge3 = edge3;
        } else {
            throw new Exception("Something is is not legal.");
        }
    }

    private static boolean isLegal(int a, int b, int c) {
        if (((a <= b) && (b <= c))) {
            if (((c - a) < b) && ((a + b) > c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRightAngled() {
        return (edge3 * edge3 == edge1 * edge1 + edge2 * edge2);
    }

    public int getEdge1() {
        return edge1;
    }

    public void setEdge1(int edge1) {
        this.edge1 = edge1;
    }

    public int getEdge2() {
        return edge2;
    }

    public void setEdge2(int edge2) {
        this.edge2 = edge2;
    }

    public int getEdge3() {
        return edge3;
    }

    public void setEdge3(int edge3) {
        this.edge3 = edge3;
    }

}
