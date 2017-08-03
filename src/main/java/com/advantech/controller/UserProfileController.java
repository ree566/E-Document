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
import com.advantech.model.Flow;
import com.advantech.model.UserProfile;
import com.advantech.security.State;
import com.advantech.security.UserProfileType;
import com.advantech.service.UserNotificationService;
import com.advantech.service.UserProfileService;
import com.advantech.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@Secured({"ROLE_OPER", "ROLE_ADMIN"})
@RequestMapping(value = "/User")
public class UserProfileController extends CrudController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    public JqGridResponse read(@ModelAttribute PageInfo info) {
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
    protected ResponseEntity insert(User user, BindingResult bindingResult) {
        String modifyMessage;
        encryptPassword(user);
        Set profiles = new HashSet();
        profiles.add(userProfileService.findByType(UserProfileType.USER.getUserProfileType()));
        user.setUserProfiles(profiles);

        modifyMessage = userService.insert(user) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    public ResponseEntity update(@ModelAttribute User user, BindingResult bindingResult) {

        String modifyMessage;

        User existUser = userService.findByPrimaryKey(user.getId());
        if (!user.getPassword().equals(existUser.getPassword())) {
            encryptPassword(user);
        }
        user.setUserProfiles(existUser.getUserProfiles());
        user.setUserNotifications(existUser.getUserNotifications());

        modifyMessage = userService.update(user) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return serverResponse(modifyMessage);
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
    public String resetPsw() {
        return userService.resetPsw() == 1 ? "Done." : "Fail.";
    }

    //編輯USER_ROLE用
    @ResponseBody
    @RequestMapping(value = SELECT_URL + "_sub", method = {RequestMethod.GET})
    public List<UserProfile> findUserRole(@RequestParam int id) {
        return userService.findUserProfiles(id);
    }

    //編輯subgroup用
    @ResponseBody
    @RequestMapping(value = UPDATE_URL + "_sub", method = {RequestMethod.POST})
    protected ResponseEntity updateUserRole(String roles, @RequestParam int parentFlowId) {
        
        String modifyMessage = "";
        
        String[] roleNames = roles.split(",");

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }
}
