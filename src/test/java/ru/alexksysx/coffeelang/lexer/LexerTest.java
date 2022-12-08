package ru.alexksysx.coffeelang.lexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenType;

import static org.testng.Assert.assertEquals;

public class LexerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("LexerTest");

    @Test
    public void testLexer() {
        String input = "помол := эспрессо\n" +
                "температура := 95\n" +
                "давление := 9\n" +
                "вес := 16\n" +
                "вес_напитка := 32\n" +
                "помолоть_кофе(вес,помол)\n" +
                "выставить_давление(давление)\n" +
                "выставить_температуру(температура)\n" +
                "взять_двойной_холдер()\n" +
                "поставить_чашку(чашка_эспрессо)\n" +
                "вставить_холдер_в_группу()\n" +
                "готовить_кофе(вес_напитка)\n" +
                "ждать(0:45)\n" +
                "подать_напиток()";

        Token[] list = new Token[] {
                new Token(TokenType.IDENT, "помол"),
                new Token(TokenType.ASSIGN, ":="),
                new Token(TokenType.GRIND_LEVEL_ESPRESSO, "эспрессо"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.IDENT, "температура"),
                new Token(TokenType.ASSIGN, ":="),
                new Token(TokenType.NUMBER, "95"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.IDENT, "давление"),
                new Token(TokenType.ASSIGN, ":="),
                new Token(TokenType.NUMBER, "9"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.IDENT, "вес"),
                new Token(TokenType.ASSIGN, ":="),
                new Token(TokenType.NUMBER, "16"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.IDENT, "вес_напитка"),
                new Token(TokenType.ASSIGN, ":="),
                new Token(TokenType.NUMBER, "32"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.GRIND_COFFEE, "помолоть_кофе"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.IDENT, "вес"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.IDENT, "помол"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.SET_PRESSURE, "выставить_давление"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.IDENT, "давление"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.SET_TEMPERATURE, "выставить_температуру"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.IDENT, "температура"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.PREPARE_DOUBLE_HOLDER, "взять_двойной_холдер"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.PUT_CUP, "поставить_чашку"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.CUP_ESPRESSO, "чашка_эспрессо"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.INSERT_HOLDER, "вставить_холдер_в_группу"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.MAKE_COFFEE, "готовить_кофе"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.IDENT, "вес_напитка"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.WAIT, "ждать"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.TIME, "0:45"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOL, ""),
                new Token(TokenType.SERVE_DRINK, "подать_напиток"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOF, "")
        };

        Lexer l = new Lexer(input);
        Token t;
        for (Token expected : list) {
            t = l.nextToken();
            LOGGER.info("Найден токен: {}", t);
            assertEquals(t, expected);
        }
    }


    @Test
    public void testIllegalIdent() {
        String input = "выставить_температуру(*)";
        Token[] list = new Token[]{
                new Token(TokenType.SET_TEMPERATURE, "выставить_температуру"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.ILLEGAL, "*"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.EOF, "")
        };

        Lexer l = new Lexer(input);
        Token t;
        for (Token expected : list) {
            t = l.nextToken();
            LOGGER.info("Найден токен: {}", t);
            assertEquals(t, expected);
        }
    }
}