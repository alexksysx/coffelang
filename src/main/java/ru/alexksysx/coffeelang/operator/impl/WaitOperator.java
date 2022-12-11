package ru.alexksysx.coffeelang.operator.impl;

import ru.alexksysx.coffeelang.operand.impl.TimeValue;
import ru.alexksysx.coffeelang.operator.IOperator;

import java.io.PrintStream;

public class WaitOperator implements IOperator {
    private final TimeValue time;

    public WaitOperator(TimeValue time) {
        this.time = time;
    }

    @Override
    public void run(PrintStream out) {
        if (time.getMinutes().equals(0L) && !time.getSeconds().equals(0L))
            out.printf("Ожидаем %d секунд%n", time.getSeconds());
        else if (!time.getMinutes().equals(0L) && !time.getSeconds().equals(0L))
            out.printf("Ожидаем %d минут и %d секунд%n", time.getMinutes(), time.getSeconds());
        else if (!time.getMinutes().equals(0L) && time.getSeconds().equals(0L))
            out.printf("Ожидаем %d минут%n", time.getSeconds());
        else
            out.println("Ожидание не требуется, так как выставлено 00:00");
        // Тут могло быть реальное ожидание
    }
}
