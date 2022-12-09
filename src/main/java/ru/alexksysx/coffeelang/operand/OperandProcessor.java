package ru.alexksysx.coffeelang.operand;

import ru.alexksysx.coffeelang.MemoryBank;
import ru.alexksysx.coffeelang.exception.MemoryException;
import ru.alexksysx.coffeelang.exception.OperandException;
import ru.alexksysx.coffeelang.operand.impl.CupValue;
import ru.alexksysx.coffeelang.operand.impl.GrindLevelValue;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operand.impl.TimeValue;
import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenKind;
import ru.alexksysx.coffeelang.token.TokenType;

public class OperandProcessor {

    public IValue getValue(Token token) throws MemoryException, OperandException {
        switch (token.getTokenType()) {
            case NUMBER:
                return getNumberValue(token);
            case TIME:
                return getTimeValue(token);
            case GRIND_LEVEL_ESPRESSO:
            case GRIND_LEVEL_FILTER:
            case GRIND_LEVEL_FRENCH_PRESS:
                return getGrindLevelValue(token);
            case CUP_ESPRESSO:
            case CUP_COMMON:
            case CUP_CAPPUCCINO:
                return getCupValue(token);
            default:
                throw new OperandException("Ошибка при определении типа данных");
        }
    }

    public NumberValue getNumberValue(Token token) throws OperandException, MemoryException {
        IValue value;
        if (token.getTokenType() == TokenType.IDENT) {
            value = processIdentifier(token);
            if (checkInstance(value, NumberValue.class))
                return (NumberValue) value;
            else
                throw new OperandException(value.getToken(), TokenType.NUMBER);

        } else if (token.getTokenType() == TokenType.NUMBER) {
            return new NumberValue(token, Double.parseDouble(token.getLiteral()));
        }
        throw new OperandException(token, TokenType.NUMBER);
    }

    public TimeValue getTimeValue(Token token) throws OperandException, MemoryException {
        IValue value;
        if (token.getTokenType() == TokenType.IDENT) {
            value = processIdentifier(token);
            if (checkInstance(value, TimeValue.class)) {
                return (TimeValue) value;
            } else {
                throw new OperandException(value.getToken(), TokenType.TIME);
            }
        } else if (token.getTokenType() == TokenType.TIME) {
            String[] values = token.getLiteral().split(":");
            Long minutes = Long.parseLong(values[0]);
            Long seconds = Long.parseLong(values[1]);
            return new TimeValue(token, minutes, seconds);
        }
        throw new OperandException(token, TokenType.TIME);
    }

    public GrindLevelValue getGrindLevelValue(Token token) throws MemoryException, OperandException {
        IValue value;
        if (token.getTokenType() == TokenType.IDENT) {
            value = processIdentifier(token);
            if (checkInstance(value, GrindLevelValue.class))
                return (GrindLevelValue) value;
            else
                throw new OperandException(value.getToken(), TokenKind.GRIND_LEVEL);

        } else if (token.getTokenType().getKind() == TokenKind.GRIND_LEVEL) {
            return new GrindLevelValue(token);
        }
        throw new OperandException(token, TokenKind.GRIND_LEVEL);
    }

    public CupValue getCupValue(Token token) throws MemoryException, OperandException {
        IValue value;
        if (token.getTokenType() == TokenType.IDENT) {
            value = processIdentifier(token);
            if (checkInstance(value, CupValue.class))
                return (CupValue) value;
            else
                throw new OperandException(value.getToken(), TokenKind.CUP);

        } else if (token.getTokenType().getKind() == TokenKind.CUP) {
            return new CupValue(token);
        }
        throw new OperandException(token, TokenKind.CUP);
    }

    private IValue processIdentifier(Token token) throws MemoryException {
        IValue value = MemoryBank.getInstance().getValue(token.getLiteral());
        if (value == null)
            throw new MemoryException("В памяти не найдена переменная с данным идентификатором: " + token.getLiteral());
        return value;
    }

    private boolean checkInstance(IValue value, Class<? extends IValue> valueClass) {
        return valueClass.isInstance(value);
    }
}
