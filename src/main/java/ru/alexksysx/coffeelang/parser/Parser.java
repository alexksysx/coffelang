package ru.alexksysx.coffeelang.parser;

import ru.alexksysx.coffeelang.AnalyzeException;
import ru.alexksysx.coffeelang.lexer.Lexer;
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

    public void parse(String line) throws AnalyzeException {
        lexer.setInput(line);
        nextToken();
        nextToken();
        parseStatement();
    }

    private void parseStatement() throws AnalyzeException {
        if (currentToken.getTokenType() == TokenType.IDENT) {
            parseAssignStatement();
        } else if (currentToken.getTokenType().getKind() == TokenKind.OPERATOR) {
            parseOperator();
        } else if (currentToken.getTokenType() == TokenType.EOF || currentToken.getTokenType() == TokenType.EOL) {
            return;
        } else {
            throw new AnalyzeException("Ожидается идентификатор или команда", lexer.getInput(), currentToken);
        }
    }

    private void parseOperator() throws AnalyzeException {
        switch (currentToken.getTokenType()) {
            case WAIT:
                parseWait();
                break;
            case GRIND_COFFEE:
                parseGrindCoffee();
                break;
            case SET_TEMPERATURE:
                parseSetTemperature();
                break;
            case SET_PRESSURE:
                parseSetPressure();
                break;
            case PREPARE_DOUBLE_HOLDER:
                parsePrepareDoubleHolder();
                break;
            case PUT_CUP:
                parsePutCup();
                break;
            case INSERT_HOLDER:
                parseInsertHolder();
                break;
            case MAKE_COFFEE:
                parseMakeCoffee();
                break;
            case SERVE_DRINK:
                parseServeDrink();
                break;
            default:
                throw new AnalyzeException("Ошибка синтаксиса: неожиданный токен", lexer.getInput(), currentToken);
        }
    }

    private void parseInsertHolder() throws AnalyzeException {
        checkOperatorWithoutArgs();
    }

    private void parseServeDrink() throws AnalyzeException {
        checkOperatorWithoutArgs();
    }

    private void parsePrepareDoubleHolder() throws AnalyzeException {
        checkOperatorWithoutArgs();
    }

    private void parsePutCup() throws AnalyzeException {
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
    }

    private void parseMakeCoffee() throws AnalyzeException {
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
    }

    private void parseSetTemperature() throws AnalyzeException {
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
    }

    private void parseSetPressure() throws AnalyzeException {
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
    }

    private void parseAssignStatement() throws AnalyzeException {
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
    }

    private void parseWait() throws AnalyzeException {
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

    }

    private void parseGrindCoffee() throws AnalyzeException {
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
