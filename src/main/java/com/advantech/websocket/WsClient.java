/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.websocket;

import hello.wsdl.GetWeather;
import hello.wsdl.GetWeatherResponse;
import hello.wsdl.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class WsClient extends WebServiceGatewaySupport {
    
    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public GetWeatherResponse simpleSendAndReceive() {
        ObjectFactory factory = new ObjectFactory();
        GetWeather w = factory.createGetWeather();
        w.setCountryName("Taiwan");
        w.setCityName("Taipei");
//        webServiceTemplate.setMarshaller(marshaller);
        GetWeatherResponse response = (GetWeatherResponse) webServiceTemplate.marshalSendAndReceive(w);
        return response;
    }

}
