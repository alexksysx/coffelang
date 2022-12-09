package ru.alexksysx.coffeelang.operand;

import ru.alexksysx.coffeelang.token.Token;

public abstract class IValue {
    protected final Token token;

    protected IValue(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
