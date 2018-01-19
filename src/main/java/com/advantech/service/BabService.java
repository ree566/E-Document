package com.advantech.service;

import com.advantech.model.Bab;
import com.advantech.dao.BabDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.BabAlarmHistory;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.BabStatus;
import com.advantech.model.ReplyStatus;
import com.advantech.model.TagNameComparison;
import com.advantech.model.view.BabAvg;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.google.common.base.Preconditions.*;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class BabService {

    @Autowired
    private BabDAO babDAO;

    @Autowired
    private LineBalancingService lineBalancingService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private BabAlarmHistoryService babAlarmHistoryService;

    public void BABDAO() {
        babDAO.BABDAO();
    }

    public List<Bab> findAll() {
        return babDAO.findAll();
    }

    public Bab findByPrimaryKey(Object obj_id) {
        return babDAO.findByPrimaryKey(obj_id);
    }

    public Bab findWithLineInfo(int bab_id) {
        return babDAO.findWithLineInfo(bab_id);
    }

    public List<Bab> findByDate(DateTime sD, DateTime eD) {
        return babDAO.findByDate(sD, eD);
    }

    public List<Bab> findByModelAndDate(String modelName, DateTime sD, DateTime eD) {
        return babDAO.findByModelAndDate(modelName, sD, eD);
    }

    public List<Bab> findTodays() {
        DateTime d = new DateTime();
        return this.findByDate(d, d);
    }

    public List<Bab> findByMultipleClause(DateTime sD, DateTime eD, int lineType_id, int floor_id, boolean isAboveTenPcs) {
        return babDAO.findByMultipleClause(sD, eD, lineType_id, floor_id, isAboveTenPcs);
    }

    public List<Bab> findProcessing() {
        return babDAO.findProcessing();
    }

    public List<Bab> findProcessingByLine(int line_id) {
        return babDAO.findProcessingByLine(line_id);
    }

    public List<Bab> findProcessingByTagName(String tagName) {
        return babDAO.findProcessingByTagName(tagName);
    }

    public List<String> findAllModelName() {
        return babDAO.findAllModelName();
    }

    public int insert(Bab pojo) {
        return babDAO.insert(pojo);
    }

    public int checkAndInsert(Bab b, String jobnumber, TagNameComparison tag) {
        List<Bab> processes = babDAO.findProcessingByLine(b.getLine().getId());
        Bab duplicate = processes.stream().filter(bab -> bab.getPo().equals(b.getPo())).findFirst().orElse(null);
        checkArgument(duplicate == null, "工單號碼已經存在");
        babDAO.insert(b);
        babSettingHistoryService.insertByBab(b, tag);
        return 1;
    }

    public int update(Bab pojo) {
        return babDAO.update(pojo);
    }

    public int stationComplete(Bab bab, BabSettingHistory setting) {
        return this.stationComplete(bab, setting, true);
    }

    public int stationComplete(Bab bab, BabSettingHistory setting, boolean isNeedCheckPrev) {
        checkArgument(bab.getIspre() != 1, "前置工單請直接從最後一站關閉");

        if (isNeedCheckPrev) {
            BabSettingHistory prev = babSettingHistoryService.findByBabAndStation(bab, setting.getStation() - 1);

            if (setting.getStation() == 2 && prev.getLastUpdateTime() == null) {
                prev.setLastUpdateTime(new Date());
                babSettingHistoryService.update(prev);
            } else {
                checkArgument(prev.getLastUpdateTime() != null, "關閉失敗，請檢查上一站是否關閉");
            }
        }

        setting.setLastUpdateTime(new Date());
        babSettingHistoryService.update(setting);

        return 1;

    }

    /*
        檢查統計值是否為空，空值直接讓使用者作結束
        檢查上一顆sensor使否有在紀錄中，有把紀錄記在LineBalancingMain
        所有儲存動作已經交由usp_CloseBabWithSaving
        不需要在code做其他動作(除了save avg data into Line_Balancing.dbo.Line_Balancing_Main table)
     */
    public int closeBab(Bab bab) {
        List<BabAvg> babAvgs = sqlViewService.findBabAvg(bab.getId()); //先各站別取平衡率再算平均
        List<BabSettingHistory> babSettings = babSettingHistoryService.findByBab(bab);
        boolean needToSave = false;
        if (babAvgs != null && !babAvgs.isEmpty()) {
            bab.setBabAvgs(babAvgs);
            if (bab.getPeople() != 2) {
                BabSettingHistory prev = babSettings.stream()
                        .filter(b -> b.getStation() == bab.getPeople() - 1)
                        .reduce((first, second) -> second).orElse(null);
                checkArgument(prev.getLastUpdateTime() != null, "關閉失敗，請檢查上一站是否關閉");
            }
            needToSave = true;
        }

        if (needToSave) {
            this.closeBabWithSaving(bab);
            bab.setBabStatus(BabStatus.CLOSED);
            if (bab.getReplyStatus() == ReplyStatus.NO_NEED_TO_REPLY && isBalanceAboveAvg(bab)) {
                bab.setReplyStatus(ReplyStatus.UNREPLIED);
            }
        } else {
            this.closeBabDirectly(bab);
            bab.setBabStatus(BabStatus.NO_RECORD);
        }
        //Update Status flag
        this.update(bab);

        BabSettingHistory setting = babSettings.stream()
                .filter(b -> b.getStation() == bab.getPeople())
                .reduce((first, second) -> second).orElse(null);
        this.stationComplete(bab, setting, needToSave == true);

        return 1;
    }

    private boolean isBalanceAboveAvg(Bab b) {
        BabAlarmHistory alarmHistory = babAlarmHistoryService.findByBab(b.getId());
        checkArgument(alarmHistory != null, "Alarm history not found");
        return alarmHistory.getIspass() == 1;
    }

    public int closeBabDirectly(Bab b) {
        return babDAO.closeBabDirectly(b);
    }

    public int closeBabWithSaving(Bab b) {
        babDAO.closeBabWithSaving(b);
        lineBalancingService.insert(b);
        return 1;
    }

    public int delete(Bab pojo) {
        return babDAO.delete(pojo);
    }

}
