package com.antiri.poetry.core;

import java.util.ArrayList;
import java.util.List;

public class Production {
    private String nonTerminal;
    private List<String> expressions;

    Production(String production) {
        nonTerminal = production.split(": ")[0];

        expressions = new ArrayList<String>();
        for (String expression : production.split(": ", -1)[1].split("\\|")) {
            expressions.add(expression);
        }

    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public List<String> getExpressions() {
        return expressions;
    }

}
