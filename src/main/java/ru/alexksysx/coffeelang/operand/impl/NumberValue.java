package ru.alexksysx.coffeelang.operand.impl;

import ru.alexksysx.coffeelang.operand.IValue;
import ru.alexksysx.coffeelang.token.Token;

public class NumberValue extends IValue {
    private final Double value;

    public NumberValue(Token token, Double value) {
        super(token);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
