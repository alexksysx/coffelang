package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.MachineState;
import ru.alexksysx.coffeelang.exception.CoffeeRuntimeException;
import ru.alexksysx.coffeelang.operand.impl.CupValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

import static java.lang.String.format;

public class PutCupOperator implements IOperator {
    private final CupValue cupValue;

    public PutCupOperator(CupValue cupValue) {
        this.cupValue = cupValue;
    }

    @Override
    public void run(PrintStream out) throws CoffeeRuntimeException {
        MachineState.getInstance().setCupReady();
        String commonCupSet = "Под группу поставлен стакан";
        String anotherCupSet = "Под группу поставлена ";
        switch (cupValue.getToken().getTokenType()) {
            case CUP_ESPRESSO:
                out.println(anotherCupSet + "чашка для эспрессо");
                break;
            case CUP_CAPPUCCINO:
                out.println(anotherCupSet + "чашка для каппучино");
                break;
            case CUP_COMMON:
                out.println(commonCupSet);
                break;
            default:
                throw new CoffeeRuntimeException("Произошла ошибка при подставке кружки под группу");
        }
    }
}
