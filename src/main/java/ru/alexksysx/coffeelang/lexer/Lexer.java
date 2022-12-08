package ru.alexksysx.coffeelang.lexer;

import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenType;

import static ru.alexksysx.coffeelang.token.TokenType.*;

public class Lexer {
    private String input;
    private int position; // позиция текущего символа
    private int readPosition; // позиция символа за текущим
    private char symbol; // рассматриваемый символ

    public Lexer() {
    }

    public Lexer(String input) {
        this.input = input;
        readChar();
    }

    public void setInput(String input) {
        this.input = input;
        position = 0;
        readPosition = 0;
        readChar();
    }

    public String getInput() {
        return input;
    }

    public Token nextToken() {
        Token token;
        skipWhiteSpace();
        switch (symbol) {
            case ':':
                if (peekChar() == '=') {
                    readChar();
                    token = new Token(TokenType.ASSIGN, ":=");
                } else {
                    token = new Token(TokenType.ILLEGAL, symbol);
                }
                break;
            case '(':
                token = new Token(TokenType.LPAREN, symbol);
                break;
            case ')':
                token = new Token(TokenType.RPAREN, symbol);
                break;
            case ',':
                token = new Token(TokenType.COMMA, symbol);
                break;
            case '\n':
                token = new Token(TokenType.EOL, "");
                break;
            case '\u0000':
                token = new Token(TokenType.EOF, "");
                break;
            default:
                if (isLetter(symbol)) {
                    String literal = readIdentifier();
                    token = new Token(getIdentifier(literal), literal);
                } else if (isDigit(symbol)) {
                    String number = readNumber();
                    TokenType type = NUMBER;
                    if (number.contains(":"))
                        type = TIME;
                    token = new Token(type, number);
                } else {
                    token = new Token(TokenType.ILLEGAL, symbol);
                }
        }
        readChar();
        return token;
    }

    private void readChar() {
        if (readPosition >= input.length()) {
            symbol = '\u0000';
        } else {
            symbol = input.charAt(readPosition);
        }
        position = readPosition;
        readPosition++;
    }

    private char peekChar() {
        if (readPosition >= input.length()) {
            return 0;
        } else {
            return input.charAt(readPosition);
        }
    }

    private void skipWhiteSpace() {
        while (symbol == ' ' || symbol == '\t' || symbol == '\r') {
            readChar();
        }
    }

    private String readIdentifier() {
        int startPosition = position;
        if (isLetter(peekChar()))
            do {
                readChar();
            }
            while (isLetter(peekChar()));
        return input.substring(startPosition, position + 1);
    }

    private String readNumber() {
        int startPosition = position;
        if (isDigit(peekChar()))
            do {
                readChar();
            } while (isDigit(peekChar()));
        return input.substring(startPosition, position + 1);
    }

    private static boolean isLetter(char ch) {
        return Character.isAlphabetic(ch) || ch == '_';
    }

    private static boolean isDigit(char ch) {
        return Character.isDigit(ch) || ch == '.' || ch == ':';
    }

    public static TokenType getIdentifier(String literal) {
        TokenType type = TokenType.ofLiteral(literal.toLowerCase());
        return type == null ? IDENT : type;
    }
}
