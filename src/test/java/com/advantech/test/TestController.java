/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.webservice.Factory;
import javax.ws.rs.core.MediaType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestController {

    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();  //初始化MockMvc对象
    }

//    @Test
    public void testFqcService() throws Exception {
        String responseString = mockMvc.perform(
                post("/FqcController/stationComplete").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fqc.id", "407")
                        .param("timeCost", "15")
                        .param("remark", "test")
        ).andExpect(status().isOk()) //返回的状态是200
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------返回的json = " + responseString);
    }

//    @Test
    public void testM3Model() throws Exception {
        assertEquals("UPOS540FP1801-T", getModel("PSI6484ZA", Factory.DEFAULT)); //Test M3 model
    }

//    @Test
    public void testM2Model() throws Exception {
        assertEquals("APAX-5580-433AE", getModel("FII6245ZA", Factory.TEMP2)); //Test M2 model
    }

//    @Test
    public void testM6Model() throws Exception {
        assertEquals("IDK-C15G-IEXGA2E", getModel("AEI6068RQ", Factory.TEMP1)); //Test M6 model
    }

    private String getModel(String po, Factory f) throws Exception {
        String responseString = mockMvc.perform(
                get("/ModelController/findModelNameByPoAndFactory").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("po", po)
                        .param("factory", f.toString())
        ) //返回的状态是200
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------返回的json = " + responseString);
        return responseString;
//        .andExpect(status().isOk())
    }
    
//    @Test
    public void testArrayParamInput() throws Exception{
        String responseString = mockMvc.perform(
                get("/BabLineProductivityExcludeController/insert").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id[]", "1,2,3,4,5,6")
        ) //返回的状态是200
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------返回的json = " + responseString);
    }
    
    @Test
    public void testCloseBab() throws Exception{
        String responseString = mockMvc.perform(
                post("/BabOtherStationController/stationComplete").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("bab_id", "33428")
                        .param("tagName", "L3-S-3")
                        .param("jobnumber", "A-8236")
                        .param("pcs", "1,1,1")
        ) //返回的状态是200
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("--------返回的json = " + responseString);
    }
}
