package ru.alexksysx.coffeelang.token;

public class Token {
    private final TokenType tokenType;
    private final String literal;

    public Token(TokenType tokenType, String literal) {
        this.tokenType = tokenType;
        this.literal = literal;
    }

    public Token(TokenType tokenType, char literal) {
        this.tokenType = tokenType;
        this.literal = String.valueOf(literal);
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", literal='" + literal + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (tokenType != token.tokenType) return false;
        return literal.equals(token.literal);
    }

    @Override
    public int hashCode() {
        int result = tokenType.hashCode();
        result = 31 * result + literal.hashCode();
        return result;
    }
}
