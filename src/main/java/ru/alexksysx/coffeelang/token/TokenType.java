package ru.alexksysx.coffeelang.token;

public enum TokenType {
    ILLEGAL("ILLEGAL"),

    IDENT("IDENT"),
    NUMBER("NUMBER"),

    ASSIGN(":="),
    COMMA(","),

    LPAREN("("),
    RPAREN(")"),

    EOL("EOL"),
    EOF("EOF"),

    // keywords
    // команды
    WAIT("ждать"),
    GRIND_COFFEE("помолоть_кофе"),
    SET_PRESSURE("выставить_давление"),
    SET_TEMPERATURE("выставить_температуру"),
    PREPARE_DOUBLE_HOLDER("взять_двойной_холдер"),
    PUT_CUP("поставить_чашку"),
    INSERT_HOLDER("вставить_холдер_в_группу"),
    MAKE_COFFEE("готовить_кофе"),
    SERVE_DRINK("подать_напиток"),
    // уровни помола
    GRIND_LEVEL_ESPRESSO("эспрессо"),
    GRIND_LEVEL_FILTER("воронка"),
    GRIND_LEVEL_FRENCH_PRESS("фрэнч-пресс"),
    // Сосуды
    CUP_COMMON("стакан"),
    CUP_ESPRESSO("чашка_эспрессо"),
    CUP_CAPPUCCINO("чашка_каппучино");


    private final String literal;

    TokenType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public static TokenType ofLiteral(String literal) {
        for (TokenType tt : TokenType.values()) {
            if (literal.equalsIgnoreCase(tt.getLiteral()))
                return tt;
        }
        return null;
    }
}
