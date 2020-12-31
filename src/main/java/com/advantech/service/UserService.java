/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.repo.UserRepository;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.model.UserProfile;
import com.advantech.repo.UnitRepository;
import com.advantech.security.State;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private UnitRepository unitRepo;

    public List<User> findAll() {
        return repo.findAll();
    }

    public List<User> findAll(PageInfo info) {
        return repo.findAll(info);
    }

    public List<User> findAll(PageInfo info, Unit unit) {
        List<User> l = repo.findAll(info);
        return l.stream()
                .filter(u -> u.getUnit().getName().equals(unit.getName()))
                .collect(toList());
    }

    public User findByPrimaryKey(Integer obj_id) {
        return repo.getOne(obj_id);
    }

    public User findByJobnumber(String jobnumber) {
        User i = repo.findByJobnumber(jobnumber);

        if (i == null) {
            return null;
        }

        //Initialize the lazy loading relative object
        Hibernate.initialize(i.getUnit());
        Hibernate.initialize(i.getFloor());
        Hibernate.initialize(i.getUserProfiles());

        return i;
    }

    public List<UserProfile> findUserProfiles(int user_id) {
        List l = new ArrayList();
        User u = this.findByPrimaryKey(user_id);
        l.addAll(u.getUserProfiles());
        return l;
    }

    public List<UserProfile> findUserNotifications(int user_id) {
        List l = new ArrayList();
        User u = this.findByPrimaryKey(user_id);
        l.addAll(u.getUserNotifications());
        return l;
    }

    public List<User> findByUnitName(String unitName) {
        Unit u = unitRepo.findFirstByName(unitName);
        return repo.findByUnit(u);
    }

    public List<User> findActive() {
        return repo.findByState(State.ACTIVE);
    }

    public int insert(User user) {
        repo.save(user);
        return 1;
    }

    public int update(User user) {
        repo.save(user);
        return 1;
    }

    public int delete(int id) {
        User user = this.findByPrimaryKey(id);
        repo.delete(user);
        return 1;
    }

    public int resetPsw() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        List<User> l = this.findAll();
        l.forEach((user) -> {
            String encryptPassord = encoder.encode(user.getJobnumber());
            user.setPassword(encryptPassord);
        });
        repo.saveAll(l);
        return 1;
    }

}
