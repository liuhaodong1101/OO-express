package expr;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

public class Term {
    private final HashSet<Factor> factors;
    private final int sign;

    public Term(int sign) {
        this.sign = sign;
        this.factors = new HashSet<>();
    }

    public Poly toPoly() {
        Iterator<Factor> iter = factors.iterator();
        Mono mono = new Mono(BigInteger.valueOf(1),0,0,0);
        Poly p = new Poly();
        p.findAndAdd(mono);
        while (iter.hasNext()) {
            Factor factor = iter.next();
            p = p.mulPoly(factor.toPoly());
        }
        if (sign == -1) {
            p.reverse();
        }
        return p;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public String toString() {
        return this.toPoly().toString();
    }

}
