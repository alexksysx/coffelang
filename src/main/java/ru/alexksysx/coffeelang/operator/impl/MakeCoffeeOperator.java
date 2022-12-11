package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.exception.CoffeeRuntimeException;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class MakeCoffeeOperator implements IOperator {
    private final NumberValue weight;

    public MakeCoffeeOperator(NumberValue weight) {
        this.weight = weight;
    }

    @Override
    public void run(PrintStream out) throws CoffeeRuntimeException {
        if (!MachineState.getInstance().getHolderInserted())
            out.printf("Через группу без холдера пролита вода (%,.1f) с температурой %,.1f%n",
                    weight.getValue(),MachineState.getInstance().getWaterTemperature());
        else if (MachineState.getInstance().getCoffeeGroundAmount() <= 0) {
            if (!MachineState.getInstance().getCupReady())
                out.printf("Прогрев группы водой с температурой %,.1f%n", MachineState.getInstance().getWaterTemperature());
            else
                out.printf("В чашку налита вода с температурой %,.1f%n", MachineState.getInstance().getWaterTemperature());
        } else if (MachineState.getInstance().getCoffeeGroundAmount() > 0) {
            if (!MachineState.getInstance().getCupReady()) {
                throw new CoffeeRuntimeException(("Внимание!!! Под группой нет кружки, готовый кофе может утечь в слив!"));
            } else {
                MachineState.getInstance().addMadeCoffeeAmount(weight.getValue());
                out.printf("Через кофейную таблетку пролито %,.1f грамм воды с температурой %,.1f под давлением %,.1f%n",
                        weight.getValue(), MachineState.getInstance().getWaterTemperature(),
                        MachineState.getInstance().getPressure());
            }
        }

    }
}
