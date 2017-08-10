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
public class EsValidator implements ConstraintValidator<EsValidate, Worktime> {

    @Override
    public void initialize(EsValidate a) {

    }

    @Override
    public boolean isValid(Worktime w, ConstraintValidatorContext cvc) {
        if (w == null) {
            return true;
        }

        String modelName = w.getModelName();
        BusinessGroup bg = w.getBusinessGroup();

        //leave null-checking to @NotNull on individual parameters
        if (modelName == null || bg == null) {
            return true;
        }

        boolean isEsModelName = modelName.endsWith("-ES");
        boolean isEsBusinessGroup = bg.getName().equals("ES");
        if (isEsModelName && !isEsBusinessGroup) {
            this.setMessage(cvc, "businessGroup", "Must contain \"ES\"");
            return false;
        } else if (!isEsModelName && isEsBusinessGroup) {
            this.setMessage(cvc, "modelName", "Must endWith \"ES\"");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext cvc, String fieldName, String errorMessage) {
        cvc.disableDefaultConstraintViolation();
        cvc
                .buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(fieldName).addConstraintViolation();
    }

}
