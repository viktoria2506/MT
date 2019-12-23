package parser;

public class Token {
    public enum type {
        FUNCTION,
        PROCEDURE,
        TYPE,
        NAME,
        LPAREN,    //(
        RPAREN,    //)
        COMMA,     //,
        COLON,     //:
        SEMICOLON, //;
        END,
        VAR
    }

    private type type;
    private CharSequence value;

    Token(type type, CharSequence value) {
        this.type = type;
        this.value = value;
    }

    public type type() {
        return type;
    }

    public CharSequence value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}