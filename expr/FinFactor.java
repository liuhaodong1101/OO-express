package expr;

import java.math.BigInteger;
import java.util.HashMap;

public class FinFactor extends Factor {
    private final String choose;
    private final Factor factor;

    private final int exp;

    private final HashMap<Poly,Integer> finMap = new HashMap<>();

    @Override
    public Poly toPoly() {
        if (exp != 0) {
            finMap.put(factor.toPoly(),exp);
        }
        Mono mono;
        if (choose.equals("c")) {
            mono = new Mono(BigInteger.valueOf(1), 0, 0, 0, new HashMap<>(), finMap);
        }
        else {
            mono = new Mono(BigInteger.valueOf(1), 0, 0, 0, finMap, new HashMap<>());
        }
        Poly p = new Poly();
        p.findAndAdd(mono);
        return p;
    }

    @Override
    public String toString() {
        return toPoly().toString();
    }

    public FinFactor(Factor factor,int exp,String choose) {
        this.factor = factor;
        this.exp = exp;
        this.choose = choose;
    }
}
