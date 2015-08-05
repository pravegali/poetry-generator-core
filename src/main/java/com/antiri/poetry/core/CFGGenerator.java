package com.antiri.poetry.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class CFGGenerator {

    private static String expression = "";
    private static java.util.List<Production> productionList = new ArrayList<Production>();

    public static void main(String[] args) throws IOException {

        String result = "<POEM>";
        File cfgFile = new File("poem.txt");
        Scanner fileScanner = new Scanner(cfgFile);
        Random random = new Random();

        while (fileScanner.hasNext()) {
            Production p = new Production(fileScanner.nextLine());
            productionList.add(p);
        }

        while (result.contains("<")) {
            String nonTerminal = result.substring(result.indexOf('<') + 1, result.indexOf('>'));

            result = result.replaceAll("\\s*\\$LINEBREAK\\s*", System.lineSeparator());

            for (int i = 0; i <= productionList.size() - 1; i++) {
                Production production = productionList.get(i);
                if (nonTerminal.equalsIgnoreCase(production.getNonTerminal())) {
                    int randomExpressionIndex = random.nextInt(production.getExpressions().size());
                    expression = production.getExpressions().get(randomExpressionIndex);
                }
            }
            result = result.replaceFirst("<" + nonTerminal + ">", expression);
        }
        System.out.println(result);
    }

}