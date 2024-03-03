package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Mono {
    private static final Poly ZERO_POLY = new NumberFactor(BigInteger.ZERO).toPoly();
    private final BigInteger zero = BigInteger.valueOf(0);
    private final BigInteger one = BigInteger.valueOf(1);
    private final BigInteger negativeOne = BigInteger.valueOf(-1);
    private BigInteger coe;
    private final int expX;
    private final int expY;
    private final int expZ;
    private final HashMap<Poly,Integer> sinMap;
    private final HashMap<Poly,Integer> cosMap;

    public Mono(BigInteger coe, int expX, int expY, int expZ) {
        this.coe = coe;
        this.expX = expX;
        this.expY = expY;
        this.expZ = expZ;
        sinMap = new HashMap<>();
        cosMap = new HashMap<>();
    }

    public Mono(BigInteger coe, int expX, int expY, int expZ,
                HashMap<Poly,Integer> sinMap,HashMap<Poly,Integer> cosMap) {
        this.coe = coe;
        this.expX = expX;
        this.expY = expY;
        this.expZ = expZ;
        this.sinMap = new HashMap<>(sinMap);
        this.cosMap = new HashMap<>(cosMap);
        simplify();
    }

    public HashMap<Poly,Integer> mulSinOrCos(HashMap<Poly,Integer> map1,HashMap<Poly,Integer> map2)
    {
        Integer integer;
        Integer integer2;
        HashMap<Poly,Integer> map = new HashMap<>(map1);
        for (Poly p : map2.keySet()) {
            integer2 = map2.get(p);
            if (map.containsKey(p)) {
                integer = map.get(p);
                map.put(p,integer + integer2);
            }
            else {
                map.put(p,integer2);
            }
        }
        return map;
    }

    public Mono mulMono(Mono mono) {
        BigInteger newCoe = coe.multiply(mono.getCoe());
        int newExpX = expX + mono.getExpX();
        int newExpY = expY + mono.getExpY();
        int newExpZ = expZ + mono.getExpZ();
        HashMap<Poly,Integer> newSinMap = mulSinOrCos(sinMap,mono.sinMap);
        HashMap<Poly,Integer> newCosMap = mulSinOrCos(cosMap,mono.cosMap);
        Mono mono1 = new Mono(newCoe,newExpX,newExpY,newExpZ,newSinMap,newCosMap);
        mono1.simplify();
        return mono1;
    }

    public BigInteger getCoe() {
        return coe;

    }

    public int getExpX() {
        return expX;

    }

    public int getExpY() {
        return expY;

    }

    public int getExpZ() {
        return expZ;

    }

    public int isZero(int num) {
        if (num == 0) {
            return 0;
        }
        else {
            return 1;
        }

    }

    public int getComplex() {
        int complex = 0;
        if (coe.equals(zero)) {
            return 0;
        } else if (!(coe.equals(one))) {
            complex++;
        }
        complex += isZero(expX) + isZero(expY) + isZero(expZ) +
                isZero(sinMap.size()) + isZero(cosMap.size());
        if (complex == 1) {
            if (sinMap.size() == 1) {
                for (Poly p: sinMap.keySet()) {
                    if (sinMap.get(p) > 1) {
                        complex++;
                    }
                }
            }
            if (cosMap.size() == 1) {
                for (Poly p: cosMap.keySet()) {
                    if (cosMap.get(p) > 1) {
                        complex++;
                    }
                }
            }
        }
        return complex;

    }

    public void simplify() {
        Iterator<Map.Entry<Poly, Integer>> iter = sinMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Poly, Integer> entry = iter.next();
            if (entry.getValue() == 0) {
                iter.remove();
            }
            else if (entry.getKey().equals(ZERO_POLY)) {
                this.coe = zero;
                iter.remove();
            }
        }
        iter = cosMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Poly, Integer> entry = iter.next();
            if (entry.getValue() == 0) {
                iter.remove();
            }
            else if (entry.getKey().equals(ZERO_POLY)) {
                iter.remove();
            }
        }

    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;

    }

    public void printFin(StringBuilder sb, HashMap<Poly,Integer> finMap, int choose) {
        if (finMap.size() == 0) {
            return;
        }
        if (!(sb.length() == 0 || (sb.length() == 1 && sb.toString().equals("-")))) {
            sb.append("*");
        }
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        finMap.forEach((poly, integer) -> {
            flag.set(true);
            if (choose == 0) {
                if (poly.getMonos().size() == 1 && poly.getMonos().get(0).getComplex() <= 1) {
                    sb.append("sin(");
                    sb.append(poly);
                    sb.append(")");
                } else {
                    sb.append("sin((");
                    sb.append(poly);
                    sb.append("))");
                }
            }
            else {
                if (poly.getMonos().size() == 1 && poly.getMonos().get(0).getComplex() <= 1) {
                    sb.append("cos(");
                    sb.append(poly);
                    sb.append(")");
                } else {
                    sb.append("cos((");
                    sb.append(poly);
                    sb.append("))");
                }
            }
            if (integer > 1) {
                sb.append("**").append(integer);
            }
            sb.append("*");
        });
        if (flag.get()) {
            sb.deleteCharAt(sb.length() - 1);
        }

    }

    public void printExp(StringBuilder sb, int exp, char c) {
        if (exp == 0) {
            return;
        }
        if (!(sb.length() == 0 || (sb.length() == 1 && sb.toString().equals("-")))) {
            sb.append("*");
        }
        if (exp == 1) {
            sb.append(c);
        }
        else {
            sb.append(c).append("**").append(exp);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (coe.equals(zero) || (expX == 0 && expY == 0 && expZ == 0 &&
                sinMap.size() == 0 && cosMap.size() == 0)) {
            sb.append(coe);
            return sb.toString();
        }
        if (coe.equals(negativeOne)) {
            sb.append("-");
        }
        else if (!coe.equals(one)) {
            sb.append(coe);
        }
        printExp(sb, expX,'x');
        printExp(sb, expY,'y');
        printExp(sb, expZ,'z');
        printFin(sb,sinMap,0);
        printFin(sb,cosMap,1);
        return sb.toString();
    }

    public boolean areMapsEqual(HashMap<Poly, Integer> map1, HashMap<Poly, Integer> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Poly key : map1.keySet()) {
            if (!map2.containsKey(key) || !map1.get(key).equals(map2.get(key))) {
                return false;
            }
        }
        return true;

    }

    public boolean weakEqual(Mono mono) {
        if ((this.getCoe().equals(mono.getCoe())) &&
                (this.getCoe().equals(BigInteger.valueOf(0)))) {
            return true;
        }
        if (this.expX == mono.expX && this.expY == mono.expY && this.expZ == mono.expZ) {
            if (!areMapsEqual(this.sinMap, mono.sinMap)) {
                return false;
            }
            return areMapsEqual(this.cosMap, mono.cosMap);
        }
        else {
            return false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mono)) {
            return false;
        }
        Mono mono = (Mono) o;
        if (!this.getCoe().equals(mono.getCoe())) {
            return false;
        }
        else {
            return weakEqual(mono);
        }

    }

    @Override
    public int hashCode() {
        int j = 0;
        for (Poly poly :sinMap.keySet()) {
            j += poly.hashCode();
        }
        for (Poly poly :cosMap.keySet()) {
            j += poly.hashCode();
        }
        j = coe.intValue() * (7 * expX + 11 * expY + 13 * expZ + j);
        return j;

    }

    public Poly diff(int choice) {
        Poly finalDiffPoly = new Poly();
        if (expX != 0 && choice == 1) {
            Mono mono = new Mono(this.getCoe().multiply(BigInteger.valueOf(this.expX)),
                    this.expX - 1,this.expY,this.expZ,sinMap,cosMap);
            finalDiffPoly.findAndAdd(mono);
        }

        if (expY != 0 && choice == 2) {
            Mono mono = new Mono(this.getCoe().multiply(BigInteger.valueOf(this.expY)),
                    this.expX,this.expY - 1,this.expZ,sinMap,cosMap);
            finalDiffPoly.findAndAdd(mono);
        }

        if (expZ != 0 && choice == 3) {
            Mono mono = new Mono(this.getCoe().multiply(BigInteger.valueOf(this.expZ)),
                    this.expX,this.expY,this.expZ - 1,sinMap,cosMap);
            finalDiffPoly.findAndAdd(mono);
        }

        Iterator<Map.Entry<Poly, Integer>> iter = sinMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Poly, Integer> entry = iter.next();
            if (entry.getValue() != 0) {
                HashMap<Poly,Integer> newSinMap = new HashMap<>(sinMap);
                newSinMap.put(entry.getKey(),entry.getValue() - 1);
                Mono originPart = new Mono(this.getCoe().
                        multiply(BigInteger.valueOf(entry.getValue())),
                        this.expX,this.expY,this.expZ, newSinMap,cosMap);//part1
                originPart.simplify();
                HashMap<Poly,Integer> newCosMap = new HashMap<>();
                newCosMap.put(entry.getKey(),1);
                Mono cosPart = new Mono(one, 0,0,0,new HashMap<>(), newCosMap);//part2
                Poly polyDiffPart = entry.getKey().diff(choice);//part3
                Poly finalSinPoly = polyDiffPart.
                        mulPoly(new Poly(cosPart)).mulPoly(new Poly(originPart));
                finalDiffPoly = finalDiffPoly.addPoly(finalSinPoly);
            }
        }
        iter = cosMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Poly, Integer> entry = iter.next();
            if (entry.getValue() != 0) {
                HashMap<Poly,Integer> newCosMap = new HashMap<>(cosMap);
                newCosMap.put(entry.getKey(),entry.getValue() - 1);
                Mono originPart = new Mono(this.getCoe().
                        multiply(BigInteger.valueOf(entry.getValue())),
                        this.expX,this.expY,this.expZ, sinMap,newCosMap);//part1
                originPart.simplify();
                HashMap<Poly,Integer> newSinMap = new HashMap<>();
                newSinMap.put(entry.getKey(),1);
                Mono sinPart = new Mono(negativeOne, 0,0,0,newSinMap, new HashMap<>());//part2
                Poly polyDiffPart = entry.getKey().diff(choice);//part3
                Poly finalCosPoly = polyDiffPart.
                        mulPoly(new Poly(sinPart)).mulPoly(new Poly(originPart));
                finalDiffPoly = finalDiffPoly.addPoly(finalCosPoly);
            }
        }
        return finalDiffPoly;
    }
}
