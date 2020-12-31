/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.FlowPermutations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface FlowPermutationsRepository extends JpaRepository<FlowPermutations, Integer>, PageableRepository<FlowPermutations, Integer> {

    @Query(value = "SELECT  top 1 code FROM Flow_Permutations order by CONVERT(INT, SUBSTRING(code,PATINDEX('%[0-9]%',code), LEN(code))) desc",
            nativeQuery = true)
    public String findLastCode();

}
