package ru.alexksysx.coffeelang.parser;

import ru.alexksysx.coffeelang.exception.AnalyzeException;
import ru.alexksysx.coffeelang.lexer.Lexer;
import ru.alexksysx.coffeelang.operator.impl.*;
import ru.alexksysx.coffeelang.operator.IOperator;
import ru.alexksysx.coffeelang.token.Token;
import ru.alexksysx.coffeelang.token.TokenKind;
import ru.alexksysx.coffeelang.token.TokenType;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private Token peekToken;

    public Parser() {
        lexer = new Lexer();
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
        nextToken();
        checkEndOfLine();
        return new PutCupOperator();
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
        nextToken();
        checkEndOfLine();
        return new MakeCoffeeOperator();
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
        nextToken();
        checkEndOfLine();
        return new SetPressureOperator();
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
        nextToken();
        checkEndOfLine();
        return new SetPressureOperator();
    }

    private IOperator parseAssignStatement() throws AnalyzeException {
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
        checkEndOfLine();
        return new AssignOperator();
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
        nextToken();
        checkEndOfLine();
        return new WaitOperator();
    }

    private IOperator parseGrindCoffee() throws AnalyzeException {
        if (peekToken.getTokenType() != TokenType.LPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \"(\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType() != TokenType.NUMBER && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидалось число или идентификатор, обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.COMMA)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \",\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        nextToken();
        if (currentToken.getTokenType().getKind() != TokenKind.GRIND_LEVEL && currentToken.getTokenType() != TokenType.IDENT)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался идентификатор или степень помола кофе, обнаружилось: " + currentToken.getLiteral(), lexer.getInput(), currentToken);
        if (peekToken.getTokenType() != TokenType.RPAREN)
            throw new AnalyzeException("Ошибка синтаксиса, ожидался символ \")\", получен " + peekToken.getLiteral(), lexer.getInput(), peekToken);
        nextToken();
        checkEndOfLine();
        return new GrindCoffeeOperator();
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
