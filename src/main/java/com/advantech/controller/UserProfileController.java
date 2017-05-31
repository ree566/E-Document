/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.CustomPasswordEncoder;
import static com.advantech.helper.JqGridResponseUtils.toJqGridResponse;
import com.advantech.helper.PageInfo;
import com.advantech.model.User;
import com.advantech.response.JqGridResponse;
import com.advantech.security.UserProfileType;
import com.advantech.service.UserProfileService;
import com.advantech.service.UserService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
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
        return toJqGridResponse(userService.findAll(info), info);
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

        modifyMessage = userService.update(user) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return serverResponse(modifyMessage);
    }

    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    @Override
    protected ResponseEntity delete(int id) {
        String modifyMessage = userService.delete(id) == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;
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
}
