/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.WorktimeMailManager;
import com.advantech.excel.XlsWorkBook;
import com.advantech.excel.XlsWorkSheet;
import static com.advantech.helper.HibernateObjectPrinter.print;
import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.TypeService;
import com.advantech.service.UserService;
import com.advantech.service.WorktimeService;
import com.google.gson.Gson;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@Secured({"ROLE_ADMIN", "ROLE_OPER"})
@RequestMapping(value = "/WorktimeBatchMod")
public class WorktimeBatchModController {

    @Autowired
    private WorktimeMailManager worktimeMailManager;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private PendingService pendingService;

    @Autowired
    private UserService userService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private WorktimeService worktimeService;

    private static Validator validator;

    @PostConstruct
    public void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //Check model is exist.
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String batchInsert(@RequestParam("file") MultipartFile file) throws Exception {
        List<Worktime> hgList = this.transToWorktimes(file);

        //Validate the column, throw exception when false.
        validateWorktime(hgList);

        for (Worktime w : hgList) {
            if (isModelExists(w)) {
                throw new Exception("This model is already exist.");
            }
        }

        if (worktimeService.insertWithFormulaSetting(hgList) == 1) {
            worktimeMailManager.notifyUser(hgList, "add");
            return "success";
        } else {
            return "fail";
        }
    }

    //Check current revision & model name is duplicate
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String batchUpdate(@RequestParam("file") MultipartFile file) throws Exception {
        List<Worktime> hgList = this.transToWorktimes(file);

        //Validate the column, throw exception when false.
        validateWorktime(hgList);

        for (Worktime w : hgList) {
            if (isModelExists(w)) {
                throw new Exception("This model is already exist.");
            }
        }

        return worktimeService.merge(hgList) == 1 ? "success" : "fail";
    }

    //Check model is exist
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String batchDelete(@RequestParam("file") MultipartFile file) throws Exception {
        List<Worktime> hgList = this.transToWorktimes(file);
        int[] ids = new int[hgList.size()];
        for (int i = 0; i < hgList.size(); i++) {
            ids[i] = hgList.get(i).getId();
        }

        if (worktimeService.delete(ids) == 1) {
            worktimeMailManager.notifyUser(hgList, "del");
            return "success";
        } else {
            return "fail";
        }
    }

    private boolean isModelExists(Worktime worktime) {
        Worktime existWorktime = worktimeService.findByModel(worktime.getModelName());
        if (worktime.getId() == 0) {
            return existWorktime != null;
        } else {
            return existWorktime != null && existWorktime.getId() != worktime.getId();
        }
    }

    //Update exist worktime by excel sheet.
    //Check current revision first.
    @ResponseBody
    @RequestMapping(value = "/batchUpload", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam("file") MultipartFile file) {
        //Add revision number into some column.
        //If revision not found, return error.
        //Check last revision each row, if pass, update.

        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/reUpdateAllFormulaColumn", method = {RequestMethod.GET})
    public boolean reUpdateAllFormulaColumn() {
        worktimeService.reUpdateAllFormulaColumn();
        return true;
    }

    private List<Worktime> transToWorktimes(MultipartFile file) throws Exception {
        //固定sheet name為sheet1
        XlsWorkBook workbook = new XlsWorkBook(file.getInputStream());
        XlsWorkSheet sheet = workbook.getSheet("sheet1");
        if (sheet == null) {
            throw new Exception("Sheet named \"sheet1\" not found");
        }

        //Init not relative column first.
        List<Worktime> hgList = sheet.buildBeans(Worktime.class);
        hgList = retriveRelativeColumns(sheet, hgList);

        return hgList;
    }

    private boolean validateWorktime(List<Worktime> l) throws Exception {
        Map<String, Map<String, String>> checkResult = new HashMap();
        int count = 2;
        for (Worktime w : l) {
            Set<ConstraintViolation<Worktime>> constraintViolations = validator.validate(w);
            if (!constraintViolations.isEmpty()) {
                Iterator it = constraintViolations.iterator();
                Map errors = new HashMap();
                while (it.hasNext()) {
                    ConstraintViolation violation = (ConstraintViolation) it.next();
                    errors.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
                checkResult.put("Row" + count, errors);
            }
            count++;
        }

        if (checkResult.isEmpty()) {
            return true;
        } else {
            throw new Exception(new Gson().toJson(checkResult));
        }
    }

    private List retriveRelativeColumns(XlsWorkSheet sheet, List<Worktime> hgList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception {
        Map<String, Type> typeOptions = toSelectOptions(typeService.findAll());
        Map<String, Floor> floorOptions = toSelectOptions(floorService.findAll());
        Map<String, User> userOptions = toSelectOptions(userService.findAll());
        Map<String, Flow> flowOptions = toSelectOptions(flowService.findAll());
        Map<String, Pending> pendingOptions = toSelectOptions(pendingService.findAll());
        Map<String, PreAssy> preAssyOptions = toSelectOptions(preAssyService.findAll());

        //設定關聯by name
        for (int i = 0; i < hgList.size(); i++) {
            Worktime w = hgList.get(i);
            w.setType(typeOptions.get(sheet.getValue(i, "typeName").toString()));
            w.setFloor(floorOptions.get(sheet.getValue(i, "floorName").toString()));

            String eeUserName = sheet.getValue(i, "eeOwnerName").toString();
            String speUserName = sheet.getValue(i, "speOwnerName").toString();
            String qcUserName = sheet.getValue(i, "qcOwnerName").toString();

            w.setUserByEeOwnerId(valid(eeUserName, userOptions.get(eeUserName)));
            w.setUserBySpeOwnerId(valid(speUserName, userOptions.get(speUserName)));
            w.setUserByQcOwnerId(valid(qcUserName, userOptions.get(qcUserName)));

            String babFlowName = sheet.getValue(i, "babFlowName").toString();
            String pkgFlowName = sheet.getValue(i, "packingFlowName").toString();
            String testFlowName = sheet.getValue(i, "testFlowName").toString();

            w.setFlowByBabFlowId(valid(babFlowName, flowOptions.get(babFlowName)));
            w.setFlowByPackingFlowId(valid(pkgFlowName, flowOptions.get(pkgFlowName)));
            w.setFlowByTestFlowId(valid(testFlowName, flowOptions.get(testFlowName)));

            w.setPending(pendingOptions.get(sheet.getValue(i, "pendingName").toString()));

            String preAssyName = sheet.getValue(i, "preAssyName").toString();
            w.setPreAssy(valid(preAssyName, preAssyOptions.get(preAssyName)));
        }

        return hgList;

    }

    private <T extends Object> Map<String, T> toSelectOptions(List l) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map m = new HashMap();
        if (!l.isEmpty()) {
            Object firstObj = l.get(0);
            boolean isUserObject = firstObj instanceof User;
            for (Object obj : l) {
                String name = (String) PropertyUtils.getProperty(obj, isUserObject ? "username" : "name");
                m.put(isUserObject ? name : name, obj);
            }
        }
        return m;
    }

    private <T extends Object> T valid(String objName, T obj) throws Exception {
        if (objName != null && !"".equals(objName) && obj == null) {
            throw new Exception("Object name " + objName + " found but object is not exist.");
        } else {
            return obj;
        }
    }

}