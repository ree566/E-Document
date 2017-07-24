/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.WorktimeMailManager;
import com.advantech.excel.XlsWorkBook;
import com.advantech.excel.XlsWorkSheet;
import com.advantech.model.BusinessGroup;
import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Pending;
import com.advantech.model.PreAssy;
import com.advantech.model.Type;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.BusinessGroupService;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.math.NumberUtils;
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

    @Autowired
    private BusinessGroupService businessGroupService;

    @Autowired
    private AuditService auditService;

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
        List<Worktime> hgList = this.transToWorktimes(file, false);

        //Validate the column, throw exception when false.
        validateWorktime(hgList);

        for (Worktime w : hgList) {
            w.setId(0);
            checkModelExists(w);
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
        List<Worktime> hgList = this.transToWorktimes(file, true);

        //Validate the column, throw exception when false.
        validateWorktime(hgList);

        for (Worktime w : hgList) {
            checkModelExists(w);
        }

        return worktimeService.merge(hgList) == 1 ? "success" : "fail";
    }

    //Check model is exist
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String batchDelete(@RequestParam("file") MultipartFile file) throws Exception {
        List<Worktime> hgList = this.transToWorktimes(file, false);
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

    private void checkModelExists(Worktime worktime) throws Exception {
        Worktime existWorktime = worktimeService.findByModel(worktime.getModelName());
        boolean checkFlag;
        if (worktime.getId() == 0) {
            checkFlag = existWorktime != null;
        } else {
            checkFlag = existWorktime != null && existWorktime.getId() != worktime.getId();
        }
        if (checkFlag == true) {
            throw new Exception("This modelName &lt;" + worktime.getModelName() + "&gt; is already exist.");
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

    private List<Worktime> transToWorktimes(MultipartFile file, boolean checkRevision) throws Exception {
        //固定sheet name為sheet1
        try (XlsWorkBook workbook = new XlsWorkBook(file.getInputStream())) {
            XlsWorkSheet sheet = workbook.getSheet("sheet1");
            if (sheet == null) {
                throw new Exception("Sheet named \"sheet1\" not found");
            }

            //Init not relative column first.
            List<Worktime> hgList = sheet.buildBeans(Worktime.class);

            //If id is zero, the action is add.
            if (checkRevision) {
                Integer revisionNum = retriveRevisionNumber(sheet);
                checkRevision(hgList, revisionNum);
            }

            hgList = retriveRelativeColumns(sheet, hgList);
            return hgList;
        }
    }

    //Check revision number is greater than current revision
    private void checkRevision(List<Worktime> l, Integer revisionNum) throws Exception {

        Integer currentRevision = auditService.findLastRevisions(Worktime.class).intValue();

        //Check revision history contain update datas or not.
        if (revisionNum < currentRevision) {
            for (int i = revisionNum + 1; i <= currentRevision; i++) {
                List<Worktime> revData = auditService.findModifiedAtRevision(Worktime.class, i);
                for (Worktime w : l) {
                    for (Worktime rev_w : revData) {
                        if (rev_w.getId() == w.getId()) {
                            throw new Exception("欲更改的資料包含已逾期的資料行 &lt;" + w.getModelName() + "&gt; ，請重新下載excel再上傳.");
                        }
                    }
                }
            }
        }
        //If all pass, begin update.
    }

    //Check the revision number into info is valid
    //Check the revision append on the last of the excel file
    private Integer retriveRevisionNumber(XlsWorkSheet sheet) throws Exception {
        Object revisionInfo = sheet.getValue(0, "Revision");
        String revKeyWord = "revision: ";
        if (revisionInfo == null || "".equals(revisionInfo)) {
            throw new Exception("Your revision number is not valid!");
        }
        String decodeString = new String(Base64.decodeBase64(revisionInfo.toString().getBytes()));
        if (!decodeString.contains(revKeyWord)) {
            throw new Exception("Can not retrive the revision number!");
        }
        String revNumString = decodeString.split(revKeyWord)[1];
        if (NumberUtils.isNumber(revNumString)) {
            return Integer.parseInt(revNumString);
        } else {
            throw new Exception("Invalid revision number!");
        }
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
        Map<String, BusinessGroup> businessGroupOptions = toSelectOptions(businessGroupService.findAll());

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

            String businessGroupName = sheet.getValue(i, "businessGroupName").toString();
            w.setBusinessGroup(valid(businessGroupName, businessGroupOptions.get(businessGroupName)));
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
