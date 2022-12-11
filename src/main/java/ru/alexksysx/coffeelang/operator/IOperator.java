package ru.alexksysx.coffeelang.operator;

import ru.alexksysx.coffeelang.exception.CoffeeRuntimeException;

import java.io.PrintStream;

public interface IOperator {
    void run(PrintStream out) throws CoffeeRuntimeException;
}
