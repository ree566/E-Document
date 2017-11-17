/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.model.Worktime;
import com.google.gson.Gson;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng 徒程上傳
 */
@Component
public class FlowUploadPort extends BasicUploadPort {

    private static final Logger logger = LoggerFactory.getLogger(FlowUploadPort.class);

    @Override
    protected void initJaxbMarshaller() {

    }

    @Override
    public void upload(Worktime w) throws Exception {
//        super.upload(w); //To change body of generated methods, choose Tools | Templates.
        Map result = this.transformData(w);
        System.out.println(new Gson().toJson(result));
    }

    @Override
    public Map<String, String> transformData(Worktime w) throws Exception {
        //PreAssy, Bab, Test, Pkg

        return null;
    }

}
