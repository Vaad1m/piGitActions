package org.example;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Main {
    @SuppressWarnings("PMD.CloseResource")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        String inputString = scanner.nextLine();
        scanner.close();

        RpmParser parser = new RpmParser();
        try {
            List<Lexeme> rpm = parser.parse(inputString);
            printRpm(rpm);
            Calculator calculator = new Calculator();
            System.out.println("Result: " + calculator.calculate(rpm));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printRpm(List<Lexeme> lexemes) {
        System.out.println("Reverse Polish notation:");
        for (Lexeme lexeme : lexemes) {
            if (lexeme.isDigit()) {
                System.out.print(lexeme.value() + " ");
            } else if (lexeme.isOpenBracket()) {
                System.out.print("(");
            } else if (lexeme.isCloseBracket()) {
                System.out.print(")");
            } else if (lexeme.isUnaryMinus()) {
                System.out.print("-");
            } else if (lexeme.isOperation() && lexeme.value() == 0) {
                System.out.print("+");
            } else if (lexeme.isOperation() && lexeme.value() == 1) {
                System.out.print("-");
            } else if (lexeme.isOperation() && lexeme.value() == 2) {
                System.out.print("*");
            } else if (lexeme.isOperation() && lexeme.value() == 3) {
                System.out.print("/");
            }
        }
        System.out.println();
    }
}

