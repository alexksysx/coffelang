package ru.alexksysx.coffeelang.parser;

import ru.alexksysx.coffeelang.exception.AnalyzeException;
import ru.alexksysx.coffeelang.exception.MemoryException;
import ru.alexksysx.coffeelang.exception.OperandException;
import ru.alexksysx.coffeelang.lexer.Lexer;
import ru.alexksysx.coffeelang.operand.IValue;
import ru.alexksysx.coffeelang.operand.OperandProcessor;
import ru.alexksysx.coffeelang.operand.impl.CupValue;
import ru.alexksysx.coffeelang.operand.impl.GrindLevelValue;
import ru.alexksysx.coffeelang.operand.impl.NumberValue;
import ru.alexksysx.coffeelang.operand.impl.TimeValue;
import ru.alexksysx.coffeelang.operator.impl.*;
import ru.alexksysx.coffeelang.operator.IOperator;
import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenKind;
import ru.alexksysx.coffeelang.token.TokenType;

public class Parser {
    private final Lexer lexer;
    private final OperandProcessor processor;
    private Token currentToken;
    private Token peekToken;

    public Parser() {
        lexer = new Lexer();
        processor = new OperandProcessor();
    }

    public IOperator parseLine(String line) throws AnalyzeException {
        lexer.setInput(line);
        nextToken();
        nextToken();
        return parseStatement();
    }

    private IOperator parseStatement() throws AnalyzeException {
        if (currentToken.getTokenType() == TokenType.IDENT) {
            return parseAssignStatement();
        } else if (currentToken.getTokenType().getKind() == TokenKind.OPERATOR) {
            return parseOperator();
        } else if (currentToken.getTokenType() == TokenType.EOF || currentToken.getTokenType() == TokenType.EOL) {
            return null;
        } else {
            throw new AnalyzeException("Ожидается идентификатор или команда", lexer.getInput(), currentToken);
        }
    }

    private IOperator parseOperator() throws AnalyzeException {
        switch (currentToken.getTokenType()) {
            case WAIT:
                return parseWait();
            case GRIND_COFFEE:
                return parseGrindCoffee();
            case SET_TEMPERATURE:
                return parseSetTemperature();
            case SET_PRESSURE:
                return parseSetPressure();
            case PREPARE_DOUBLE_HOLDER:
                return parsePrepareDoubleHolder();
            case PUT_CUP:
                return parsePutCup();
            case INSERT_HOLDER:
                return parseInsertHolder();
            case MAKE_COFFEE:
                return parseMakeCoffee();
            case SERVE_DRINK:
                return parseServeDrink();
            default:
                throw new AnalyzeException("Ошибка синтаксиса: неожиданный токен", lexer.getInput(), currentToken);
        }
    }

    private IOperator parseInsertHolder() throws AnalyzeException {
        checkOperatorWithoutArgs();
        return new InsertHolderOperator();
    }

    private IOperator parseServeDrink() throws AnalyzeException {
        checkOperatorWithoutArgs();
        return new ServeDrinkOperator();
    }

    private IOperator parsePrepareDoubleHolder() throws AnalyzeException {
        checkOperatorWithoutArgs();
        return new PrepareDoubleHolderOperator();
    }

    private IOperator parsePutCup() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType().getKind() != TokenKind.CUP && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или тип чашки, обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);

        CupValue cupValue;
        try {
            cupValue = processor.getCupValue(currentToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        nextToken();
        checkEndOfLine();
        return new PutCupOperator(cupValue);
    }

    private IOperator parseMakeCoffee() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType() != TokenType.NUMBER && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или вес(число), обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);

        NumberValue weight;
        try {
            weight = processor.getNumberValue(currentToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        nextToken();
        checkEndOfLine();
        return new MakeCoffeeOperator(weight);
    }

    private IOperator parseSetTemperature() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType() != TokenType.NUMBER && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или температура(число), обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);

        NumberValue temperature;
        try {
            temperature = processor.getNumberValue(currentToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        nextToken();
        checkEndOfLine();
        return new SetTemperatureOperator(temperature);
    }

    private IOperator parseSetPressure() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType() != TokenType.NUMBER && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или давление(число), обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);

        NumberValue pressure;
        try {
            pressure = processor.getNumberValue(currentToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        nextToken();
        checkEndOfLine();
        return new SetPressureOperator(pressure);
    }

    private IOperator parseAssignStatement() throws AnalyzeException {
        String identifier = currentToken.getLiteral();
        if (peekToken.getTokenType() != TokenType.ASSIGN) {
            throw new AnalyzeException("Ошибка синтаксиса: ожидается оператор присваивания \":=\", найдено \"" + peekToken.getLiteral() + "\"", lexer.getInput(), peekToken);
        }
        nextToken();
        if ((peekToken.getTokenType() != TokenType.NUMBER) &&
                (peekToken.getTokenType().getKind() != TokenKind.GRIND_LEVEL) &&
                (peekToken.getTokenType().getKind() != TokenKind.CUP) &&
                (peekToken.getTokenType() != TokenType.IDENT) &&
                (peekToken.getTokenType() != TokenType.TIME)
        ) {
            throw new AnalyzeException("Ошибка синтаксиса: ожидается число, время, идентификатор, степень помола или тип кружки для подачи", lexer.getInput(), peekToken);
        }
        nextToken();
        IValue value;
        try {
            value = processor.getValue(currentToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        checkEndOfLine();
        return new AssignOperator(identifier, value);
    }

    private IOperator parseWait() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType() != TokenType.TIME && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или время в формате: секунды или минуты:секунды, обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);

        TimeValue time;
        try {
            time = processor.getTimeValue(currentToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        nextToken();
        checkEndOfLine();
        return new WaitOperator(time);
    }

    private IOperator parseGrindCoffee() throws AnalyzeException {
        Token weightToken;
        Token grindLevelToken;
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType() != TokenType.NUMBER && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидалось число или идентификатор, обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.COMMA)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \",\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        weightToken = currentToken;
        nextToken();
        nextToken();
        if (currentToken.getTokenType().getKind() != TokenKind.GRIND_LEVEL && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или степень помола кофе, обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        grindLevelToken = currentToken;
        NumberValue weight;
        GrindLevelValue grindLevel;
        try {
            weight = processor.getNumberValue(weightToken);
            grindLevel = processor.getGrindLevelValue(grindLevelToken);
        } catch (OperandException | MemoryException e) {
            throw new AnalyzeException(e.getMessage(), lexer.getInput(), currentToken);
        }
        nextToken();
        checkEndOfLine();
        return new GrindCoffeeOperator(weight, grindLevel);
    }

    private void nextToken() throws AnalyzeException {
        currentToken = peekToken;
        peekToken = lexer.nextToken();
        checkIllegalToken();
    }

    private void checkEndOfLine() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.EOF && peekToken.getTokenType() != TokenType.EOL) {
            throw new AnalyzeException("Ошибка синтаксиса: новые команды необходимо делать с новой строки", lexer.getInput(), peekToken);
        }
    }

    private void checkIllegalToken() throws AnalyzeException {
        if (peekToken.getTokenType() == TokenType.ILLEGAL) {
            throw new AnalyzeException("Произошла ошибка при лексическом анализе, найден некорректный символ: " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        }
    }

    private void checkOperatorWithoutArgs() throws AnalyzeException {
        try {
            checkEndOfLine();
        } catch (AnalyzeException e) {
            if (peekToken.getTokenType() != TokenType.LPAREN)
                throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
            nextToken();
            if (peekToken.getTokenType() != TokenType.RPAREN)
                throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
            nextToken();
            checkEndOfLine();
        }
    }
}
