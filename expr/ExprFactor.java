package expr;

public class ExprFactor extends Factor {
    private final Expr expr;
    private final int power;

    public ExprFactor(Expr expr, int power) {
        this.expr = expr;
        this.power = power;
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }

    @Override
    public Poly toPoly() {
        return expr.toPoly().powerPoly(power);
    }

}
