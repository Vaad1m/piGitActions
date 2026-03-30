package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.*;

public class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    class SimpleOperations {
        @Test
        void calculateAddition() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 5));
            rpm.add(new Lexeme(LexemeType.DIGIT, 3));
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(8.0);
        }

        @Test
        void calculateSubtraction() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 10));
            rpm.add(new Lexeme(LexemeType.DIGIT, 4));
            rpm.add(new Lexeme(LexemeType.OPERATION, 1)); // -

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(6.0);
        }

        @Test
        void calculateMultiplication() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 7));
            rpm.add(new Lexeme(LexemeType.DIGIT, 6));
            rpm.add(new Lexeme(LexemeType.OPERATION, 2)); // *

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(42.0);
        }

        @Test
        void calculateDivision() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 15));
            rpm.add(new Lexeme(LexemeType.DIGIT, 3));
            rpm.add(new Lexeme(LexemeType.OPERATION, 3)); // /

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(5.0);
        }

        @Test
        void calculateDivisionWithDoubleResult() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 10));
            rpm.add(new Lexeme(LexemeType.DIGIT, 4));
            rpm.add(new Lexeme(LexemeType.OPERATION, 3)); // /

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(2.5);
        }
    }

    @Nested
    class ComplexExpressions {
        @Test
        void calculateMultipleOperations() {
            // 5 + 3 * 2 = 5 + 6 = 11
            // RPN: 5 3 2 * +
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 5));
            rpm.add(new Lexeme(LexemeType.DIGIT, 3));
            rpm.add(new Lexeme(LexemeType.DIGIT, 2));
            rpm.add(new Lexeme(LexemeType.OPERATION, 2)); // *
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(11.0);
        }

        @Test
        void calculateWithUnaryMinus() {
            // -5 + 3
            // RPN: 5 - 3 +
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 5));
            rpm.add(new Lexeme(LexemeType.OPERATION, 4)); // унарный минус
            rpm.add(new Lexeme(LexemeType.DIGIT, 3));
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(-2.0);
        }

        @Test
        void calculateComplexWithUnaryMinus() {
            // -5 * (3 + 2)
            // RPN: 5 - 3 2 + *
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 5));
            rpm.add(new Lexeme(LexemeType.OPERATION, 4)); // унарный минус
            rpm.add(new Lexeme(LexemeType.DIGIT, 3));
            rpm.add(new Lexeme(LexemeType.DIGIT, 2));
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +
            rpm.add(new Lexeme(LexemeType.OPERATION, 2)); // *

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(-25.0);
        }
    }

    @Nested
    class EdgeCases {
        @Test
        void calculateSingleNumber() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 42));

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(42.0);
        }

        @Test
        void calculateWithZero() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 0));
            rpm.add(new Lexeme(LexemeType.DIGIT, 5));
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(5.0);
        }

        @Test
        void calculateDivisionByZero() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 10));
            rpm.add(new Lexeme(LexemeType.DIGIT, 0));
            rpm.add(new Lexeme(LexemeType.OPERATION, 3)); // /

            // Деление на ноль даст Infinity в Java
            double result = calculator.calculate(rpm);
            assertThat(result).isInfinite();
        }

        @Test
        void calculateWithNegativeNumbers() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, -5));
            rpm.add(new Lexeme(LexemeType.DIGIT, 3));
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(-2.0);
        }

        @Test
        void calculateWithDecimalNumbers() {
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 2.5));
            rpm.add(new Lexeme(LexemeType.DIGIT, 1.5));
            rpm.add(new Lexeme(LexemeType.OPERATION, 0)); // +

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(4.0);
        }
    }

    @Nested
    class OperationOrder {
        @Test
        void calculateSubtractionOrderMatters() {
            // 10 - 5 = 5, не 5 - 10 = -5
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 10));
            rpm.add(new Lexeme(LexemeType.DIGIT, 5));
            rpm.add(new Lexeme(LexemeType.OPERATION, 1)); // -

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(5.0);
        }

        @Test
        void calculateDivisionOrderMatters() {
            // 20 / 4 = 5, не 4 / 20 = 0.2
            ArrayList<Lexeme> rpm = new ArrayList<>();
            rpm.add(new Lexeme(LexemeType.DIGIT, 20));
            rpm.add(new Lexeme(LexemeType.DIGIT, 4));
            rpm.add(new Lexeme(LexemeType.OPERATION, 3)); // /

            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(5.0);
        }
    }
}