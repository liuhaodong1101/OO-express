package dealpart;

import expr.ExprFactor;
import expr.DiffFactor;
import expr.FuncFactor;
import expr.FinFactor;
import expr.MakeFunc;
import expr.Factor;
import expr.Expr;
import expr.NumberFactor;
import expr.PowerFactor;
import expr.Term;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        int sign = 1;
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            sign = -1;
            lexer.next();
        }
        expr.addTerm(parseTerm(sign));
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("+")) {
                lexer.next();
                expr.addTerm(parseTerm(1));
            }
            else {
                lexer.next();
                expr.addTerm(parseTerm(-1));
            }
        }
        return expr;
    }

    public Term parseTerm(int sign) {
        int sign1 = 1;
        if (lexer.peek().equals("-")) {
            sign1 = -1;
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        int sign2;
        if (sign == sign1) {
            sign2 = 1;
        } else {
            sign2 = -1;
        }
        Term term = new Term(sign2);
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public PowerFactor parsePowerFactor() {
        PowerFactor powerFactor;
        if (lexer.peek().equals("x")) {
            powerFactor = new PowerFactor(lexer.getPower(), 0, 0);
        }
        else if (lexer.peek().equals("y")) {
            powerFactor = new PowerFactor(0,lexer.getPower(),  0);
        }
        else {
            powerFactor = new PowerFactor(0,0, lexer.getPower());
        }
        lexer.next();
        return powerFactor;
    }

    public NumberFactor parseNumberFactor() {
        BigInteger num;
        if (lexer.peek().equals("+")) {
            lexer.next();
            num = new BigInteger(lexer.getNumber());
            lexer.next();
        }
        else if (lexer.peek().equals("-")) {
            lexer.next();
            num = new BigInteger("-" + lexer.getNumber());
            lexer.next();
        }
        else {
            num = new BigInteger(lexer.getNumber());
            lexer.next();
        }
        return new NumberFactor(num);
    }

    public ExprFactor parseExprFactor() {
        lexer.next();
        Expr expr = parseExpr();
        ExprFactor exprFactor = new ExprFactor(expr, lexer.getPower());
        lexer.next();
        return exprFactor;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            return parseExprFactor();
        }
        else if ("sc".contains(lexer.peek())) {
            return parseFinFactor(lexer.peek());
        }
        else if ("fgh".contains(lexer.peek())) {
            return parseFuncFactor(lexer.peek());
        }
        else if ("ABE".contains(lexer.peek())) {
            return parseDiffFactor(lexer.peek());
        }
        else if ("xyz".contains(lexer.peek())) {
            return parsePowerFactor();
        }
        else {
            return parseNumberFactor();
        }
    }

    private Factor parseDiffFactor(String peek) {
        int choice;
        if (peek.equals("A")) {
            choice = 1;
        } else if (peek.equals("B")) {
            choice = 2;
        } else {
            choice = 3;
        }
        lexer.next();
        Expr expr = parseExpr();
        DiffFactor diffFactor = new DiffFactor(expr, lexer.getPower(), choice);
        lexer.next();
        return diffFactor;
    }

    private Factor parseFuncFactor(String name) {
        ArrayList<Factor> factors = new ArrayList<>();
        int num = MakeFunc.getNumberOfParas(name);
        for (int i = 0;i < num;i++) {
            lexer.next();
            factors.add(parseFactor());
        }
        lexer.next();
        return new FuncFactor(name,factors);
    }

    public FinFactor parseFinFactor(String choose) {
        lexer.next();
        Factor factor = parseFactor();
        FinFactor finFactor = new FinFactor(factor, lexer.getPower(), choose);
        lexer.next();
        return finFactor;
    }

}
