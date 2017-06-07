/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.security;

/**
 *
 * @author Wei.Cheng
 */
public enum Permission {
    GUEST("GUEST"),
    USER("USER"),
    ADMIN("ADMIN");

    String userProfileType;

    private Permission(String userProfileType) {
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType() {
        return userProfileType;
    }
}
