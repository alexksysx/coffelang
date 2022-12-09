package ru.alexksysx.coffeelang.operand;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.alexksysx.coffeelang.MemoryBank;
import ru.alexksysx.coffeelang.exception.MemoryException;
import ru.alexksysx.coffeelang.exception.OperandException;
import ru.alexksysx.coffeelang.operand.impl.CupValue;
import ru.alexksysx.coffeelang.operand.impl.GrindLevelValue;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operand.impl.TimeValue;
import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenType;

import static org.testng.Assert.*;

public class OperandProcessorTest {
    private OperandProcessor processor;

    @BeforeClass
    public void init() {
        processor = new OperandProcessor();
        MemoryBank.getInstance().putValue("время", new TimeValue(new Token(TokenType.TIME, "00:30"), 0L, 30L));
        MemoryBank.getInstance().putValue("температура", new NumberValue(new Token(TokenType.NUMBER, "95"), 95D));
        MemoryBank.getInstance().putValue("помол", new GrindLevelValue(new Token(TokenType.GRIND_LEVEL_ESPRESSO, "эспрессо")));
        MemoryBank.getInstance().putValue("кружка", new CupValue(new Token(TokenType.CUP_COMMON, "стакан")));
    }

    @Test
    public void testGetNumberFromIdent() throws MemoryException, OperandException {
        NumberValue temperature = processor.getNumberValue(new Token(TokenType.IDENT, "температура"));
        assertEquals(temperature.getValue(), 95D);
    }

    @Test
    public void testGetNumberFromIdentWrongType() throws MemoryException {
        try {
            NumberValue temperature = processor.getNumberValue(new Token(TokenType.IDENT, "время"));
        } catch (OperandException e) {
            assertEquals(e.getMessage(), "{Ожидался тип = NUMBER, получили = Token{tokenType=TIME, literal='00:30'}}");
        }
    }

    @Test
    public void testGetNumberWrongType() throws MemoryException {
        try {
            NumberValue temperature = processor.getNumberValue(new Token(TokenType.TIME, "00:30"));
        } catch (OperandException e) {
            assertEquals(e.getMessage(), "{Ожидался тип = NUMBER, получили = Token{tokenType=TIME, literal='00:30'}}");
        }
    }

    @Test
    public void getNumberFromIdentNotInMemory() throws OperandException {
        try {
            NumberValue temperature = processor.getNumberValue(new Token(TokenType.IDENT, "температура"));
        } catch (MemoryException e) {
            assertEquals(e.getMessage(), "В памяти не найдена переменная с данным идентификатором: температура");
        }
    }

    @Test
    public void getTimeFromIdentTest() throws MemoryException, OperandException {
        TimeValue time = processor.getTimeValue(new Token(TokenType.IDENT, "время"));
        assertEquals(time.getMinutes(), 0L);
        assertEquals(time.getSeconds(), 30L);
    }

    @Test
    public void getTimeTest() throws MemoryException, OperandException {
        TimeValue time = processor.getTimeValue(new Token(TokenType.TIME, "1:45"));
        assertEquals(time.getMinutes(), 1L);
        assertEquals(time.getSeconds(), 45L);
    }

    @Test
    public void getGrindLevelFromIdentTest() throws MemoryException, OperandException {
        GrindLevelValue grind = processor.getGrindLevelValue(new Token(TokenType.IDENT, "помол"));
        assertEquals(grind.getToken(), new Token(TokenType.GRIND_LEVEL_ESPRESSO, "эспрессо"));
    }

    @Test
    public void getGrindLevelTest() throws MemoryException, OperandException {
        GrindLevelValue grind = processor.getGrindLevelValue(new Token(TokenType.GRIND_LEVEL_FILTER, "воронка"));
        assertEquals(grind.getToken(), new Token(TokenType.GRIND_LEVEL_FILTER, "воронка"));
    }

    @Test
    public void getCupFromIdentTest() throws MemoryException, OperandException {
        CupValue grind = processor.getCupValue(new Token(TokenType.IDENT, "кружка"));
        assertEquals(grind.getToken(), new Token(TokenType.CUP_COMMON, "стакан"));
    }

    @Test
    public void getCupTest() throws MemoryException, OperandException {
        CupValue grind = processor.getCupValue(new Token(TokenType.CUP_ESPRESSO, "чашка_эспрессо"));
        assertEquals(grind.getToken(), new Token(TokenType.CUP_ESPRESSO, "чашка_эспрессо"));
    }
}