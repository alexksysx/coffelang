package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class ServeDrinkOperator implements IOperator {
    public ServeDrinkOperator() {
    }

    @Override
    public void run(PrintStream out) {
        if (!MachineState.getInstance().getCupReady())
            out.println("Кружки нет под группой, подавать нечего");
        else if (MachineState.getInstance().getCoffeeMadeAmount() <= 0) {
            out.println("Подаётся кружка без кофе");
        } else {
            out.println("Кружка с кофе подана клиенту");
        }
    }
}
