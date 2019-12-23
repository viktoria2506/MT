import parser.Parser;
import parser.Tree;
import java.io.IOException;

public class Test {
    private static final String path = "./src/main/java/tests/";
    private static Parser parser = new Parser();


    public static void main(String[] args) throws IOException {
        Tree.print(parser.parse("function f ( n , a : integer; var x : string) : integer;"), "./tree_images/test0");

        Tree.print(parser.parse("procedure f;"), "./tree_images/test1");
        while (n) {
            if (n & 1) {
                count++;
            }
            n = (n >> 1);
        }

    }
}
