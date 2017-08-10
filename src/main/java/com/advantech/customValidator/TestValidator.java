/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.customValidator;

import com.advantech.model.BusinessGroup;
import com.advantech.model.Worktime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Wei.Cheng
 */
public class TestValidator implements ConstraintValidator<TestValidate, Worktime> {

    @Override
    public void initialize(TestValidate a) {

    }

    @Override
    public boolean isValid(Worktime w, ConstraintValidatorContext cvc) {
        if (w == null) {
            return true;
        }

        cvc.disableDefaultConstraintViolation();
        
        this.setMessage(cvc, "a1", "a1");
        this.setMessage(cvc, "a2", "a2");
        this.setMessage(cvc, "a3", "a3");
        this.setMessage(cvc, "a4", "a4");

        return false;
    }

    private void setMessage(ConstraintValidatorContext cvc, String fieldName, String errorMessage) {
        cvc
                .buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(fieldName).addConstraintViolation();
    }

}
