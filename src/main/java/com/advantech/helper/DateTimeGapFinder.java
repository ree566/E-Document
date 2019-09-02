/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Stack;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 * https://github.com/erfangc/JodaTimeGapFinder/blob/master/src/org/erfangc/jodatime/DateTimeGapFinder.java
 */
@Component
public class DateTimeGapFinder {

    /**
     * Finds gaps on the time line between a list of existing {@link Interval}
     * and a search {@link Interval}
     *
     * @param existingIntervals
     * @param searchInterval
     * @return The list of gaps
     */
    public List<Interval> findGaps(List<Interval> existingIntervals, Interval searchInterval) {
        List<Interval> gaps = new ArrayList<>();

        DateTime searchStart = searchInterval.getStart();
        DateTime searchEnd = searchInterval.getEnd();

        if (existingIntervals.isEmpty() || hasNoOverlap(existingIntervals, searchInterval, searchStart, searchEnd)) {
            gaps.add(searchInterval);
            return gaps;
        }

        // create a sub-list that excludes interval which does not overlap with
        // searchInterval
        List<Interval> subExistingList = removeNoneOverlappingIntervals(existingIntervals, searchInterval);
        DateTime subEarliestStart = subExistingList.get(0).getStart();
        DateTime subLatestStop = subExistingList.get(subExistingList.size() - 1).getEnd();

        // in case the searchInterval is wider than the union of the existing
        // include searchInterval.start => earliestExisting.start
        if (searchStart.isBefore(subEarliestStart)) {
            gaps.add(new Interval(searchStart, subEarliestStart));
        }

        // get all the gaps in the existing list
        gaps.addAll(getExistingIntervalGaps(subExistingList));

        // include latestExisting.stop => searchInterval.stop
        if (searchEnd.isAfter(subLatestStop)) {
            gaps.add(new Interval(subLatestStop, searchEnd));
        }
        return gaps;
    }

    private List<Interval> getExistingIntervalGaps(List<Interval> existingList) {
        List<Interval> gaps = new ArrayList<>();
        Interval current = existingList.get(0);
        for (int i = 1; i < existingList.size(); i++) {
            Interval next = existingList.get(i);
            Interval gap = current.gap(next);
            if (gap != null) {
                gaps.add(gap);
            }
            current = next;
        }
        return gaps;
    }

    private List<Interval> removeNoneOverlappingIntervals(List<Interval> existingIntervals, Interval searchInterval) {
        List<Interval> subExistingList = new ArrayList<>();
        existingIntervals.stream().filter((interval) -> (interval.overlaps(searchInterval))).forEachOrdered((interval) -> {
            subExistingList.add(interval);
        });
        return subExistingList;
    }

    private boolean hasNoOverlap(List<Interval> existingIntervals, Interval searchInterval, DateTime searchStart, DateTime searchEnd) {
        DateTime earliestStart = existingIntervals.get(0).getStart();
        DateTime latestStop = existingIntervals.get(existingIntervals.size() - 1).getEnd();
        // return the entire search interval if it does not overlap with
        // existing at all
        return searchEnd.isBefore(earliestStart) || searchStart.isAfter(latestStop);
    }

    //https://www.geeksforgeeks.org/merging-intervals/
    public List<Interval> mergeIntervals(List<Interval> intervals) {
        // Test if the given set has at least one interval  
        if (intervals.isEmpty()) {
            return null;
        }

        // Create an empty stack of intervals  
        Stack<Interval> stack = new Stack<>();

        // sort the intervals in increasing order of start time  
        Collections.sort(intervals, (Interval i1, Interval i2) -> i1.getStart().compareTo(i2.getStart()));

        // push the first interval to stack  
        stack.push(intervals.get(0));

        // Start from the next interval and merge if necessary  
        for (int i = 1; i < intervals.size(); i++) {
            // get interval from stack top  
            Interval top = stack.peek();
            Interval element = intervals.get(i);

            // if current interval is not overlapping with stack top,  
            // push it to the stack  
            if (top.getEnd().isBefore(element.getStart())) {
                stack.push(element);
            } // Otherwise update the ending time of top if ending of current  
            // interval is more  
            else if (top.getEnd().isBefore(element.getEnd())) {
                top = new Interval(top.getStart(), element.getEnd());
                stack.pop();
                stack.push(top);
            }
        }

        // Print contents of stack  
//        System.out.println("The Merged Intervals are: ");
//        while (!stack.isEmpty()) {
//            Interval t = stack.pop();
//            System.out.println("[" + t.getStart() + " --- " + t.getEnd() + "] ");
//        }
        return new ArrayList(stack);
    }
}
