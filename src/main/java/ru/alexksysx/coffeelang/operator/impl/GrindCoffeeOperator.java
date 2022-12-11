package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.operand.impl.GrindLevelValue;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class GrindCoffeeOperator implements IOperator {
    private final NumberValue weight;
    private final GrindLevelValue grindLevel;

    public GrindCoffeeOperator(NumberValue weight, GrindLevelValue grindLevel) {
        this.weight = weight;
        this.grindLevel = grindLevel;
    }

    @Override
    public void run(PrintStream out) {
        out.printf("Помолото %,.1f грамм кофе с уровнем помола %s%n", weight.getValue(), grindLevel.getGrindLevel());
        MachineState.getInstance().addGroundCoffeeAmount(weight.getValue());
        MachineState.getInstance().removeHolder();
    }
}
