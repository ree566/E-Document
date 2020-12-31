/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Unit;
import com.advantech.model.WorktimeColumnGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface WorktimeColumnGroupRepository extends JpaRepository<WorktimeColumnGroup, Integer>, PageableRepository<WorktimeColumnGroup, Integer> {

    public WorktimeColumnGroup findByUnit(Unit unit);

}
