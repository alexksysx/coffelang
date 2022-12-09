package ru.alexksysx.coffeelang.exception;

import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenKind;
import ru.alexksysx.coffeelang.token.TokenType;

public class OperandException extends Exception{

    public OperandException(Token token, TokenType tokenType) {
        super("{Ожидался тип = " + tokenType + ", получили = " + token + "}");
    }

    public OperandException(Token token, TokenKind tokenKind) {
        super("{Ожидался тип = " + tokenKind + ", получили = " + token + "}");
    }
}
