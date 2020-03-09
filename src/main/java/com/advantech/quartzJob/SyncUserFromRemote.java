/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.dao.db1.FloorDAO;
import com.advantech.dao.db1.SqlViewDAO;
import com.advantech.dao.db1.UnitDAO;
import com.advantech.dao.db1.UserDAO;
import com.advantech.dao.db1.UserProfileDAO;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.Unit;
import com.advantech.model.db1.User;
import com.advantech.model.db1.UserProfile;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.security.State;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Component
@Transactional
public class SyncUserFromRemote {

    private static final Logger logger = LoggerFactory.getLogger(SyncUserFromRemote.class);

    @Autowired
    private SqlViewDAO sqlViewDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CustomPasswordEncoder pswEncoder;

    @Autowired
    private FloorDAO floorDAO;

    @Autowired
    private UnitDAO unitDAO;

    @Autowired
    private UserProfileDAO userProfileDAO;

    private List<UserProfile> userProfiles;

    public void execute() throws Exception {
        List<UserInfoRemote> l = sqlViewDAO.findUserInfoRemote();
        List<UserInfoRemote> remoteDirectUser = l.stream()
                .filter(ur -> (ur.getDepartment().matches("(前置|組裝|測試|包裝)"))
                && ur.getDep1().contains("東湖"))
                .collect(toList());

        List<User> users = userDAO.findAll();

        List<Floor> floors = floorDAO.findAll();

        Unit mfg = unitDAO.findByPrimaryKey(1);

        userProfiles = userProfileDAO.findAll();

        //Compare which jobnumber is new and which number is old
        //沒在User_Profile_REF的User會被insert進去
        if (!remoteDirectUser.isEmpty() && !users.isEmpty()) {

            remoteDirectUser.forEach(ru -> {
                User matchesUser = users.stream()
                        .filter(a -> a.getJobnumber().equals(ru.getJobnumber()))
                        .findFirst()
                        .orElse(null);

                if (matchesUser == null) {
                    //insert new one
                    User userWithoutRole = userDAO.findByJobnumber(ru.getJobnumber());

                    Floor floor = floors.stream()
                            .filter(f -> f.getName().equals(ru.getSitefloor()))
                            .findFirst()
                            .orElse(null);

                    if (floor != null) {
                        User user = new User();
                        user.setJobnumber(ru.getJobnumber());
                        user.setUsername(ru.getName());
                        user.setUsernameCh(ru.getName());
                        user.setPassword(pswEncoder.encode(ru.getJobnumber()));
                        user.setState(State.ACTIVE);

                        user.setFloor(floor);
                        user.setUnit(mfg);

                        setUserProfle(user, ru.getDepartment());

                        userDAO.insert(user);
                        System.out.println("insert" + " " + ru.getJobnumber());
                    }
                } else {
                    //Find user's department is matches or not
                    //if not match, update department
                    setUserProfle(matchesUser, ru.getDepartment());
                    
                    matchesUser.setUsername(ru.getName());
                    matchesUser.setUsernameCh(ru.getName());

                    matchesUser.setState("1".equals(ru.getActive()) && isDateInRecent(ru.getLastUpdateTime()) ? State.ACTIVE : State.DELETED);
                    userDAO.update(matchesUser);
                    System.out.println("update" + " " + ru.getJobnumber());
                }
            });
        }
    }

    private void setUserProfle(User user, String department) {
        UserProfile preAssyRole = userProfiles.stream().filter(p -> p.getId() == 19).findFirst().orElse(null);
        UserProfile assyRole = userProfiles.stream().filter(p -> p.getId() == 14).findFirst().orElse(null);
        UserProfile pkgRole = userProfiles.stream().filter(p -> p.getId() == 15).findFirst().orElse(null);
        UserProfile testRole = userProfiles.stream().filter(p -> p.getId() == 16).findFirst().orElse(null);

        Set<UserProfile> roles = user.getUserProfiles();
        roles.remove(preAssyRole);
        roles.remove(assyRole);
        roles.remove(testRole);
        roles.remove(pkgRole);
//"ASSY_USER", "PREASSY_USER", "PACKING_USER"

        switch (department) {
            case "前置":
                roles.add(preAssyRole);
                break;
            case "組裝":
                roles.add(assyRole);
                break;
            case "測試":
                roles.add(testRole);
                break;
            case "包裝":
                roles.add(pkgRole);
                break;
            default:
                break;
        }

        user.setUserProfiles(roles);
    }

    private boolean isDateInRecent(Date d) {
        return new DateTime(d).isAfter(new DateTime().minusDays(7));
    }

}
