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
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    private final DecimalFormat formatter;
    private final DecimalFormat formatter2;

    private final int startCountMininumQuantity, startCountMininumStandardTime, minTotalStandardTime, basicSuggestPeople = 1;

    public LineBalancePeopleGenerator() {
        this.workTimeService = BasicService.getWorkTimeService();
        this.babService = BasicService.getBabService();

        PropertiesReader p = PropertiesReader.getInstance();
        this.numLampMaxTestRequiredPeople = p.getNumLampMaxTestRequiredPeople();
        this.numLampGroupStart = p.getNumLampGroupStart();
        this.numLampGroupEnd = p.getNumLampGroupEnd();
        this.babStandard = p.getBabStandard();
        this.startCountMininumQuantity = p.getNumLampMinQuantity();
        this.startCountMininumStandardTime = p.getNumLampMinStandardTime();
        this.minTotalStandardTime = p.getNumLampMinTotalStandardTime();

        formatter = new DecimalFormat("#.##%");
        formatter2 = new DecimalFormat("#.##");
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

            //Get the babAvg in setting group
            List<Map> balanceGroup = babService.getBABAvgsInSpecGroup(bab.getId(), numLampGroupStart, numLampGroupEnd);
            Integer standardVal;

            if (balanceGroup.isEmpty()) {
                message.add("The current balance is empty.");
                continue;
            } else {
                standardVal = workTimeService.getTestStandardTime(bab.getModel_name());
            }

            if (standardVal == null) {
                message.add("This model's standardTime is not setting.");
                continue;
            }

            Map firstData = balanceGroup.get(0);
            Integer currentGroup = firstData.containsKey("currentGroup") ? (Integer) firstData.get("currentGroup") : null;

            Integer suggestPeople;
            Integer totalQuantity = BasicService.getBabService().getPoTotalQuantity(bab.getPO());
            if (totalQuantity < startCountMininumQuantity || standardVal <= startCountMininumStandardTime || totalQuantity == null || standardVal * totalQuantity <= minTotalStandardTime) {
                message.add("Total quantity is: " + totalQuantity + " piece");
                message.add("T1 standard: " + standardVal);
                suggestPeople = basicSuggestPeople;
            } else {
                Double maxVal = ((BigDecimal) findMaxInList(balanceGroup)).doubleValue();
                suggestPeople = generatePeople1((int) Math.floor(maxVal), standardVal);
                message.add("AssyCT: " + formatter2.format(maxVal) + " , T1 standard: " + standardVal);
            }
            message.add("Current piece: " + currentGroup + " piece");
            message.add(bab.getLineName() + " suggest people: " + suggestPeople);
            obj.put("suggestTestPeople", suggestPeople);
            obj.put("message", message);
            babToTestAssignNumOfPeopleStatus.put(obj);
        }
    }

    private int generatePeople(Integer maxVal, Integer standardVal) {
        Double[] balances = new Double[numLampMaxTestRequiredPeople];
        Double[] abs = new Double[numLampMaxTestRequiredPeople];
        Integer people = 1;
        Double balance;
        Integer min = 0;

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

    public int generatePeople1(Integer maxVal, Integer standardVal) {
        Map<Integer, Double> balanceResults = new HashMap();
        Integer people = basicSuggestPeople;
        do {
            balanceResults.put(people, calculateBalance(maxVal, standardVal, people));
            people++;
        } while (people <= numLampMaxTestRequiredPeople);

        balanceResults = this.sortByValue(balanceResults);

        for (Map.Entry<Integer, Double> entry : balanceResults.entrySet()) {
            message.add("People: " + entry.getKey() + " / Balance:" + formatter.format(entry.getValue()));
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

    //(組裝CT + (測試標工 / 人數)) / (max(組裝CT, (測試標工 / 人數)) * 2)  因為取組裝&測試的線平衡率，所以需要*2
    private Double calculateBalance(Integer maxVal, Integer standardVal, Integer people) {
        Double babCT = (double) maxVal, testStandard = (double) standardVal;
        Double numerator = babCT + (testStandard / people);
        Double denominator = findMax(babCT, testStandard / people) * 2;
        Double result = numerator / denominator;
        return result;
    }

    private Object findMaxInList(List<Map> l) {
        List avgs = new ArrayList();
        for (Map m : l) {
            avgs.add(m.get("average"));
        }
        return findMax(avgs);
    }

    private Object findMax(List l) {
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
