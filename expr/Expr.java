package expr;

import java.util.HashSet;
import java.util.Iterator;

public class Expr implements ToPolyAble {
    private final HashSet<Term> terms;

    public Expr() {
        this.terms = new HashSet<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String toString() {
        return this.toPoly().toString();
    }

    @Override
    public Poly toPoly() {
        Iterator<Term> iter = terms.iterator();
        Poly p = new Poly();
        while (iter.hasNext()) {
            p = p.addPoly(iter.next().toPoly());
        }
        return p;
    }
}
