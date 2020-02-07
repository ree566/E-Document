/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.dao.FloorDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.dao.UnitDAO;
import com.advantech.dao.UserDAO;
import com.advantech.dao.UserProfileDAO;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.model.Floor;
import com.advantech.model.Unit;
import com.advantech.model.User;
import com.advantech.model.UserProfile;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.security.State;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
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

    public void execute() throws Exception {
        List<UserInfoRemote> l = sqlViewDAO.findUserInfoRemote();
        List<UserInfoRemote> remoteAssyUser = l.stream()
                .filter(ur -> (ur.getDepartment().matches("(前置|組裝|測試|包裝)")) && ur.getDep1().contains("東湖"))
                .collect(toList());

        List<User> assyUser = userDAO.findByRole("ASSY_USER");

        List<Floor> floors = floorDAO.findAll();

        Unit mfg = unitDAO.findByPrimaryKey(1);

        UserProfile preAssyRole = userProfileDAO.findByPrimaryKey(19);
        UserProfile assyRole = userProfileDAO.findByPrimaryKey(14);
        UserProfile pkgRole = userProfileDAO.findByPrimaryKey(15);
        UserProfile testRole = userProfileDAO.findByPrimaryKey(16);

        //Compare which jobnumber is new and which number is old
        if (!remoteAssyUser.isEmpty() && !assyUser.isEmpty()) {

            remoteAssyUser.forEach(ru -> {
                User matchesUser = assyUser.stream()
                        .filter(a -> a.getJobnumber().equals(ru.getJobnumber()))
                        .findFirst()
                        .orElse(null);

                if (matchesUser == null) {
                    //insert new one
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

                        userDAO.insert(user);
                    }
                } else {
                    //Find user's department is matches or not
                    //if not match, update department
                    Set<UserProfile> roles = matchesUser.getUserProfiles();
                    roles.remove(preAssyRole);
                    roles.remove(assyRole);
                    roles.remove(testRole);
                    roles.remove(pkgRole);

                    switch (ru.getDepartment()) {
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

                    matchesUser.setUserProfiles(roles);

                    matchesUser.setState("1".equals(ru.getActive()) ? State.ACTIVE : State.DELETED);
                    userDAO.update(matchesUser);
                }
            });
        }

    }

}
