package com.antiri.poetry.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class CFGGenerator {

    private static String expression = "";
    private static java.util.List<Production> productionList = new ArrayList<Production>();

    public static void main(String[] args) throws IOException {

        /*
         * Read input file, generate a production, one for each line
         *   A production is a map, the key is the non-terminal id and the key is a list of expressions, which
         *   could in turn be other non-terminals, or also could be commands.
         *
         * The root non-terminal is POEM (hard code that), so start with that and substitute it by looking
         * in the map for that key, and selecting at random an expression.
         *
         * Split up the expression by whitespace and put each group into a list.
         *   Ex: This is a <ADJECTIVE> one $END Can't get here
         *   This breaks down into {This is a }, <ADJECTIVE>, { one }, $END, " Can't get here"
         *   Notice the spaces are included
         *
         * Iterate ove this list.
         * If it contains something in angle brackets, it's an expression. Go out and fetch at random another one from the production map
         * If it starts with a $ symbol, it's a command. Execute the command immediatly.
         * Else, it's terminal, substitute it.
         *
         * Example:
         * POEM: <LINE> <LINE> <LINE> $END
         * LINE: <VERB> you <ADJECTIVE> <NOUN> $LINEBREAK
         * VERB: jump|swim|run
         * ADJECTIVE: silly|foolish|<NOUN> loving
         * NOUN: apple-jack|mongoose|hacker
         *
         * POEM
         * <LINE> <LINE> <LINE>
         * <VERB> you <ADJECTIVE> <NOUN> \n <LINE> <LINE>
         * run you <ADJECTIVE> <NOUN> \n <LINE> <LINE>
         * run you <NOUN> loving <NOUN> \n <LINE> <LINE>
         * run you apple-jack loving <NOUN> \n <LINE> <LINE>
         * run you apple-jack loving mongoose \n <LINE> <LINE>
         *     etc etc etc
         *
         *
         */

        Map<String, List<String>> productions = new HashMap<String, List<String>>();

        File cfgFile = new File("poem.txt");
        Scanner fileScanner = new Scanner(cfgFile);

        while (fileScanner.hasNext()) {
            String production = fileScanner.nextLine();
            List<String> expressions = new ArrayList<String>();
            String nonTerminal = production.split(": ")[0];
            expressions.addAll(Arrays.asList(production.split(": ")[1].split("\\|")));
            productions.put(nonTerminal, expressions);
        }

        System.out.println(productions);
    }

    /**
     * Replace all instances of $LINEBREAK with a newline
     * Working from left to right, identify a non-terminal, look up a random expression and replace it.
     *
     * Problem here
     * @throws FileNotFoundException
     */
    public static void initialAttempt() throws FileNotFoundException {
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