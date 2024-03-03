package dealpart;

import expr.MakeFunc;
import expr.Expr;

import java.util.Scanner;

public class InputHandler {

    public static void  inPutHandler() {
        Scanner scanner = new Scanner(System.in);
        String string = deleteSpace(scanner.nextLine());
        int n = Integer.parseInt(string);
        for (int i = 0; i < n; i++) {
            MakeFunc.newFunc(deleteSpace(scanner.nextLine()));
        }
        Lexer lexer = new Lexer(deleteSpace(scanner.nextLine()));
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        System.out.println(expr.toString().replace("+-","-")
                .replace("++","+")
                .replace("-+","-")
                .replace("--","+"));
    }

    public static String deleteSpace(String input) {
        return input.replaceAll("[ \t]", "").replace("dx","A").replace("dy","B").replace("dz","E");
    }
}
