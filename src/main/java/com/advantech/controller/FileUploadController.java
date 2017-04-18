/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Floor;
import com.advantech.model.Flow;
import com.advantech.model.Identit;
import com.advantech.model.Type;
import com.advantech.model.Worktime;
import com.advantech.service.FloorService;
import com.advantech.service.FlowService;
import com.advantech.service.IdentitService;
import com.advantech.service.TypeService;
import com.advantech.service.WorktimeService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private static Integer store_line_id = 6133;
//    private static Integer store_line_id = 4228;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private IdentitService identitService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private FlowService flowService;

    /**
     * Upload single file using Spring Controller
     *
     * @param model
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFileHandler(Model model, @RequestParam("file") MultipartFile file) {

        Workbook workbook = null;
        int i = 0;

        try {
            Map floorOptions = this.tranToIdNameCompare(floorService.findAll());
            Map identitOptions = this.tranToIdNameCompare(identitService.findAll());
            Map typeOptions = this.tranToIdNameCompare(typeService.findAll());
            Map flowOptions = this.tranToIdNameCompare(flowService.findAll());

            workbook = WorkbookFactory.create(file.getInputStream());
            String fileExt = workbook instanceof HSSFWorkbook ? "xls" : (workbook instanceof XSSFWorkbook ? "xlsx" : "unknown excel type");
            String[] message = {"get excel success", "Retrive file type is: " + fileExt};
            model.addAttribute("message", message);

            Sheet sheet = workbook.getSheetAt(0);

            i = store_line_id == -1 ? 2 : store_line_id;

            for (; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 由於第 0 Row 為 title, 故 i 從 1 開始

                if (i == 4229) {
                    continue;
                }

                Row row = sheet.getRow(i); // 取得第 i Row
                if (row != null) {
                    Cell cell_A = CellUtil.getCell(row, CellReference.convertColStringToIndex("A"));
                    cell_A.setCellType(CellType.STRING);

                    Worktime w = new Worktime();
                    w.setFloor((Floor) floorOptions.get(getCellValue(row, "V")));
                    w.setFlowByTestFlowId((Flow) flowOptions.get(getCellValue(row, "AG")));
                    w.setFlowByPackingFlowId((Flow) flowOptions.get(getCellValue(row, "AH")));
                    w.setFlowByBabFlowId((Flow) flowOptions.get(getCellValue(row, "AF")));
                    w.setIdentitByEeOwnerId((Identit) identitOptions.get(getCellValue(row, "AA")));
                    w.setIdentitByQcOwnerId((Identit) identitOptions.get(getCellValue(row, "AB")));
                    w.setIdentitBySpeOwnerId((Identit) identitOptions.get(getCellValue(row, "Z")));
                    w.setType((Type) typeOptions.get(getCellValue(row, "B")));
                    w.setModelName((String) getCellValue(row, "A"));
                    w.setTotalModule((Double) getCellValue(row, "D"));
                    w.setCleanPanel((Double) getCellValue(row, "F"));
                    w.setAssy((Double) getCellValue(row, "G"));
                    w.setT1((Double) getCellValue(row, "H"));
                    w.setT2((Double) getCellValue(row, "I"));
                    w.setT3((Double) getCellValue(row, "J"));
                    w.setT4((Double) getCellValue(row, "K"));
                    w.setPacking((Double) getCellValue(row, "L"));
                    w.setUpBiRi((Double) getCellValue(row, "M"));
                    w.setDownBiRi((Double) getCellValue(row, "N"));
                    w.setBiCost((Double) getCellValue(row, "O"));
                    w.setVibration((Double) getCellValue(row, "P"));
                    w.setHiPotLeakage((Double) getCellValue(row, "Q"));
                    w.setColdBoot((Double) getCellValue(row, "R"));
                    w.setWarmBoot((Double) getCellValue(row, "S"));
                    w.setBurnIn((String) getCellValue(row, "W"));
                    w.setBiTime(getCellValue(row, "X") == null || getCellValue(row, "X") instanceof String ? 0.0 : (Double) checkAndConvertReturn(getCellValue(row, "X")));
                    w.setBiTemperature(getCellValue(row, "Y") == null || getCellValue(row, "Y") instanceof String ? 0.0 : (Double) checkAndConvertReturn(getCellValue(row, "Y")));
                    w.setKeypartA(getCellValue(row, "AD") == null ? null : ((Double) checkAndConvertReturn(getCellValue(row, "AD"))).intValue());
                    w.setKeypartB(getCellValue(row, "AE") == null ? null : ((Double) checkAndConvertReturn(getCellValue(row, "AE"))).intValue());
                    w.setPartLink(getCellValue(row, "AI") == null ? null : ((String) getCellValue(row, "AI")).charAt(0));
                    w.setCe(getCellValue(row, "AJ") == null ? 0 : 1);
                    w.setUl(getCellValue(row, "AK") == null ? 0 : 1);
                    w.setRohs(getCellValue(row, "AL") == null ? 0 : 1);
                    w.setWeee(getCellValue(row, "AM") == null ? 0 : 1);
                    w.setMadeInTaiwan(getCellValue(row, "AN") == null ? 0 : 1);
                    w.setFcc(getCellValue(row, "AO") == null ? 0 : 1);
                    w.setEac(getCellValue(row, "AP") == null ? 0 : 1);
                    w.setNInOneCollectionBox(getCellValue(row, "AQ") == null || getCellValue(row, "AQ") instanceof String ? null : (Double) checkAndConvertReturn(getCellValue(row, "AQ")));
                    w.setPartNoAttributeMaintain(getCellValue(row, "AR") == null ? 'N' : (getCellValue(row, "AR")).toString().charAt(0));
                    w.setAssyLeadTime((Double) getCellValue(row, "AU"));
                    w.setPackingLeadTime((Double) getCellValue(row, "AW"));
                    w.setProductionWt((Double) getCellValue(row, "C"));
                    w.setSetupTime((Double) getCellValue(row, "E"));
                    w.setAssyToT1((Double) getCellValue(row, "T"));
                    w.setT2ToPacking((Double) getCellValue(row, "U"));
                    w.setAssyStation(getCellValue(row, "AS") == null ? null : ((Double) getCellValue(row, "AS")).intValue());
                    w.setPackingStation(((Double) getCellValue(row, "AT")).intValue());
                    w.setAssyKanbanTime((Double) getCellValue(row, "AV"));
                    w.setPackingKanbanTime((Double) getCellValue(row, "AX"));
                    w.setCleanPanelAndAssembly((Double) getCellValue(row, "BB"));
                }
            }
            model.addAttribute("message", "Data init done.");
            store_line_id = 0;

        } catch (IOException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | EncryptedDocumentException | InvalidFormatException ex) {
            store_line_id = i;
            model.addAttribute("message", ex.getStackTrace()[0]);
        } catch (Exception ex) {
            store_line_id = i;
            Object[] message = {"Error initialize object at row number " + (i + 1), ex.getStackTrace()[0]};
            model.addAttribute("message", message);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        return "forward:fileupload.jsp";
    }

    private Object checkAndConvertReturn(Object obj) {
        if (obj instanceof String) {
            String value = (String) obj;
            return "".equals(value) ? null : (StringUtils.isNumeric(value) && value.contains(".") ? Double.parseDouble(value) : Integer.parseInt(value));
        } else {
            return obj == null ? null : obj;
        }
    }

    private Object getCellValue(Row row, String letter) {
        Cell cell = CellUtil.getCell(row, CellReference.convertColStringToIndex(letter));
        CellType cellType = cell.getCellTypeEnum();
        if (null == cellType) {
            return null;
        } else {
            switch (cellType) {
                case STRING:
                    String value = cell.getStringCellValue();
                    System.out.println(letter + ": " + " string");
                    System.out.println("-->" + (value == null || "".equals(value.trim()) ? "Empty string" : value));
                    return value == null || "".equals(value.trim()) ? null : value;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(letter + ": " + " formula-numberic");
                            System.out.println("-->" + cell.getNumericCellValue());
                            return cell.getNumericCellValue();
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(letter + ": " + " formula-string");
                            System.out.println("-->" + cell.getRichStringCellValue());
                            return null;
                    }
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        System.out.println(letter + ": " + " numberic");
                        System.out.println("-->" + cell.getDateCellValue());
                        return cell.getDateCellValue().toString();
                    } else {
                        System.out.println(letter + ": " + " double");
                        System.out.println("-->" + cell.getNumericCellValue());
                        return cell.getNumericCellValue();
                    }
                case BLANK:
                    System.out.println(letter + ": " + " empty");
                    return null;
                case BOOLEAN:
                    System.out.println(letter + ": " + " boolean");
                    System.out.println("-->" + cell.getBooleanCellValue());
                    return Boolean.toString(cell.getBooleanCellValue());
                default:
                    return null;
            }
        }
    }

    private Map tranToIdNameCompare(List l) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map m = new HashMap();
        for (Object obj : l) {
            String name = (String) PropertyUtils.getProperty(obj, "name");
            m.put(name, obj);
        }
        return m;
    }

    /**
     * Upload multiple file using Spring Controller
     *
     * @param model
     * @param files
     * @return
     */
    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    public String uploadMultipleFileHandler(Model model, @RequestParam("file") MultipartFile[] files) {

        String[] message = this.copyFileToServer(files);
        model.addAttribute("message", message);

        return "forward:fileupload.jsp";
    }

    private String[] copyFileToServer(MultipartFile... multipartFiles) {
        List<String> message = new ArrayList();

        // Creating the directory to store file
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "tmpFiles");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile file : multipartFiles) {
            System.out.println(file.getContentType());

            String name = file.getOriginalFilename();
            try {
                byte[] bytes = file.getBytes();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
                try (BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile))) {
                    stream.write(bytes);
                }

                logger.info("Server File Location=" + serverFile.getAbsolutePath());

                message.add("You successfully uploaded file: " + name + " ");
            } catch (IOException e) {
                message.add("You failed to upload => " + e.getMessage());
            }
        }
        return message.toArray(new String[message.size()]);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception exception) {
        Map<String, Object> model = new HashMap<>();
        if (exception instanceof MaxUploadSizeExceededException) {
            model.put("message", exception.getMessage());
        } else {
            model.put("message", "Unexpected error: " + Arrays.toString(exception.getStackTrace()));
        }

        return new ModelAndView("/fileupload", model);
    }
}
