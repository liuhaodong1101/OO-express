package expr;

public class DiffFactor extends Factor {
    private final Expr expr;

    private final int power;

    private final int choice;

    public DiffFactor(Expr expr, int power, int choice) {
        this.expr = expr;
        this.power = power;
        this.choice = choice;
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }

    @Override
    public Poly toPoly() {
        return expr.toPoly().powerPoly(power).diff(choice);
    }
}
