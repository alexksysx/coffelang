package ru.alexksysx.coffeelang.token;

public enum TokenType {
    ILLEGAL("ILLEGAL", TokenKind.ETC),

    IDENT("IDENT", TokenKind.ETC),
    NUMBER("NUMBER", TokenKind.ETC),
    TIME("TIME", TokenKind.ETC),

    ASSIGN(":=", TokenKind.OPERATOR),
    COMMA(",", TokenKind.ETC),

    LPAREN("(", TokenKind.ETC),
    RPAREN(")", TokenKind.ETC),

    EOL("EOL", TokenKind.ETC),
    EOF("EOF", TokenKind.ETC),

    // keywords
    // команды
    WAIT("ждать", TokenKind.OPERATOR),
    GRIND_COFFEE("помолоть_кофе", TokenKind.OPERATOR),
    SET_PRESSURE("выставить_давление", TokenKind.OPERATOR),
    SET_TEMPERATURE("выставить_температуру", TokenKind.OPERATOR),
    PREPARE_DOUBLE_HOLDER("взять_двойной_холдер", TokenKind.OPERATOR),
    PUT_CUP("поставить_чашку", TokenKind.OPERATOR),
    INSERT_HOLDER("вставить_холдер_в_группу", TokenKind.OPERATOR),
    MAKE_COFFEE("готовить_кофе", TokenKind.OPERATOR),
    SERVE_DRINK("подать_напиток", TokenKind.OPERATOR),
    // уровни помола
    GRIND_LEVEL_ESPRESSO("эспрессо", TokenKind.GRIND_LEVEL),
    GRIND_LEVEL_FILTER("воронка", TokenKind.GRIND_LEVEL),
    GRIND_LEVEL_FRENCH_PRESS("фрэнч-пресс", TokenKind.GRIND_LEVEL),
    // чашки
    CUP_COMMON("стакан", TokenKind.CUP),
    CUP_ESPRESSO("чашка_эспрессо", TokenKind.CUP),
    CUP_CAPPUCCINO("чашка_каппучино", TokenKind.CUP);


    private final String literal;
    private final TokenKind kind;

    TokenType(String literal, TokenKind kind) {
        this.literal = literal;
        this.kind = kind;
    }

    public String getLiteral() {
        return literal;
    }

    public TokenKind getKind() {
        return kind;
    }

    public static TokenType ofLiteral(String literal) {
        for (TokenType tt : TokenType.values()) {
            if (literal.equalsIgnoreCase(tt.getLiteral()))
                return tt;
        }
        return null;
    }
}
