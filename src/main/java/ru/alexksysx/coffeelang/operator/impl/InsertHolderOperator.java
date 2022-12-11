package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

import static java.lang.String.format;

public class InsertHolderOperator implements IOperator {
    public InsertHolderOperator() {
    }

    @Override
    public void run(PrintStream out) {
        MachineState.getInstance().insertHolder();
        String holder = MachineState.getInstance().getHolderType();
        out.printf("В группу вставлен %s%n", holder);
    }
}
