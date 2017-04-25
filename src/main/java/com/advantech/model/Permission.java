/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

/**
 *
 * @author Wei.Cheng
 */
public class Permission {

    public static int VISITOR_PERMISSION;
    public static int UNIT_OPERATOR_PERMISSION;
    public static int UNIT_LEADER_PERMISSION;
    public static int SYSOP_PERMISSION;
    public static int TEST_FIELD_ACCESS_LIMIT_PERMISSION;
    private static boolean isParamSet = false;

    public static void initPermission(int visitor_p, int unit_oper_p, int unit_leader_p, int sysop_p, int test_area_limit_p) throws Exception {
        if (!isParamSet) {
            VISITOR_PERMISSION = visitor_p;
            UNIT_OPERATOR_PERMISSION = unit_oper_p;
            UNIT_LEADER_PERMISSION = unit_leader_p;
            SYSOP_PERMISSION = sysop_p;
            TEST_FIELD_ACCESS_LIMIT_PERMISSION = test_area_limit_p;
            isParamSet = true;
        } else {
            throw new Exception("The permissions are alreay set!");
        }
    }
}
