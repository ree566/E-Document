/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.customValidator;

import com.advantech.model.Flow;
import com.advantech.model.PreAssy;
import com.advantech.model.Worktime;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Wei.Cheng unfinish
 */
public class FlowValidator implements ConstraintValidator<FlowValidate, Worktime> {

    private final String WHEN_NOT_EMPTY_AND_NULL = "有值且不為0時";
    private final String NOT_NULL_OR_ZERO_MESSAGE = "需有值，不可為0";
    private final String MUST_CONTAIN_MESSAGE = "內容須為";
    private final String BOTH_SIDE_CORRECT = "";
    private final String FLOW_ERROR = "flow";
    private final String TARGET_FIELD_ERROR = "target";

    private Map<String, String> errorMessages;

    //This only init one time when object first validate.
    @Override
    public void initialize(FlowValidate a) {
        errorMessages = new HashMap();
    }

    //How to gather all error message.
    @Override
    public boolean isValid(Worktime w, ConstraintValidatorContext cvc) {
        this.errorMessages.clear();
        
        if (w == null) {
            return true;
        }

        //Check preAssy
        check("preAssy", w.getPreAssy(), "cleanPanel", w.getCleanPanel(), "PRE_ASSY", NOT_NULL_OR_ZERO_MESSAGE);

        //Check babFlow
        Flow babFlow = w.getFlowByBabFlowId();
        String[] biRi = {"BI", "RI"};
        check("babFlow", babFlow, "assy", w.getAssy(), "ASSY", NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "t1", w.getT1(), "T1", NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "vibration", w.getVibration(), "VB", NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "hiPotLeakage", w.getHiPotLeakage(), "H1", NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "coldBoot", w.getColdBoot(), "CB", NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "upBiRi", w.getUpBiRi(), biRi, NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "downBiRi", w.getDownBiRi(), biRi, NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "biCost", w.getBiCost(), biRi, NOT_NULL_OR_ZERO_MESSAGE);
        check("babFlow", babFlow, "burnIn", w.getBurnIn(), biRi, MUST_CONTAIN_MESSAGE + Arrays.toString(biRi));

        //Check testFlow
        Flow testFlow = w.getFlowByTestFlowId();
        check("testFlow", testFlow, "t2", w.getT2(), "T2", NOT_NULL_OR_ZERO_MESSAGE);
        check("testFlow", testFlow, "t3", w.getT3(), "T3", NOT_NULL_OR_ZERO_MESSAGE);
        check("testFlow", testFlow, "t4", w.getT4(), "T4", NOT_NULL_OR_ZERO_MESSAGE);

        //Check packing flow
        Flow pkgFlow = w.getFlowByPackingFlowId();
        check("packingFlow", pkgFlow, "packing", w.getPacking(), "PKG", NOT_NULL_OR_ZERO_MESSAGE);

        if (!errorMessages.isEmpty()) {
            this.addInConstraintValidatorContext(cvc);
            return false;
        }
        return true;
    }

    public void check(String flowName, Object flow, String targetName, Object targetValue, String key, String errorMessage) {
        String[] keys = {key};
        this.check(flowName, flow, targetName, targetValue, keys, errorMessage);
    }

    public void check(String flowName, Object flow, String targetName, Object targetValue, String[] keys, String errorMessage) {
        Object[] result = isFlowValid(flow, keys, targetValue);
        if (((boolean) result[1]) == false) {
            String errorField = (String) result[0];
            if (errorField.equals(FLOW_ERROR)) {
                this.addError(flowName, targetName + WHEN_NOT_EMPTY_AND_NULL + "," + flowName + " must contain " + Arrays.toString(keys));
            } else {
                this.addError(targetName, targetName + errorMessage + " because " + flowName);
            }
        }
    }

    private void addError(String field, String message) {
        this.errorMessages.put(field, message);
    }

    public void addInConstraintValidatorContext(ConstraintValidatorContext cvc) {
        cvc.disableDefaultConstraintViolation();
        for (String key : errorMessages.keySet()) {
            cvc.buildConstraintViolationWithTemplate(errorMessages.get(key))
                    .addPropertyNode(key)
                    .addConstraintViolation();
        }
    }

    private boolean isNullOrZero(BigDecimal d) {
        return d == null || BigDecimal.ZERO.compareTo(d) == 0;
    }

    private boolean isNullOrNotEqual(String v, String st) {
        return st == null || !v.equals(st);
    }

    private Object[] isFlowValid(Object flow, String[] keys, Object target) {
        if (flow == null && (target == null || BigDecimal.ZERO.compareTo((BigDecimal) target) == 0)) {
            Object[] result = {BOTH_SIDE_CORRECT, true};
            return result;
        }

        if (flow == null && target != null) {
            Object[] result = {FLOW_ERROR, false};
            return result;
        } else if (flow != null && target == null) {
            Object[] result = {TARGET_FIELD_ERROR, false};
            return result;
        }

        String flowName = "";
        if (flow instanceof Flow) {
            flowName = ((Flow) flow).getName();
        } else if (flow instanceof PreAssy) {
            flowName = ((PreAssy) flow).getName();
        }

        for (String key : keys) {
            if (flowName.contains(key)) {
                if (target == null
                        || (target instanceof BigDecimal && isNullOrZero((BigDecimal) target))
                        || (target instanceof String && isNullOrNotEqual(key, (String) target))) {
                    Object[] result = {TARGET_FIELD_ERROR, false};
                    return result;
                }
            }
        }

        Object[] result = {BOTH_SIDE_CORRECT, true};
        return result;
    }

    /*
        PRE_ASSY | cleanPanel | not_null_and_zero_message
        ASSY | assy | not_null_and_zero_message
        T1 | t1 | not_null_and_zero_message
        VB | vibration | not_null_and_zero_message
        "H1", " LK" | hiPotLeakage | not_null_and_zero_message
        CB | coldBoot | not_null_and_zero_message
        "BI", "RI" | "upBiRi", "downBiRi", "biCost" | not_null_and_zero_message
        BI | burnIn | 內容須為BI
        RI | burnIn | 內容須為RI
        T2 | t2 | not_null_and_zero_message
        T3 | t3 | not_null_and_zero_message
        T4 | t4 | not_null_and_zero_message
        PKG | packing | not_null_and_zero_message
     */
}
