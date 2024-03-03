package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Poly {
    private static final  Mono ZERO = new Mono(BigInteger.valueOf(0),0,0,0);
    private final ArrayList<Mono> monos;

    public void simplify() {
        Iterator<Mono> iter = monos.iterator();
        while (iter.hasNext()) {
            Mono mono = iter.next();
            if (mono.equals(ZERO) && monos.size() > 1) {
                iter.remove();
            }
        }
    }

    public ArrayList<Mono> getMonos() {
        return monos;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Poly)) {
            return false;
        }
        Poly poly = (Poly) o;
        HashSet<Mono> monos1 = new HashSet<>(monos);
        HashSet<Mono> monos2 = new HashSet<>(poly.getMonos());
        if (monos1.size() != monos2.size()) {
            return false;
        }
        for (Mono obj : monos1) {

            if (!monos2.contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int i = 0;
        for (Mono mono: monos) {
            i = i + mono.hashCode();
        }
        return i;
    }

    public Poly() {
        this.monos = new ArrayList<>();
    }

    public void reverse() {
        for (Mono mono:this.monos) {
            mono.setCoe(mono.getCoe().multiply(BigInteger.valueOf(-1)));
        }
    }

    public Poly powerPoly(int power) {
        Poly tempPoly = new Poly();
        if (power == 0) {
            Mono mono = new Mono(BigInteger.valueOf(1),0,0,0);
            tempPoly.findAndAdd(mono);
        }
        else {
            tempPoly = this;
            for (int i = 1;i < power;i++) {
                tempPoly = tempPoly.mulPoly(this);
            }
        }
        tempPoly.simplify();
        return tempPoly;

    }

    public Poly mulPoly(Poly p) {
        Poly tempPoly = new Poly();
        for (Mono m1:this.monos) {
            for (Mono m2:p.monos) {
                tempPoly.findAndAdd(m1.mulMono(m2));
            }
        }
        tempPoly.simplify();
        return tempPoly;
    }

    public Poly(Mono mono) {
        this.monos = new ArrayList<>();
        this.monos.add(mono);
    }

    public Poly addPoly(Poly p) {
        Poly tempPoly = new Poly();
        for (Mono mono : this.monos) {
            tempPoly.findAndAdd(mono);
        }
        for (Mono mono : p.monos) {
            tempPoly.findAndAdd(mono);
        }
        tempPoly.simplify();
        return tempPoly;
    }

    public void findAndAdd(Mono mono) {
        for (int i = 0;i < monos.size();i++) {
            Mono m = monos.get(i);
            if (m.weakEqual(mono)) {
                m.setCoe(m.getCoe().add(mono.getCoe()));
                if (m.getCoe().equals(BigInteger.valueOf(0)) && monos.size() > 1) {
                    i--;
                    monos.remove(m);
                }
                return;
            }
        }
        if (!(mono.getCoe().equals(BigInteger.valueOf(0)) && this.monos.size() > 0)) {
            this.monos.add(mono);
        }
    }

    public Poly diff(int choice) {
        Poly poly = new Poly(new Mono(BigInteger.ZERO,0,0,0));

        for (int i = 0; i <  this.monos.size(); i++) {
            Poly poly1 = this.monos.get(i).diff(choice);
            poly = poly.addPoly(poly1);
        }
        return poly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.monos.get(0));
        for (int i = 1; i < this.monos.size(); i++) {
            if (!(this.monos.get(i).getCoe().equals(BigInteger.valueOf(0)) && sb.length() > 0)) {
                sb.append("+");
                sb.append(this.monos.get(i));
            }
        }
        return sb.toString();
    }

}
