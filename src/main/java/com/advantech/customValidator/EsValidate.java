/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.customValidator;

import java.lang.annotation.Documented;  
import java.lang.annotation.Retention;  
import java.lang.annotation.Target;  
import javax.validation.Constraint;  
import javax.validation.Payload;  

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;  
import static java.lang.annotation.ElementType.TYPE;  
import static java.lang.annotation.RetentionPolicy.RUNTIME;  

/**
 *
 * @author Wei.Cheng
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { EsValidator.class })
@Documented
public @interface EsValidate {

    String message() default "Must contain \"ES\"";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}