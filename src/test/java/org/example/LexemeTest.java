package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import static org.assertj.core.api.Assertions.*;

public class LexemeTest {

    @Nested
    class TypeTests {
        @Test
        void isStartTest() {
            Lexeme lexeme = new Lexeme(LexemeType.START, 0);
            assertThat(lexeme.isStart()).isTrue();
            assertThat(lexeme.isEnd()).isFalse();
            assertThat(lexeme.isDigit()).isFalse();
            assertThat(lexeme.isOperation()).isFalse();
            assertThat(lexeme.isOpenBracket()).isFalse();
            assertThat(lexeme.isCloseBracket()).isFalse();
            assertThat(lexeme.isUnaryMinus()).isFalse();
        }

        @Test
        void isEndTest() {
            Lexeme lexeme = new Lexeme(LexemeType.END, 0);
            assertThat(lexeme.isEnd()).isTrue();
            assertThat(lexeme.isStart()).isFalse();
            assertThat(lexeme.isDigit()).isFalse();
            assertThat(lexeme.isOperation()).isFalse();
        }

        @Test
        void isDigitTest() {
            Lexeme lexeme = new Lexeme(LexemeType.DIGIT, 42.5);
            assertThat(lexeme.isDigit()).isTrue();
            assertThat(lexeme.value()).isEqualTo(42.5);
            assertThat(lexeme.isOperation()).isFalse();
            assertThat(lexeme.isStart()).isFalse();
        }

        @Test
        void isOperationTest() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 0);
            assertThat(lexeme.isOperation()).isTrue();
            assertThat(lexeme.isDigit()).isFalse();
        }

        @Test
        void isOpenBracketTest() {
            Lexeme lexeme = new Lexeme(LexemeType.BRACKET, 0);
            assertThat(lexeme.isOpenBracket()).isTrue();
            assertThat(lexeme.isCloseBracket()).isFalse();
        }

        @Test
        void isCloseBracketTest() {
            Lexeme lexeme = new Lexeme(LexemeType.BRACKET, 1);
            assertThat(lexeme.isCloseBracket()).isTrue();
            assertThat(lexeme.isOpenBracket()).isFalse();
        }

        @Test
        void isUnaryMinusTest() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 4);
            assertThat(lexeme.isUnaryMinus()).isTrue();
            assertThat(lexeme.isOperation()).isTrue();
        }

        @Test
        void isNotUnaryMinusTest() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 1);
            assertThat(lexeme.isUnaryMinus()).isFalse();
            assertThat(lexeme.isOperation()).isTrue();
        }
    }

    @Nested
    class OperationValueTests {
        @Test
        void plusOperationHasValueZero() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 0);
            assertThat(lexeme.value()).isEqualTo(0);
        }

        @Test
        void minusOperationHasValueOne() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 1);
            assertThat(lexeme.value()).isEqualTo(1);
        }

        @Test
        void multiplyOperationHasValueTwo() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 2);
            assertThat(lexeme.value()).isEqualTo(2);
        }

        @Test
        void divideOperationHasValueThree() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 3);
            assertThat(lexeme.value()).isEqualTo(3);
        }

        @Test
        void unaryMinusOperationHasValueFour() {
            Lexeme lexeme = new Lexeme(LexemeType.OPERATION, 4);
            assertThat(lexeme.value()).isEqualTo(4);
        }
    }
}