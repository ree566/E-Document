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
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
    private final Double babStandard;

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
        instance = LineBalancePeopleGenerator.getInstance();
        int babCTAvg = 200;
        int testAvgTime = 800;

        int babCTFloatingRange = 150;
        int peopleDetectMininum = 2;

        int countFlag = 0;
        int maxDetectCountTime = 10;

        int loopCount = 0;
        int maxLoopTime = 100;

        try {
            while (true) {
                if (countFlag == maxDetectCountTime || loopCount == maxLoopTime) {
                    out.println("Reach max detect count time, loop break;");
                    break;
                }

                int babCT = babCTAvg + (int) ((Math.random() * babCTFloatingRange * 2) + 1) - (babCTFloatingRange);
                int people = instance.generatePeople1(babCT + 0.0, testAvgTime + 0.0);
                out.println("The babCT is " + babCT + " and the testAvgTime is " + testAvgTime + " ,we suggest " + people + " peoples.");
                if (people >= peopleDetectMininum) {
                    ++countFlag;
                }
                ++loopCount;
                Thread.sleep(1 * 1000);
            }
        } catch (InterruptedException ex) {
            out.println(ex);
        }

    }

    public void generateTestPeople() {
        out.println("--------------------------------------------------");
        babToTestAssignNumOfPeopleStatus = new JSONObject();
        List<BAB> l = babService.getProcessingBAB();
        for (BAB bab : l) {
            out.println(new Gson().toJson(bab));
            Double standardVal = getStandardTime(bab.getModel_name());
            List balanceGroup = babService.getBABAvgsInSpecGroup(bab.getId());
            if (balanceGroup.isEmpty() || standardVal == -1) {
                out.println("balance group size is " + balanceGroup.size());
                out.println("standardVal is " + standardVal);
                continue;
            }
            int maxVal = findMaxInList(balanceGroup);
            int suggestPeople = generatePeople((double) maxVal, standardVal);
            out.println("The maxium average is " + maxVal + " , and the standard time is " + standardVal + " ...");
            out.println(bab.getName() + " suggest people = " + suggestPeople);
            babToTestAssignNumOfPeopleStatus.put(bab.getName(), suggestPeople);
        }
    }

    private int generatePeople(Double maxVal, Double standardVal) {
        Double[] balances = new Double[maxTestRequiredPeople];
        Double[] abs = new Double[maxTestRequiredPeople];
        int people = 1;
        Double balance;
        int min = 0;

        do {
            if (people == maxTestRequiredPeople) {
                return people;
            }

            balance = calculateBalance(maxVal, standardVal, people);

            out.println("Caculate balance :" + balance);
            out.println("Balance - standard = " + (balance - babStandard));

            int index = people - 1;
            balances[index] = balance;
            abs[index] = Math.abs(balances[index] - babStandard);
            out.println("Caculate abs :" + abs[index]);
            if (abs[index] < abs[min]) {
                min = index;
            }

            out.println("Continue finding...");
            people++;

        } while (balance - babStandard > 0 && people <= maxTestRequiredPeople);

        out.println("The closet value is " + balances[min]);

        return min + 1;
    }

    private int generatePeople1(Double maxVal, Double standardVal) {
        Map<Integer, Double> balanceResults = new HashMap();
        int people = 1;
        do {
            balanceResults.put(people, calculateBalance(maxVal, standardVal, people));
            people++;
        } while (people <= maxTestRequiredPeople);

        balanceResults = this.sortByValue(balanceResults);
        out.println("Array status: " + new Gson().toJson(balanceResults));

        Double bestVal = 0.0;
        int bestSetupPeople = 0;
        int loopCount = 0;

        for (Map.Entry<Integer, Double> entry : balanceResults.entrySet()) {
            ++loopCount;
            if (entry.getValue() >= babStandard || loopCount == maxTestRequiredPeople) {
                bestSetupPeople = entry.getKey();
                bestVal = entry.getValue();
                break;
            }
        }
        out.println("The best situation in these value is set people = " + bestSetupPeople + " and lineBalance is " + bestVal + " .");
        return bestSetupPeople;
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return (e1.getValue()).compareTo(e2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private Double calculateBalance(Double maxVal, Double standardVal, int people) {
        Double numerator = maxVal + (standardVal / people);
        Double denominator = findMax(maxVal, standardVal / people) * 2;
        Double result = numerator / denominator;
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
        return l.isEmpty() ? null : Collections.max(l);
    }

    private Integer findMax(Integer... vals) {
        return (Integer) this.findMaxObj((Object[]) vals);
    }

    private Double findMax(Double... vals) {
        return (Double) this.findMaxObj((Object[]) vals);
    }

    private Object findMaxObj(Object... obj) {
        Arrays.sort(obj);
        return obj.length == 0 ? null : obj[obj.length - 1];
    }

    private int findCloset(int... numbers) {
        int myNumber = (int) Math.floor(this.babStandard * 100);
        int distance = Math.abs(numbers[0] - myNumber);
        int idx = 0;
        for (int c = 1; c < numbers.length; c++) {
            int cdistance = Math.abs(numbers[c] - myNumber);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return numbers[idx];
    }

    private Double findCloset(Double... numbers) {
        Double myNumber = this.babStandard;
        Double distance = Math.abs(numbers[0] - myNumber);
        int idx = 0;
        for (int c = 1; c < numbers.length; c++) {
            Double cdistance = Math.abs(numbers[c] - myNumber);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return numbers[idx];
    }

    public static JSONObject getBabToTestAssignNumOfPeopleStatus() {
        return babToTestAssignNumOfPeopleStatus;
    }

}
