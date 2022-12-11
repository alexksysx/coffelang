package ru.alexksysx.coffeelang;

import ru.alexksysx.coffeelang.repl.Repl;

public class Main {
    public static void main(String[] args) {
        Repl repl = new Repl();
        repl.start(System.out, System.in);
    }
}
