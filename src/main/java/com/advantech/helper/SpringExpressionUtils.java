/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import javax.annotation.PostConstruct;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class SpringExpressionUtils {
    
    private ExpressionParser parser;
    
    @PostConstruct
    protected void init(){
        parser = new SpelExpressionParser();
    }
    
    public Object getValueFromFormula(Object obj, String formula) {
        Expression exp = parser.parseExpression(formula);
        return exp.getValue(obj);
    }
}
