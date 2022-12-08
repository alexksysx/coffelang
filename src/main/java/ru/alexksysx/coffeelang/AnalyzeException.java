package ru.alexksysx.coffeelang;

import ru.alexksysx.coffeelang.token.Token;

public class AnalyzeException extends Exception {
    private final Token token;
    private final String codeLine;

    public AnalyzeException(String message, String codeLine, Token token) {
        super(message);
        this.codeLine = codeLine;
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public String getCodeLine() {
        return codeLine;
    }
}
