/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.security.State;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, PageableRepository<User, Integer> {

    public User findByJobnumber(String jobnumber);

    public List<User> findByUnit(Unit unit);

    public List<User> findByUnitAndState(Unit unit, State state);

    public List<User> findByState(State state);

}
