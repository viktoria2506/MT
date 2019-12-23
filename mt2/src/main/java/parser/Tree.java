package parser;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class Tree {
    public static class Node {
        private CharSequence value;
        private List<Node> sons = new ArrayList<>();
        private boolean isTerm = false;

        public Node(CharSequence value) {
            this.value = value;
        }

        public Node(CharSequence value, boolean isTerm) {
            this.value = value;
            this.isTerm = isTerm;
        }

        public void addSon(Node son) {
            sons.add(son);
        }

        public CharSequence value() {
            return value;
        }

        public boolean isTerm() {
            return isTerm;
        }

        public List<Node> sons() {
            return sons;
        }
    }

    public static CharSequence expression(Node v) {
        if (v.isTerm()) return v.value().equals("eps") ? "" : v.value();

        StringBuilder sb = new StringBuilder();
        for (Node son : v.sons()) {
            sb.append(expression(son));
        }
        return sb;
    }


    private static int cur = 0;

    private static MutableNode node(Node v) {
        MutableNode root = mutNode(Integer.toString(cur++));
        root.add("label", v.value());
        if (v.isTerm()) {
            root.add(Color.RED);
        } else {
            root.add(Color.BLACK);
        }
        for (Node son : v.sons()) {
            root.addLink(node(son));
        }
        return root;
    }


    public static void print(Node v, String filename) throws IOException {
        MutableGraph root = mutGraph("Tree").setDirected(true).add(node(v));
        Graphviz.fromGraph(root).width(1280).render(Format.PNG).toFile(new File(filename));
    }
}

