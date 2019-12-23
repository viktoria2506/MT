package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Lexer lexer = new Lexer();
    private List<Token> tokens = new ArrayList<>();
    private int i = 0;

    private void reset() {
        i = 0;
    }

    public Tree.Node parse(CharSequence input) throws IOException {
        reset();
        tokens = lexer.parse(input + "$");

        System.out.println(input);
        for (Token token : tokens) {
            System.out.print(token.type() + " ");
        }
        System.out.println();

        Tree.Node root = parseS();
        if (tokens.get(i).type().equals(Token.type.END)) {
            return root;
        } else {
            throw new IOException("No $ at the end.");
        }
    }

    private void consume(Token.type type) throws IOException {
        if (type == Token.type.NAME && get().type() == Token.type.TYPE) {
            i++;
            return;
        }
        if (get().type() != type) throw new IOException("Unexpected token " + get().value());
        i++;
    }

    private Token get() {
        return tokens.get(i);
    }

    private Tree.Node parseS() throws IOException {
        Tree.Node root = new Tree.Node("S");
        Token token = get();
        switch (token.type()) {
            case FUNCTION:
                consume(Token.type.FUNCTION);
                root.addSon(new Tree.Node(token.value(), true));
                root.addSon(parseT());
                consume(Token.type.COLON);
                root.addSon(new Tree.Node(":", true));
                Token type = get();
                consume(Token.type.TYPE);
                root.addSon(new Tree.Node(type.value(), true));
                consume(Token.type.SEMICOLON);
                root.addSon(new Tree.Node(";", true));
                break;
            case PROCEDURE:
                consume(Token.type.PROCEDURE);
                root.addSon(new Tree.Node(token.value(), true));
                root.addSon(parseT());
                consume(Token.type.SEMICOLON);
                root.addSon(new Tree.Node(";", true));
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseT() throws IOException {
        Tree.Node root = new Tree.Node("T");
        Token token = get();
        switch (token.type()) {
            case TYPE:
            case NAME:
                Token name = get();
                consume(Token.type.NAME);
                root.addSon(new Tree.Node(name.value(), true));
                root.addSon(parseTStroke());
                /*consume(Token.type.LPAREN);
                root.addSon(new Tree.Node("(", true));
                root.addSon(parseARGS());
                consume(Token.type.RPAREN);
                root.addSon(new Tree.Node(")", true));*/
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseTStroke() throws IOException {
        Tree.Node root = new Tree.Node("TStroke");
        Token token = get();
        switch (token.type()) {
            case LPAREN:
                consume(Token.type.LPAREN);
                root.addSon(new Tree.Node("(", true));
                root.addSon(parseARGS());
                consume(Token.type.RPAREN);
                root.addSon(new Tree.Node(")", true));
                break;
            case COLON:
            case SEMICOLON:
                root.addSon(new Tree.Node("esp", true));
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseARGS() throws IOException {
        Tree.Node root = new Tree.Node("ARGS");
        Token token = get();
        switch (token.type()) {
            case RPAREN:
                root.addSon(new Tree.Node("eps", true));
                break;
            case TYPE:
            case NAME:
            case VAR:
                root.addSon(parseV());
                root.addSon(parseListV());
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseListV() throws IOException {
        Tree.Node root = new Tree.Node("LIST_V");
        Token token = get();
        switch (token.type()) {
            case RPAREN:
                root.addSon(new Tree.Node("eps", true));
                break;
            case SEMICOLON:
                consume(Token.type.SEMICOLON);
                root.addSon(new Tree.Node(";", true));
                root.addSon(parseV());
                root.addSon(parseListV());
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseListVStroke() throws IOException {
        Tree.Node root = new Tree.Node("VSTROKE");
        Token token = get();
        switch (token.type()) {
            case NAME:
                Token name = get();
                consume(Token.type.NAME);
                root.addSon(new Tree.Node(name.value(), true));
                break;
            case VAR:
                consume(Token.type.VAR);
                root.addSon(new Tree.Node("var", true));
                Token name1 = get();
                consume(Token.type.NAME);
                root.addSon(new Tree.Node(name1.value(), true));
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseV() throws IOException {
        Tree.Node root = new Tree.Node("V");
        Token token = get();
        switch (token.type()) {
            case TYPE:
            case NAME:
            case VAR:
//                Token name = get();
//                consume(Token.type.NAME);
//                root.addSon(new Tree.Node(name.value(), true));
                root.addSon(parseListVStroke());
                root.addSon(parseListName());
                consume(Token.type.COLON);
                root.addSon(new Tree.Node(":", true));
                Token type = get();
                consume(Token.type.TYPE);
                root.addSon(new Tree.Node(type.value(), true));
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }

    private Tree.Node parseListName() throws IOException {
        Tree.Node root = new Tree.Node("LIST_NAME");
        Token token = get();
        switch (token.type()) {
            case COLON:
                root.addSon(new Tree.Node("eps", true));
                break;
            case COMMA:
                consume(Token.type.COMMA);
                root.addSon(new Tree.Node(",", true));
                Token name = get();
                consume(Token.type.NAME);
                root.addSon(new Tree.Node(name.value(), true));
                root.addSon(parseListName());
                break;
            default:
                throw new IOException("Unexpected symbol in token " + token.value());
        }
        return root;
    }
}