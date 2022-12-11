package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.exception.CoffeeRuntimeException;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

import static java.lang.String.format;

public class SetTemperatureOperator implements IOperator {
    private final NumberValue temperature;

    public SetTemperatureOperator(NumberValue temperature) {
        this.temperature = temperature;
    }

    @Override
    public void run(PrintStream out) throws CoffeeRuntimeException {
        if (temperature.getValue() < 30 || temperature.getValue() > 100)
            throw new CoffeeRuntimeException(format("Невозможно выставить такую температуру (%,.1f).%n" +
                    "Давление должно быть в пределах от 30 до 100 градусов", temperature.getValue()));
        MachineState.getInstance().setWaterTemperature(temperature.getValue());
        out.printf("На кофемашине выставлена температура %,.1f градусов%n", temperature.getValue());
    }
}
