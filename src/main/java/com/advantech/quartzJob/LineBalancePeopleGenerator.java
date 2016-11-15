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
import com.advantech.service.WorkTimeService;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class LineBalancePeopleGenerator implements Job {

    private static final Logger log = LoggerFactory.getLogger(LineBalancePeopleGenerator.class);

    private static LineBalancePeopleGenerator instance;

    private final WorkTimeService workTimeService;
    private final BABService babService;
    private final int numLampMaxTestRequiredPeople, numLampGroupStart, numLampGroupEnd;
    private final Double babStandard;

    private static JSONArray babToTestAssignNumOfPeopleStatus;

    private List<String> message;

    public LineBalancePeopleGenerator() {
        this.workTimeService = BasicService.getWorkTimeService();
        this.babService = BasicService.getBabService();

        PropertiesReader p = PropertiesReader.getInstance();
        this.numLampMaxTestRequiredPeople = p.getNumLampMaxTestRequiredPeople();
        this.numLampGroupStart = p.getNumLampGroupStart();
        this.numLampGroupEnd = p.getNumLampGroupEnd();
        this.babStandard = p.getBabStandard();
    }

    public static LineBalancePeopleGenerator getInstance() {
        if (instance == null) {
            instance = new LineBalancePeopleGenerator();
        }
        return instance;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        LineBalancePeopleGenerator.getInstance().generateTestPeople();
    }

    public void generateTestPeople() {
        babToTestAssignNumOfPeopleStatus = new JSONArray();
        List<BAB> l = babService.getAssyProcessing();
        for (BAB bab : l) {
            JSONObject obj = new JSONObject(bab);
            message = new ArrayList();
            Double standardVal;

            //Get the babAvg in setting group
            List balanceGroup = babService.getBABAvgsInSpecGroup(bab.getId(), numLampGroupStart, numLampGroupEnd);

            if (balanceGroup.isEmpty()) {
                message.add("The current balance is empty.");
                continue;
            } else {
                standardVal = workTimeService.getTestStandardTime(bab.getModel_name());
            }

            if (standardVal == null) {
                continue;
            }
            int maxVal = findMaxInList(balanceGroup);
            int suggestPeople = generatePeople1((double) maxVal, standardVal);
            message.add("AssyCT: " + maxVal + " , T1 standard: " + standardVal);
            message.add(bab.getLineName() + " suggest people: " + suggestPeople);
            obj.put("suggestTestPeople", suggestPeople);
            obj.put("message", message);
            babToTestAssignNumOfPeopleStatus.put(obj);
        }
    }

    private int generatePeople(Double maxVal, Double standardVal) {
        Double[] balances = new Double[numLampMaxTestRequiredPeople];
        Double[] abs = new Double[numLampMaxTestRequiredPeople];
        int people = 1;
        Double balance;
        int min = 0;

        do {
            if (people == numLampMaxTestRequiredPeople) {
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

        } while (balance - babStandard > 0 && people <= numLampMaxTestRequiredPeople);

        out.println("The closet value is " + balances[min]);

        return min + 1;
    }

    public int generatePeople1(Double maxVal, Double standardVal) {
        Map<Integer, Double> balanceResults = new HashMap();
        int people = 1;
        do {
            balanceResults.put(people, calculateBalance(maxVal, standardVal, people));
            people++;
        } while (people <= numLampMaxTestRequiredPeople);

        balanceResults = this.sortByValue(balanceResults);

        for (Map.Entry<Integer, Double> entry : balanceResults.entrySet()) {
            message.add(entry.getKey() + " 人 / 計算平衡率:" + entry.getValue());
        }

        int bestSetupPeople = 0;
        int loopCount = 0;

        for (Map.Entry<Integer, Double> entry : balanceResults.entrySet()) {
            ++loopCount;
            if (entry.getValue() >= babStandard || loopCount == numLampMaxTestRequiredPeople) {
                bestSetupPeople = entry.getKey();
                break;
            }
        }
//        out.println("The best situation in these value is set people = " + bestSetupPeople + " and lineBalance is " + bestVal + " .");
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

    public JSONArray getBabToTestAssignNumOfPeopleStatus() {
        return babToTestAssignNumOfPeopleStatus == null ? new JSONArray() : babToTestAssignNumOfPeopleStatus;
    }

}
