package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class RpmParserTest {
    private RpmParser parser;

    @BeforeEach
    void setUp() {
        parser = new RpmParser();
    }

    @Nested
    class SimpleExpressions {
        @Test
        void parseSimpleAddition() {
            List<Lexeme> result = parser.parse("3+5");
            assertThat(result).isNotNull();

            // RPN: 3 5 +
            assertThat(result).hasSize(3);
            assertThat(result.get(0).isDigit()).isTrue();
            assertThat(result.get(0).value()).isEqualTo(3);
            assertThat(result.get(1).isDigit()).isTrue();
            assertThat(result.get(1).value()).isEqualTo(5);
            assertThat(result.get(2).isOperation()).isTrue();
            assertThat(result.get(2).value()).isEqualTo(0); // +
        }

        @Test
        void parseSimpleSubtraction() {
            List<Lexeme> result = parser.parse("10-4");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(3);
            assertThat(result.get(2).value()).isEqualTo(1); // -
        }

        @Test
        void parseSimpleMultiplication() {
            List<Lexeme> result = parser.parse("7*6");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(3);
            assertThat(result.get(2).value()).isEqualTo(2); // *
        }

        @Test
        void parseSimpleDivision() {
            List<Lexeme> result = parser.parse("20/5");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(3);
            assertThat(result.get(2).value()).isEqualTo(3); // /
        }

        @Test
        void parseDecimalNumber() {
            List<Lexeme> result = parser.parse("3.5+2.5");
            assertThat(result).isNotNull();

            assertThat(result.get(0).value()).isEqualTo(3.5);
            assertThat(result.get(1).value()).isEqualTo(2.5);
        }
    }

    @Nested
    class OperatorPrecedence {
        @Test
        void parseMultiplicationBeforeAddition() {
            // 3 + 5 * 2 -> RPN: 3 5 2 * +
            List<Lexeme> result = parser.parse("3+5*2");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(5);
            assertThat(result.get(0).value()).isEqualTo(3);
            assertThat(result.get(1).value()).isEqualTo(5);
            assertThat(result.get(2).value()).isEqualTo(2);
            assertThat(result.get(3).value()).isEqualTo(2); // *
            assertThat(result.get(4).value()).isEqualTo(0); // +
        }

        @Test
        void parseDivisionBeforeSubtraction() {
            // 10 - 8 / 2 -> RPN: 10 8 2 / -
            List<Lexeme> result = parser.parse("10-8/2");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(5);
            assertThat(result.get(3).value()).isEqualTo(3); // /
            assertThat(result.get(4).value()).isEqualTo(1); // -
        }

        @Test
        void parseMultipleOperationsSamePrecedence() {
            // 3 + 5 - 2 -> RPN: 3 5 + 2 -
            List<Lexeme> result = parser.parse("3+5-2");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(5);
            assertThat(result.get(2).value()).isEqualTo(0); // +
            assertThat(result.get(4).value()).isEqualTo(1); // -
        }
    }

    @Nested
    class Brackets {
        @Test
        void parseExpressionWithBrackets() {
            // (3 + 5) * 2 -> RPN: 3 5 + 2 *
            List<Lexeme> result = parser.parse("(3+5)*2");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(5);
            assertThat(result.get(0).value()).isEqualTo(3);
            assertThat(result.get(1).value()).isEqualTo(5);
            assertThat(result.get(2).value()).isEqualTo(0); // +
            assertThat(result.get(3).value()).isEqualTo(2);
            assertThat(result.get(4).value()).isEqualTo(2); // *
        }

        @Test
        void parseNestedBrackets() {
            // 2 * (3 + (4 - 1)) -> RPN: 2 3 4 1 - + *
            List<Lexeme> result = parser.parse("2*(3+(4-1))");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(7);
            assertThat(result.get(0).value()).isEqualTo(2);
            assertThat(result.get(1).value()).isEqualTo(3);
            assertThat(result.get(2).value()).isEqualTo(4);
            assertThat(result.get(3).value()).isEqualTo(1);
            assertThat(result.get(4).value()).isEqualTo(1); // -
            assertThat(result.get(5).value()).isEqualTo(0); // +
            assertThat(result.get(6).value()).isEqualTo(2); // *
        }

        @Test
        void parseExpressionWithMultipleBrackets() {
            // (3+5)*(2+4) -> RPN: 3 5 + 2 4 + *
            List<Lexeme> result = parser.parse("(3+5)*(2+4)");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(7);
            assertThat(result.get(2).value()).isEqualTo(0); // +
            assertThat(result.get(5).value()).isEqualTo(0); // +
            assertThat(result.get(6).value()).isEqualTo(2); // *
        }
    }

    @Nested
    class UnaryMinus {
        @Test
        void parseUnaryMinusAtBeginning() {
            // -5 + 3 -> RPN: 5 - 3 +
            List<Lexeme> result = parser.parse("-5+3");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(4);
            assertThat(result.get(0).value()).isEqualTo(5);
            assertThat(result.get(1).isUnaryMinus()).isTrue();
            assertThat(result.get(2).value()).isEqualTo(3);
            assertThat(result.get(3).value()).isEqualTo(0); // +
        }

        @Test
        void parseUnaryMinusAfterBracket() {
            // 5 * (-3) -> RPN: 5 3 - *
            List<Lexeme> result = parser.parse("5*(-3)");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(4);
            assertThat(result.get(0).value()).isEqualTo(5);
            assertThat(result.get(1).value()).isEqualTo(3);
            assertThat(result.get(2).isUnaryMinus()).isTrue();
            assertThat(result.get(3).value()).isEqualTo(2); // *
        }

        @Test
        void parseDoubleUnaryMinus() {
            // -(-5) -> RPN: 5 - -
            List<Lexeme> result = parser.parse("-(-5)");
            assertThat(result).isNotNull();

            assertThat(result).hasSize(3);
            assertThat(result.get(0).value()).isEqualTo(5);
            assertThat(result.get(1).isUnaryMinus()).isTrue();
            assertThat(result.get(2).isUnaryMinus()).isTrue();
        }
    }

    @Nested
    class InvalidExpressions {
        @Test
        void parseTwoOperatorsInRow() {
            assertThatThrownBy(() -> parser.parse("3++5")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void parseMissingOperand() {
            assertThatThrownBy(() -> parser.parse("3+")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void parseMismatchedBrackets() {
            assertThatThrownBy(() -> parser.parse("(3+5")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void parseEmptyExpression() {
            List<Lexeme> result = parser.parse("");
            assertThat(result).isEmpty();
        }

        @Test
        void parseInvalidCharacter() {
            assertThatThrownBy(() -> parser.parse("3+5a")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void parseNumberWithMultipleDots() {
            assertThatThrownBy(() -> parser.parse("3..5+2")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void parseOperatorAtBeginningWithoutUnary() {
            assertThatThrownBy(() -> parser.parse("*5")).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ComplexValidExpressions {
        @Test
        void parseComplexExpressionWithAllOperations() {
            // 3 + 5 * 2 - 8 / 4
            List<Lexeme> result = parser.parse("3+5*2-8/4");
            assertThat(result).isNotNull();
            assertThat(result).hasSize(9); // 3 5 2 * 8 4 / - +
        }

        @Test
        void parseExpressionWithUnaryAndBrackets() {
            // -(3+5) * 2
            List<Lexeme> result = parser.parse("-(3+5)*2");
            assertThat(result).isNotNull();
            assertThat(result).hasSize(6); // 3 5 + - 2 *
        }

        @Test
        void parseExpressionWithMultipleUnaryMinus() {
            // 5 * (-(-3 + 2))
            List<Lexeme> result = parser.parse("5*(-(-3+2))");
            assertThat(result).isNotNull();
        }
    }

    @Nested
    class IntegrationWithCalculator {
        @Test
        void parseAndCalculateSimple() {
            List<Lexeme> rpm = parser.parse("3+5");
            assertThat(rpm).isNotNull();

            Calculator calculator = new Calculator();
            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(8.0);
        }

        @Test
        void parseAndCalculateComplex() {
            List<Lexeme> rpm = parser.parse("(3+5)*2");
            assertThat(rpm).isNotNull();

            Calculator calculator = new Calculator();
            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(16.0);
        }

        @Test
        void parseAndCalculateWithUnaryMinus() {
            List<Lexeme> rpm = parser.parse("-5+3");
            assertThat(rpm).isNotNull();

            Calculator calculator = new Calculator();
            double result = calculator.calculate(rpm);
            assertThat(result).isEqualTo(-2.0);
        }
    }
}