package expr;

import dealpart.Lexer;
import dealpart.Parser;

import java.util.ArrayList;

public class FuncFactor extends Factor {
    private final String func;
    private final Expr expr;

    public FuncFactor(String name, ArrayList<Factor> factors) {
        func = MakeFunc.useFunc(name,factors);
        Lexer lexer = new Lexer(func);
        Parser parser = new Parser(lexer);
        expr = parser.parseExpr();
    }

    @Override
    public  Poly toPoly() {
        return expr.toPoly();
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
