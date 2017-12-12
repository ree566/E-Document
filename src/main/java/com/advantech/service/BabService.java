package com.advantech.service;

import com.advantech.model.Bab;
import com.advantech.dao.BabDAO;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.TagNameComparison;
import com.advantech.model.view.BabAvg;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.google.common.base.Preconditions.*;

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

    @PostConstruct
    protected void BABService() {
    }

    public void BABDAO() {
        babDAO.BABDAO();
    }

    public List<Bab> findAll() {
        return babDAO.findAll();
    }

    public Bab findByPrimaryKey(Object obj_id) {
        return babDAO.findByPrimaryKey(obj_id);
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

    public List<String> findAllModelName() {
        return babDAO.findAllModelName();
    }

    public int insert(Bab pojo) {
        return babDAO.insert(pojo);
    }

    public int checkAndInsert(Bab b, String jobnumber) {
        List<Bab> processes = babDAO.findProcessingByLine(b.getLine().getId());
        Bab duplicate = processes.stream().filter(bab -> bab.getPo().equals(b.getPo())).findFirst().orElse(null);
        checkArgument(duplicate != null, "工單號碼已經存在");

        Bab prevBab = processes.stream().findFirst().orElse(null);

        checkArgument(
                prevBab != null && prevBab.getStartPosition() != b.getStartPosition(),
                "上一套工單與本次工單的起始站別不符，請等待上套工單完成再做投入"
        );

        babDAO.insert(b);
        TagNameComparison tagName = tagNameComparisonService.findByLineAndStation(b.getLine().getId(), 1);
        babSettingHistoryService.insert(new BabSettingHistory(b, 1, tagName.getId().getLampSysTagName(), jobnumber));

        return 1;
    }

    public int update(Bab pojo) {
        return babDAO.update(pojo);
    }

    public int stationComplete(Bab bab, int station) {
        List<BabSettingHistory> babSettings = babSettingHistoryService.findByBab(bab);

        if (bab.getIspre() == 0) {
            checkArgument(babSettings.size() == bab.getPeople(), "人員資料數量與人數不符");
        }

        BabSettingHistory setting = babSettings.stream().filter(b -> b.getStation() == station).findFirst().orElse(null);

        checkArgument(setting.getLastUpdateTime() != null, "感應器已經關閉");

        //沒有babavg，直接回傳success，等第三站關閉
        List<BabAvg> l = sqlViewService.findBabAvg(bab.getId());
        checkArgument(!l.isEmpty(), "查無統計數據，若要關閉工單請從最後一站直接做關閉動作");

        BabSettingHistory prev = babSettings.stream().filter(b -> b.getStation() == bab.getPeople() - 1).findFirst().orElse(null);
        checkArgument(prev.getLastUpdateTime() != null, "關閉失敗，請檢查上一站是否關閉");

        setting.setLastUpdateTime(new DateTime().toDate());
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
        boolean needToSave = false;
        if (babAvgs != null && !babAvgs.isEmpty()) {
            boolean prevSensorCloseFlag = true;
            bab.setBabAvgs(babAvgs);
            if (bab.getPeople() != 2) {
                List<BabSettingHistory> babSettings = babSettingHistoryService.findByBab(bab);
                BabSettingHistory prev = babSettings.stream().filter(b -> b.getStation() == bab.getPeople() - 1).findFirst().orElse(null);
                checkArgument(prev.getLastUpdateTime() == null, "關閉失敗，請檢查上一站是否關閉");
            }
            needToSave = prevSensorCloseFlag;
        }
        return (needToSave ? this.closeBabWithSaving(bab) : this.closeBabDirectly(bab));
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
