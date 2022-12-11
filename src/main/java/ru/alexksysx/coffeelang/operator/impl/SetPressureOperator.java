package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.exception.CoffeeRuntimeException;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class SetPressureOperator implements IOperator {
    private final NumberValue pressure;

    public SetPressureOperator(NumberValue pressure) {
        this.pressure = pressure;
    }

    @Override
    public void run(PrintStream out) throws CoffeeRuntimeException {
        if (pressure.getValue() < 1 || pressure.getValue() > 15)
            throw new CoffeeRuntimeException("Невозможно выставить такое давление (%,.1f).\n" +
                    "Давление должно быть в пределах от 1 до 15");
        MachineState.getInstance().setPressure(pressure.getValue());
        out.printf("На кофемашине выставлено давление %,.1f бар%n", pressure.getValue());
    }
}
