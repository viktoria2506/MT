package tests;

import parser.Tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator {
    private static Random random = new Random();

    private static char letter() {
        return (char) ('a' + random.nextInt(26));
    }

    private static char digit() {
        return (char) ('0' + random.nextInt(10));
    }


    private static Tree.Node name() {
        StringBuilder sb = new StringBuilder();
        sb.append(letter());
        for (int j = 0; j < random.nextInt(4); ++j) {
            sb.append(random.nextBoolean() ? letter() : digit());
        }
        return new Tree.Node(sb, true);
    }

    private static Tree.Node type() {
        CharSequence[] TYPES = {"integer", "real", "double", "char", "boolean", "byte", "string"};
        return new Tree.Node(TYPES[random.nextInt(7)], true);
    }

    private static Tree.Node v() {
        Tree.Node root = new Tree.Node("V");
        root.addSon(name());
        root.addSon(listName());
        root.addSon(new Tree.Node(":", true));
        root.addSon(type());
        return root;
    }

    private static Tree.Node listName() {
        if (random.nextBoolean()) {
            return new Tree.Node("eps", true);
        } else {
            Tree.Node root = new Tree.Node("LIST_NAME");
            root.addSon(new Tree.Node(",", true));
            root.addSon(name());
            root.addSon(listName());
            return root;
        }
    }

    private static Tree.Node t() {
        Tree.Node root = new Tree.Node("T");
        root.addSon(name());
        root.addSon(new Tree.Node("(", true));
        root.addSon(args());
        root.addSon(new Tree.Node(")", true));
        return root;
    }

    private static Tree.Node args() {
        if (random.nextBoolean()) {
            return new Tree.Node("eps", true);
        } else {
            Tree.Node root = new Tree.Node("ARGS");
            root.addSon(v());
            root.addSon(listV());
            return root;
        }
    }

    private static Tree.Node listV() {
        if (random.nextInt(4) < 1) {
            return new Tree.Node("eps", true);
        } else {
            Tree.Node root = new Tree.Node("LIST_V");
            root.addSon(new Tree.Node(";", true));
            root.addSon(v());
            root.addSon(listV());
            return root;
        }
    }

    private static Tree.Node s() {
        Tree.Node root = new Tree.Node("S");
        if (random.nextBoolean()) {
            root.addSon(new Tree.Node("function ", true));
            root.addSon(t());
            root.addSon(new Tree.Node(":", true));
            root.addSon(type());
            root.addSon(new Tree.Node(";", true));
        } else {
            root.addSon(new Tree.Node("procedure ", true));
            root.addSon(t());
            root.addSon(new Tree.Node(";", true));
        }
        return root;
    }

    public static Tree.Node generate() {
        return s();
    }

    public static void main(String[] args) throws IOException {
        //BufferedWriter bw = new BufferedWriter(new FileWriter("./src/tests/sample02"));
        for (int i = 0; i < 1_00; ++i) {
            Tree.Node root = s();
            System.out.println(Tree.expression(root));
        }
        //bw.close();
    }
}
