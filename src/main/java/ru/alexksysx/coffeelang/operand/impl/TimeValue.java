package ru.alexksysx.coffeelang.operand.impl;

import ru.alexksysx.coffeelang.operand.IValue;
import ru.alexksysx.coffeelang.token.Token;

public class TimeValue extends IValue {
    private final Long minutes;
    private final Long seconds;
    public TimeValue(Token token, Long minutes, Long seconds) {
        super(token);
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Long getMinutes() {
        return minutes;
    }

    public Long getSeconds() {
        return seconds;
    }
}
