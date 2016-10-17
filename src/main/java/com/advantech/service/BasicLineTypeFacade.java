/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.helper.PropertiesReader;
import com.advantech.helper.TxtWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public abstract class BasicLineTypeFacade {

    protected boolean controlJobFlag = true;//Change the flag if you want to pause the job outside.

    protected boolean isWriteToTxt, isWriteToDB;

    protected final TxtWriter txtWriter;

    protected final int ALARM_SIGN = 1, NORMAL_SIGN = 0;

    protected boolean resetFlag;//設定Flag，以免txt一植被重複init，當True才write txt.

    protected final Map dataMap;//占存資料用map
    protected JSONObject processingJsonObject;//暫存處理過的資料

    protected boolean isNeedToOutputResult;//從改寫的function 得知是否要write the result to txt(有人亮燈時).

    protected String txtName;//先設定txtName

    protected BasicLineTypeFacade() {

        txtWriter = TxtWriter.getInstance();

        PropertiesReader p = PropertiesReader.getInstance();
        isWriteToTxt = p.isWriteToTxt();
        isWriteToDB = p.isWriteToDB();

        resetFlag = true;
        dataMap = new HashMap();
    }

    public void processingDataAndSave() throws Exception {
        isNeedToOutputResult = this.generateData();
        if (controlJobFlag == true) {
            if (isNeedToOutputResult) {
                outputResult(dataMap, txtName);
            } else {
                resetOutputResult(txtName);
            }
        }
    }

    protected void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    /**
     * Init the super.dataMap first.
     */
    protected abstract void initMap();

    /**
     * Generate data and put the data into variable processingJsonObject.
     *
     * @return Someone is under the balance or not.
     */
    protected abstract boolean generateData();

    private void outputResult(Map m, String txtName) throws IOException {
        if (isWriteToTxt) {
            txtWriter.writeTxtWithFileName(m, txtName);
        }
        if (isWriteToDB) {
            saveAlarmSignToDb(m);
        }
        if (isWriteToTxt || isWriteToDB) {
            resetFlag = true;
        }
    }

    private void resetOutputResult(String txtName) throws IOException {
        if (isWriteToTxt || isWriteToDB) {
            if (resetFlag == true) {
                initMap();
                if (isWriteToTxt) {
                    outputResult(dataMap, txtName);
                }
                if (isWriteToDB) {
                    resetDbAlarmSign();
                }
                resetFlag = false;
            }
        }
    }

    private boolean saveAlarmSignToDb(Map map) {
        return setDbAlarmSign(mapToAlarmSign(map));
    }

    protected List<AlarmAction> mapToAlarmSign(Map map) {
        List l = new ArrayList();
        if (map != null && !map.isEmpty()) {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                String tableId = key.toString();
                int action = (int) map.get(key);
                l.add(new AlarmAction(tableId, action));
            }
        }
        return l;
    }

    /**
     * Set the data into database signal into database.
     *
     * @param l
     * @return
     */
    protected abstract boolean setDbAlarmSign(List<AlarmAction> l);

    protected abstract boolean resetDbAlarmSign();

    protected boolean hasDataInCollection(Collection c) {
        return c != null && !c.isEmpty();
    }

    public void initInnerObjs() {
        dataMap.clear();
        this.processingJsonObject = null;
    }

    public JSONObject getJSONObject() {
        return this.processingJsonObject;
    }

    public Map getMap() {
        return this.dataMap;
    }

    public void isNeedToOutput(boolean controlJobFlag) {
        this.controlJobFlag = controlJobFlag;
    }

}
