/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Worktime;
import java.lang.reflect.Field;

/**
 *
 * @author Wei.Cheng
 */
public class Test1 {

    public static void main(String arg0[]) {
        String testFieldName = "model_name,type_id,clean_panel,assy,t1,t2,t3,t4,packing,up_bi_ri,down_bi_ri,bi_cost,vibration,hi_pot_leakage,cold_boot,warm_boot,assy_to_t1,t2_to_packing,floor_id,pending,pending_time,burn_in,bi_time,bi_temperature,spe_owner_id,ee_owner_id,qc_owner_id,assy_packing_sop,test_sop,keypart_a,keypart_b,pre_assy,bab_flow,test_flow,packing_flow,part_link,cae,ul,rohs,weee,made_in_taiwan,fcc,eac,n_in_one_collection_boxs,part_no_attribute_maintain";
        checkField(Worktime.class, testFieldName.split(","));
    }

    private static void checkField(Class clz, String... fieldNames) {
        Class cls = clz;
        for (String fieldName : fieldNames) {
            System.out.println(fieldName + " testing...");
            try {
                for (Class acls = cls; acls != null; acls = acls.getSuperclass()) {
                    Field field = acls.getDeclaredField(fieldName);
                    System.out.println("\t" + field.getName() + " is exist");
                }
            } catch (NoSuchFieldException ex) {
                System.out.println("\t\t" + fieldName + " is not exist.");
            }
        }
    }

}
