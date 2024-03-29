/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Wei.Cheng
 */
//@WebAppConfiguration
//@ContextConfiguration(locations = {
//    "classpath:servlet-context.xml",
//    "classpath:hibernate.cfg.xml"
//})
//@RunWith(SpringJUnit4ClassRunner.class)
public class WorktimeBatchModControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WorktimeService worktimeService;

    //CRUD testing.
    @Transactional
    @Rollback(true)
//    @Test
    public void testInsert() throws Exception {
        //Data rows: 2
        MockMultipartFile firstFile = new MockMultipartFile("file", "sample1.xls", "text/plain", getData("sample1.xls"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/WorktimeBatchMod/add")
                .file(firstFile))
                //                .with(user(new User())))
                .andExpect(status().is(HttpStatus.OK.value()));

        Worktime w = worktimeService.findByModel("test12311");
        assertTrue(w != null);
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testUpdate() throws Exception {

        //Data rows: 2
        MockMultipartFile firstFile = new MockMultipartFile("file", "sample2.xls", "text/plain", getData("sample2.xls"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/WorktimeBatchMod/update")
                .file(firstFile))
                .andExpect(status().is(HttpStatus.OK.value()));

        Worktime w = worktimeService.findByModel("test1122");
        assertEquals(w.getType().getName(), "DVT");
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testDelete() throws Exception {

        //Data rows: 2
        MockMultipartFile firstFile = new MockMultipartFile("file", "sample5.xls", "text/plain", getData("sample5.xls"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/WorktimeBatchMod/delete")
                .file(firstFile))
                .andExpect(status().is(HttpStatus.OK.value()));

        Worktime w = worktimeService.findByModel("test1122");
        assertTrue(w == null);
    }

    //Invalid sheet testing.
    @Transactional
    @Rollback(true)
//    @Test
    public void testModelIsExist() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "sample5.xls", "text/plain", getData("sample5.xls"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/WorktimeBatchMod/update")
                .file(firstFile))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testSheetNotFound() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "sample3.xls", "text/plain", getData("sample3.xls"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/WorktimeBatchMod/update")
                .file(firstFile))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testDataInvalidCheck() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("file", "sample4.xls", "text/plain", getData("sample4.xls"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/WorktimeBatchMod/update")
                .file(firstFile))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andDo(MockMvcResultHandlers.print());
    }

    private byte[] getData(String fileName) throws IOException {
        String file = "C:\\Users\\Wei.Cheng\\Desktop\\testXls\\" + fileName;
        Path fileLocation = Paths.get(file);
        return Files.readAllBytes(fileLocation);
    }

}
