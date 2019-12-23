package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String FUNCTION = "function";
    private String PROCEDURE = "procedure";
    private String VAR = "var";
    private String[] TYPES = {"integer", "real", "double", "char", "boolean", "byte", "string"};

    private boolean partOfName(char c) {
        return Character.isAlphabetic(c)
                || Character.isDigit(c)
                || c == '_';
    }
    private boolean isType(String s) {
        for (String type : TYPES) {
            if (type.equals(s)) return true;
        }
        return false;
    }

    public List<Token> parse(CharSequence s) throws IOException {
        List<Token> tokens = new ArrayList<>();

        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }


            StringBuilder value = new StringBuilder();
            value.append(c);

            switch (c) {
                case '(':
                    tokens.add(new Token(Token.type.LPAREN, value));
                    break;
                case ')':
                    tokens.add(new Token(Token.type.RPAREN, value));
                    break;
                case ',':
                    tokens.add(new Token(Token.type.COMMA, value));
                    break;
                case ':':
                    tokens.add(new Token(Token.type.COLON, value));
                    break;
                case ';':
                    tokens.add(new Token(Token.type.SEMICOLON, value));
                    break;
                case '$':
                    tokens.add(new Token(Token.type.END, value));
                    break;
                default :
                    if(Character.isAlphabetic(c)) {
                        int j = i + 1;
                        while(j < s.length() && partOfName(s.charAt(j))) {
                            value.append(s.charAt(j++));
                        }
                        if (value.toString().equals(FUNCTION)) tokens.add(new Token(Token.type.FUNCTION, value));
                        else if (value.toString().equals(PROCEDURE)) tokens.add(new Token(Token.type.PROCEDURE, value));
                        else if(value.toString().equals(VAR)) tokens.add(new Token(Token.type.VAR, value));
                        else if (isType(value.toString())) tokens.add(new Token(Token.type.TYPE, value));
                        else tokens.add(new Token(Token.type.NAME, value));
                    } else {
                        throw new IOException("Can't parse symbol \'" + c + "\' at position " + i);
                    }
                    break;
            }

            i += value.length();
        }

        return tokens;
    }
}