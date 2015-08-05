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
    private static Random random = new Random();
    private static Map<String, List<List<String>>> productions = new HashMap<String, List<List<String>>>();
    private static Map<String, List<List<String>>> poem = new HashMap<String, List<List<String>>>();

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



        File cfgFile = new File("poem.txt");
        Scanner fileScanner = new Scanner(cfgFile);

        while (fileScanner.hasNext()) {
            String production = fileScanner.nextLine();
            List<List<String>> expressions = new ArrayList<List<String>>();
            List<String> components = new ArrayList<String>();

            // Fin the non-terminal identifier
            String nonTerminal = production.split(": ")[0];

            // Break up the line on spaces to get the components
            // Break up the components into expressions
            // Add the expressions into the components list
            // Add the components list to the expressions list
            components.addAll(Arrays.asList(production.split(": ")[1].split(" ")));
            for (String component : components) {
                List<String> expression2 = new ArrayList<String>();
                expression2.addAll(Arrays.asList(component.split("\\|")));
                expressions.add(expression2);
            }
            productions.put(nonTerminal, expressions);
        }

        // Find the POEM. For every token in there, find the right substitution. Check if it's a command or not.
        // Start to build out a string. When we hit the $END command, or run out of substitutions to make, end
        // the loop and return the string.

        String poem = "";
        process:
        {
            for (List<String> expressionList : productions.get("VERB")) {
                String element = selectRandomElement(expressionList);

                if (element.startsWith("$")) {
                    if (element.compareTo("$END") == 0) {
                        break process;
                    }
                    if (element.compareTo("$LINEBREAK") == 0) {
                        element = System.lineSeparator();
                    }
                } else if (element.startsWith("<")) {
                    String newElement = "";
                    for (String nonTerminal : findTerminal(element)) {
                        newElement += nonTerminal;
                    }
                    element = newElement;

                }
                poem += (" " + element);
            }
        }

        System.out.println(poem);
    }

    /**
     * Decide if a given element is or is not a terminal token
     * @param expression
     * @return
     */
    public static boolean isTerminal(String expression) {
        return !(expression.startsWith("$") || expression.startsWith("<"));
    }

    /**
     * Take in a token like "hello", "$LINEBREAK", "$END" or "<VERB>"
     * If it's terminal, just return it.
     * if it's non terminal, resolve it
     * @param nonTerminal
     * @return
     */
    public static List<String> findTerminal(String nonTerminal) {
        List<String> tokens = new ArrayList<String>();
        nonTerminal = nonTerminal.substring(nonTerminal.indexOf('<') + 1, nonTerminal.indexOf('>'));
        for (List<String> component : productions.get(nonTerminal)) {
            tokens.add(selectRandomElement(component));
        }
        return tokens;
    }
    /**
     * Randomly select a terminal, recursively
     * @return
     */
    public static String selectRandomElement(List<String> expressions) {
        return expressions.get(random.nextInt(expressions.size()));
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