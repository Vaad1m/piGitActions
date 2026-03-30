package org.example;

public record Lexeme(LexemeType type, double value) {
    public Lexeme(LexemeType type, double value) {
        this.type = type;
        // 0 PLUS
        // 1 MINUS
        // 2 MUL
        // 3 DIV
        // 4 UNARY MINUS
        // 0 OPEN BRACKET
        // 1 CLOSE BRACKET
        this.value = value;
    }

    public boolean isStart() {
        return type == LexemeType.START;
    }

    public boolean isEnd() {
        return type == LexemeType.END;
    }

    public boolean isDigit() {
        return type == LexemeType.DIGIT;
    }

    public boolean isOperation() {
        return type == LexemeType.OPERATION;
    }

    public boolean isOpenBracket() {
        return type == LexemeType.BRACKET && value == 0;
    }

    public boolean isCloseBracket() {
        return type == LexemeType.BRACKET && value == 1;
    }

    public boolean isUnaryMinus() {
        return type == LexemeType.OPERATION && value == 4;
    }

}
