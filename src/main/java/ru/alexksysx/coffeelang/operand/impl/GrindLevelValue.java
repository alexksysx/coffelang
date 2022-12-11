package ru.alexksysx.coffeelang.operand.impl;

import ru.alexksysx.coffeelang.operand.IValue;
import ru.alexksysx.coffeelang.token.Token;

public class GrindLevelValue extends IValue {
    public GrindLevelValue(Token token) {
        super(token);
    }

    public String getGrindLevel() {
        return token.getTokenType().getLiteral();
    }
}
