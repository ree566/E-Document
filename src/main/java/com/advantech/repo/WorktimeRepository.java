/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface WorktimeRepository extends JpaRepository<Worktime, Integer>, PageableRepository<Worktime, Integer> {

    public Worktime findByModelName(String modelName);

//    public List<Worktime> findWithFullRelation(PageInfo info) {
//        String[] fetchField = {
//            "type", "businessGroup", "floor", "pending", "preAssy",
//            "flowByBabFlowId", "flowByPackingFlowId", "flowByTestFlowId",
//            "userBySpeOwnerId", "userByEeOwnerId", "userByQcOwnerId"
//        };
//
//        Criteria criteria = createEntityCriteria();
//        for (String field : fetchField) {
//            criteria.setFetchMode(field, FetchMode.JOIN);
//        }
//
//        String fetchField_c = "bwFields";
//        criteria.createAlias(fetchField_c, fetchField_c, JoinType.LEFT_OUTER_JOIN);
//
//        List l = getByPaginateInfo(criteria, info);
//        return l;
//    }
    
    @Query(value = "select * from SheetView", nativeQuery = true)
    public List<SheetView> findSheetView();
}
