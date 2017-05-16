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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@Secured("ROLE_ADMIN")
@RequestMapping(value = "/User")
public class UserProfileController extends CrudController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @ResponseBody
    @RequestMapping(value = SELECT_URL, method = {RequestMethod.GET})
    @Override
    public JqGridResponse findAll(@ModelAttribute PageInfo info) {
        return toJqGridResponse(userService.findAll(info), info);
    }

    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    @Override
    public ResponseEntity update(
            @RequestParam final String oper, 
            @ModelAttribute User user,
            BindingResult bindingResult) {

        String modifyMessage;
        int responseFlag = 0;

        switch (oper) {
            case ADD:
                encryptPassword(user);
                Set profiles = new HashSet();
                profiles.add(userProfileService.findByType(UserProfileType.USER.getUserProfileType()));
                user.setUserProfiles(profiles);
                responseFlag = userService.insert(user);
                break;
            case EDIT:
                User existUser = userService.findByPrimaryKey(user.getId());
                if (!user.getPassword().equals(existUser.getPassword())) {
                    encryptPassword(user);
                }
                user.setUserProfiles(existUser.getUserProfiles());
                responseFlag = userService.update(user);
                break;
            case DELETE:
                responseFlag = userService.delete(userService.findByPrimaryKey(user.getId()));
                break;
        }

        modifyMessage = responseFlag == 1 ? this.SUCCESS_MESSAGE : this.FAIL_MESSAGE;

        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(modifyMessage) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(modifyMessage);
    }

    private void encryptPassword(User user) {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder();
        String encryptPassord = encoder.encode(user.getPassword());
        user.setPassword(encryptPassord);
    }

}
