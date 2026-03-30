package org.example;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RpmParser {
    private List<Lexeme> rpm;
    private Deque<Lexeme> operations;
    private String string;
    private int indexString;
    private Lexeme lastLexeme;

    public RpmParser() {
        this.rpm = new ArrayList<>();
        this.operations = new ArrayDeque<>();
        this.string = "";
        this.indexString = 0;
        this.lastLexeme = new Lexeme(LexemeType.START, 0);
    }

    private void resetParser(String string) {
        rpm = new ArrayList<>();
        operations = new ArrayDeque<>();
        this.string = string;
        indexString = 0;
        lastLexeme = new Lexeme(LexemeType.START, 0);
    }

    public List<Lexeme> parse(String inputString) {
        resetParser(inputString);
        Lexeme lexeme = new Lexeme(LexemeType.DIGIT, 0);

        while (!lexeme.isEnd()) {
            lexeme = getLexeme();
            if (lexeme == null || !checkSyntax(lexeme)) {
                throw new IllegalArgumentException("Incorrect mathematical expression!");
            }

            if (lexeme.isDigit()) {
                rpm.add(lexeme);
            } else if (lexeme.isOpenBracket()) {
                operations.push(lexeme);
            } else if (lexeme.isCloseBracket() && !addToBracket()) {
                throw new IllegalArgumentException("Incorrect mathematical expression!");
            } else if (lexeme.isOperation()) {
                addToPriority(lexeme);
            } else if (lexeme.isEnd() && !addAll()) {
                throw new IllegalArgumentException("Incorrect mathematical expression!");
            }
            lastLexeme = lexeme;
        }
        return rpm;
    }

    private Lexeme getLexeme() {
        Lexeme lexeme = null;
        if (indexString == string.length()) {
            lexeme = new Lexeme(LexemeType.END, 0);
        } else if (isOperator(string.charAt(indexString))) {
            lexeme = toOperator(string.charAt(indexString));
            indexString++;
        } else if (isDigit(string.charAt(indexString))) {
            lexeme = accumulateNumber();
        }
        return lexeme;
    }

    private boolean isOperator(char c) {
        return "+-*/()".indexOf(c) != -1;
    }

    private boolean isDigit(char c) {
        return "0123456789".indexOf(c) != -1;
    }

    private Lexeme toOperator(char c) {
        Lexeme lexeme = null;
        if (c == '+') {
            lexeme = new Lexeme(LexemeType.OPERATION, 0);
        }
        if (c == '*') {
            lexeme = new Lexeme(LexemeType.OPERATION, 2);
        }
        if (c == '/') {
            lexeme = new Lexeme(LexemeType.OPERATION, 3);
        }
        if (c == '(') {
            lexeme = new Lexeme(LexemeType.BRACKET, 0);
        }
        if (c == ')') {
            lexeme = new Lexeme(LexemeType.BRACKET, 1);
        }
        if (c == '-' && (lastLexeme.isStart() || lastLexeme.isOpenBracket())) {
            lexeme = new Lexeme(LexemeType.OPERATION, 4);
        } else if (c == '-') {
            lexeme = new Lexeme(LexemeType.OPERATION, 1);
        }
        return lexeme;
    }

    private Lexeme accumulateNumber() {
        StringBuilder sb = new StringBuilder();
        while (indexString != string.length() && isDigit(string.charAt(indexString))) {
            sb.append(string.charAt(indexString));
            indexString++;
        }
        if (indexString != string.length() && string.charAt(indexString) == '.') {
            sb.append('.');
            indexString++;
            if (!isDigit(string.charAt(indexString))) {
                return null;
            }
            while (indexString != string.length() && isDigit(string.charAt(indexString))) {
                sb.append(string.charAt(indexString));
                indexString++;
            }
        }
        return new Lexeme(LexemeType.DIGIT, Double.parseDouble(sb.toString()));
    }

    private boolean checkSyntax(Lexeme currLexeme) {
        boolean status = true;
        if (currLexeme.isDigit() && lastLexeme.isCloseBracket()) {
            status = false;
        } else if (currLexeme.isOpenBracket() && (lastLexeme.isCloseBracket() || lastLexeme.isDigit())) {
            status = false;
        } else if (currLexeme.isCloseBracket() && (lastLexeme.isOpenBracket() || lastLexeme.isStart())) {
            status = false;
        } else if (currLexeme.isUnaryMinus() && (lastLexeme.isOperation() || lastLexeme.isCloseBracket())) {
            status = false;
        } else if (!currLexeme.isUnaryMinus() && currLexeme.isOperation() &&
                (lastLexeme.isOperation() || lastLexeme.isOpenBracket() || lastLexeme.isStart())) {
            status = false;
        } else if (currLexeme.isEnd() && (lastLexeme.isOperation() || lastLexeme.isOpenBracket())) {
            status = false;
        }
        return status;
    }

    private boolean addToBracket() {
        while (!operations.isEmpty()) {
            Lexeme lexeme = operations.pop();
            if (lexeme.isOpenBracket()) {
                return true;
            } else {
                rpm.add(lexeme);
            }
        }
        return false;
    }

    private void addToPriority(Lexeme lexeme) {
        while (true) {
            if (!operations.isEmpty() && operations.peek().isUnaryMinus()) {
                rpm.add(operations.pop());
            } else if (!operations.isEmpty() && isHigherPriority(lexeme, operations.peek())) {
                rpm.add(operations.pop());
            } else {
                operations.push(lexeme);
                break;
            }
        }
    }

    private boolean addAll() {
        while (!operations.isEmpty()) {
            Lexeme lexeme = operations.pop();
            if (lexeme.isOpenBracket()) {
                return false;
            }
            rpm.add(lexeme);
        }
        return true;
    }

    private boolean isHigherPriority(Lexeme op1, Lexeme op2) {
        return getPriority(op1) <= getPriority(op2);
    }

    private int getPriority(Lexeme op) {
        if (op.isUnaryMinus()) {
            return 3;
        } else if (op.isOperation() && (op.value() == 2 || op.value() == 3)) {
            return 2;
        } else if (op.isOperation() && (op.value() == 1 || op.value() == 0)) {
            return 1;
        } else
            return 0;
    }
}
