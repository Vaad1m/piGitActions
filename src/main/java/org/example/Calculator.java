package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Calculator {
    private Deque<Double> stack;

    public Calculator() {
        this.stack = new ArrayDeque<>();
    }
    public double calculate(List<Lexeme> rpm) {
        stack = new ArrayDeque<>();
        for (Lexeme lexeme: rpm) {
            if (lexeme.isDigit()) {
                stack.push(lexeme.value());
            } else if (lexeme.isUnaryMinus()) {
                stack.push(stack.pop() * -1);
            } else if (lexeme.isOperation()) {
                double a = stack.pop();
                double b = stack.pop();
                if (lexeme.value() == 0) {
                    stack.push(b + a);
                } else if (lexeme.value() == 1) {
                    stack.push(b - a);
                } else if (lexeme.value() == 2) {
                    stack.push(b - a);
                } else if (lexeme.value() == 3) {
                    stack.push(b / a);
                }
            }
        }
        return stack.pop();
    }
}
