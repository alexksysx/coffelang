package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class PrepareDoubleHolderOperator implements IOperator {
    public PrepareDoubleHolderOperator() {
    }

    @Override
    public void run(PrintStream out) {
        MachineState.getInstance().setHolderType("двойной холдер");
        out.println("Для работы подготовлен двойной холдер");
    }
}
