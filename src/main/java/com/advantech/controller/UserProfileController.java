/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.CustomPasswordEncoder;
import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.User;
import com.advantech.jqgrid.JqGridResponse;
import com.advantech.model.UserProfile;
import com.advantech.security.State;
import com.advantech.security.UserProfileType;
import com.advantech.service.UserService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/User")
public class UserProfileController extends CrudController<User> {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    protected JqGridResponse read(@ModelAttribute PageInfo info) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List l;
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            l = userService.findAll(info);
        } else {
            l = userService.findAll(info, user.getUnit());
        }
        return toJqGridResponse(l, info);
    }

    @ResponseBody
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity insert(User user, BindingResult bindingResult) throws Exception {

        checkUserRole(user);
        String modifyMessage;
        encryptPassword(user);

        modifyMessage = userService.insert(user) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(@ModelAttribute User user, BindingResult bindingResult) throws Exception {

        String modifyMessage;

        checkUserRole(user);
        Set notifications = user.getUserNotifications();
        if (notifications != null && !notifications.isEmpty() && notifications.iterator().next() == null) {
            user.setUserNotifications(null);
        }

        User existUser = userService.findByPrimaryKey(user.getId());
        if (!user.getPassword().equals(existUser.getPassword())) {
            encryptPassword(user);
        }

        modifyMessage = userService.update(user) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return serverResponse(modifyMessage);
    }

    private void checkUserRole(User user) throws Exception {
        User userInSession = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<UserProfile> s = user.getUserProfiles();
        for (UserProfile u : s) {
            if (!userInSession.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) && UserProfileType.ADMIN.getUserProfileType().equals(u.getName())) {
                throw new Exception("Role admin only can add by admin user.");
            }
        }

    }

    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity delete(int id) {
        User u = userService.findByPrimaryKey(id);
        u.setState(State.DELETED.getState());
        String modifyMessage = userService.update(u) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
        return serverResponse(modifyMessage);
    }

    private void encryptPassword(User user) {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        String encryptPassord = encoder.encode(user.getPassword());
        user.setPassword(encryptPassord);
    }

    @ResponseBody
    @RequestMapping(value = "/pswReset/all", method = {RequestMethod.GET})
    protected String resetPsw() {
        return userService.resetPsw() == 1 ? "Done." : "Fail.";
    }

    //編輯USER_ROLE用
    @ResponseBody
    @RequestMapping(value = SELECT_URL + "/userProfiles", method = {RequestMethod.GET})
    public List<UserProfile> findUserRole(@RequestParam int userId) {
        return userService.findUserProfiles(userId);
    }

    @ResponseBody
    @RequestMapping(value = SELECT_URL + "/userNotifications", method = {RequestMethod.GET})
    public List<UserProfile> findUserNotification(@RequestParam int userId) {
        return userService.findUserNotifications(userId);
    }

}
