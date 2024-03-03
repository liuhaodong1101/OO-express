package expr;

import java.math.BigInteger;

public class PowerFactor extends Factor {
    private final int expX;
    private final int expY;
    private final int expZ;

    public PowerFactor(int expX, int expY, int expZ) {
        this.expX = expX;
        this.expY = expY;
        this.expZ = expZ;
    }

    @Override
    public Poly toPoly() {
        Mono mono = new Mono(BigInteger.valueOf(1), expX, expY, expZ);
        Poly p = new Poly();
        p.findAndAdd(mono);
        return p;
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}
