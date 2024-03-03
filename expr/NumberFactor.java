package expr;

import java.math.BigInteger;

public class NumberFactor extends Factor {
    private final BigInteger num;

    public NumberFactor(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    @Override
    public Poly toPoly() {
        Mono mono = new Mono(num,0,0,0);
        Poly p = new Poly();
        p.findAndAdd(mono);
        return p;
    }
}
