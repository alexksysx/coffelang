package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MemoryBank;
import ru.alexksysx.coffeelang.operand.IValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class AssignOperator implements IOperator {
    private final String identifier;
    private final IValue value;

    public AssignOperator(String identifier, IValue value) {
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public void run(PrintStream out) {
        MemoryBank.getInstance().putValue(identifier, value);
        out.printf("Добавлена переменная %s со значением: %s%n", identifier, value.getToken().getLiteral());
    }
}
