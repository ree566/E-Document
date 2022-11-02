/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.helper;

import java.time.Duration;
import java.util.Map;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author Wei.Cheng
 */
public class EmployeeZoneUtils {

    private final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    private String webapiUrl;

    public void setWebapiUrl(String webapiUrl) {
        this.webapiUrl = webapiUrl;
    }

    private WebClient getClient() {
        return WebClient.create(webapiUrl);
    }

    public Map<String, String>[] findUser(String jobnumber) {
        return getClient()
                .get()
                .uri("/Employee/" + jobnumber)
                .retrieve()
                .bodyToMono(Map[].class)
                .block(REQUEST_TIMEOUT);
    }

    public boolean login(String username, String password) {
        return false;
    }
}
