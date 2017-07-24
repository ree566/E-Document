/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.customValidator;

import com.advantech.model.Flow;
import com.advantech.model.Worktime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Wei.Cheng
 * unfinish
 */
public class FlowValidator implements ConstraintValidator<FlowValidate, Worktime> {

    @Override
    public void initialize(FlowValidate a) {

    }

    @Override
    public boolean isValid(Worktime w, ConstraintValidatorContext cvc) {
        if (w == null) {
            return true;
        }

        Flow babFlow = w.getFlowByBabFlowId();

        cvc.disableDefaultConstraintViolation();
        cvc.buildConstraintViolationWithTemplate("{my.custom.template}")
                .addPropertyNode("passengers").addConstraintViolation();

        return false;
    }

}
