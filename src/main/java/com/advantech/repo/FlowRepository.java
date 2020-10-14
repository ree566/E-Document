/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Flow;
import com.advantech.model.FlowGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface FlowRepository extends JpaRepository<Flow, Integer> {

    public List<Flow> findByFlowGroupOrderByName(FlowGroup flowGroup);

    public Flow findByName(String name);

    

}
