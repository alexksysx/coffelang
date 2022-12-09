package ru.alexksysx.coffeelang;

import ru.alexksysx.coffeelang.operand.IValue;

import java.util.HashMap;
import java.util.Map;

public class MemoryBank {
    private static final MemoryBank INSTANCE = new MemoryBank();
    private final Map<String, IValue> memory;

    private MemoryBank() {
        memory = new HashMap<>();
    }

    public static MemoryBank getInstance() {
        return INSTANCE;
    }

    public void putValue(String identifier, IValue value) {
        memory.put(identifier, value);
    }

    public IValue getValue(String identifier) {
        return memory.get(identifier);
    }
}
