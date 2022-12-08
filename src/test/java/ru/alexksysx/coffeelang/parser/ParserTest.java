package ru.alexksysx.coffeelang.parser;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.alexksysx.coffeelang.AnalyzeException;
import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenType;

import static org.testng.Assert.*;

public class ParserTest {
    private Parser parser;

    @BeforeClass
    public void init() {
        parser = new Parser();
    }

    @DataProvider(name = "failedParse")
    public Object[][] failedParse() {
        return new Object[][] {
                {"помол := ()", "Ошибка синтаксиса: ожидается число, время, идентификатор, степень помола или тип кружки для подачи", new Token(TokenType.LPAREN, '(')},
                {"помолоть_кофе(,, эспрессо)", "Ошибка синтаксиса, ожидалось число или идентификатор, обнаружилось: ,", new Token(TokenType.COMMA, ",")},
                {"помолоть_кофе(12, ,)", "Ошибка синтаксиса, ожидался идентификатор или степень помола кофе, обнаружилось: ,", new Token(TokenType.COMMA, ",")},
                {"выставить_давление(,)", "Ошибка синтаксиса, ожидался идентификатор или давление(число), обнаружилось: ,", new Token(TokenType.COMMA, ",")},
                {"выставить_температуру(()", "Ошибка синтаксиса, ожидался идентификатор или температура(число), обнаружилось: (", new Token(TokenType.LPAREN, "(")},
                {"взять_двойной_холдер(", "Ошибка синтаксиса, ожидался символ \")\", получен ", new Token(TokenType.EOF, "")},
                {"поставить_чашку(12)", "Ошибка синтаксиса, ожидался идентификатор или тип чашки, обнаружилось: 12", new Token(TokenType.NUMBER, "12")},
                {"вставить_холдер_в_группу)", "Ошибка синтаксиса, ожидался символ \"(\", получен )", new Token(TokenType.RPAREN, ")")},
                {"готовить_кофе(\n)", "Ошибка синтаксиса, ожидался идентификатор или вес(число), обнаружилось: ", new Token(TokenType.EOL, "")},
                {"ждать(,)", "Ошибка синтаксиса, ожидался идентификатор или время в формате: секунды или минуты:секунды, обнаружилось: ,", new Token(TokenType.COMMA, ",")},
                {"подать_напиток,", "Ошибка синтаксиса, ожидался символ \"(\", получен ,", new Token(TokenType.COMMA, ",")}
        };
    }

    @Test
    public void testAssign() {
        String input = "помол := эспрессо";
        try {
            parser.parse(input);
        } catch (AnalyzeException e) {
            fail("Не должны получить ошибку");
        }
    }

    @Test
    public void testGrindCoffee() {
        String input = "помолоть_кофе(12, эспрессо)";
        try {
            parser.parse(input);
        } catch (AnalyzeException e) {
            fail();
        }
    }

    @Test
    public void failedTest() {
        String input = "взять_двойной_холдер()";
        String inputNoParens = "взять_двойной_холдер";
        try {
            parser.parse(input);
            parser.parse(inputNoParens);
        } catch (AnalyzeException e) {
            fail();
        }
    }

    @Test(dataProvider = "failedParse")
    public void failedTestGroup(String input, String message, Token token) {
        try {
            parser.parse(input);
            fail();
        } catch (AnalyzeException e) {
            assertEquals(e.getMessage(), message);
            assertEquals(e.getCodeLine(), input);
            assertEquals(e.getToken(), token);
        }
    }

}