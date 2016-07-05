/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 每兩個小時幫忙把BAB資料做儲存
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.advantech.service.PrepareScheduleService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class LineBalancePeopleGenerator {

    private static final Logger log = LoggerFactory.getLogger(LineBalancePeopleGenerator.class);

    private static LineBalancePeopleGenerator instance;

    private final PrepareScheduleService psService;
    private final BABService babService;
    private final int maxTestRequiredPeople;
    private final double babStandard;

    private static JSONObject babToTestAssignNumOfPeopleStatus;

    public LineBalancePeopleGenerator() {
        this.psService = BasicService.getPrepareScheduleService();
        this.babService = BasicService.getBabService();
        this.maxTestRequiredPeople = PropertiesReader.getInstance().getMaxTestRequiredPeople();
        this.babStandard = PropertiesReader.getInstance().getBabStandard();
    }

    public static LineBalancePeopleGenerator getInstance() {
        if (instance == null) {
            instance = new LineBalancePeopleGenerator();
        }
        return instance;
    }

    public static void main(String arg0[]) {
        int people = LineBalancePeopleGenerator.getInstance().generatePeople(5.5, 10.0);
        System.out.println("Suggess people = " + people);
    }

    public void generateTestPeople() {
        System.out.println("--------------------------------------------------");
        babToTestAssignNumOfPeopleStatus = new JSONObject();
        List<BAB> l = babService.getProcessingBAB();
        for (BAB bab : l) {
            System.out.println(new Gson().toJson(bab));
            Double standardVal = getStandardTime(bab.getModel_name());
            List balanceGroup = babService.getBABAvgsInSpecGroup(bab.getId());
            if (balanceGroup.isEmpty() || standardVal == -1) {
                System.out.println("balance group size is " + balanceGroup.size());
                System.out.println("standardVal is " + standardVal);
                continue;
            }
            int maxVal = findMaxInList(balanceGroup);
            int suggessPeople = generatePeople((double) maxVal, standardVal);
            System.out.println("The maxium average is " + maxVal + " , and the standard time is " + standardVal + " ...");
            System.out.println(bab.getName() + " suggess people = " + suggessPeople);
            babToTestAssignNumOfPeopleStatus.put(bab.getName(), suggessPeople);
        }
    }

    private int generatePeople(Double maxVal, Double standardVal) {
        Double[] balances = new Double[4];
        Double[] abs = new Double[4];
        int people = 1;
        double balance;
        int min = 0;

        do {
            if (people == maxTestRequiredPeople) {
                return people;
            }

            balance = calculateBalance(maxVal, standardVal, people);

            System.out.println("Caculate balance :" + balance);
            System.out.println("Balance - standard = " + (balance - babStandard));

            int index = people - 1;
            balances[index] = balance;
            abs[index] = Math.abs(balances[index] - babStandard);
            System.out.println("Caculate abs :" + abs[index]);
            if (abs[index] < abs[min]) {
                min = index;
            }

            System.out.println("Continue finding...");
            people++;

        } while (balance - babStandard > 0 && people <= maxTestRequiredPeople);

        System.out.println("Ths closet value is " + balances[min]);

        return min + 1;
    }

    private Double calculateBalance(Double maxVal, Double standardVal, int people) {
//        double numerator = maxVal + standardVal;
//        double denominator = findMax(maxVal, standardVal) * people;

        double numerator = maxVal + (standardVal / people);
        double denominator = findMax(maxVal, standardVal / people) * 2;
        double result = numerator / denominator;
        return result;
    }

    private Double getStandardTime(String modelName) {
        List<Map> l = psService.getTestStandardTime(modelName);
        if (l.isEmpty()) {
            return -1.0;
        }
        Map date = l.get(0);
        return (Double) date.get("standardTime") * 60;
    }

    private Integer findMaxInList(List<Map> l) {
        List avgs = new ArrayList();
        for (Map m : l) {
            avgs.add(m.get("average"));
        }
        return findMax(avgs);
    }

    private Integer findMax(List<Integer> l) {
        if (l.isEmpty()) {
            return null;
        }
        Collections.sort(l);
        return l.get(l.size() - 1);
    }

    private Integer findMax(Integer... vals) {

        int max = 0;

        for (Integer i : vals) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    private Double findMax(Double... vals) {
        double max = Double.NEGATIVE_INFINITY;

        for (double d : vals) {
            if (d > max) {
                max = d;
            }
        }
        return max;
    }

    public static JSONObject getBabToTestAssignNumOfPeopleStatus() {
        return babToTestAssignNumOfPeopleStatus;
    }

}
