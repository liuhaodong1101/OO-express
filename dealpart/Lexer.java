package dealpart;

import java.math.BigInteger;

public class Lexer {
    private final String input;
    private int pos = 0;

    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    public int getPower() {
        if (pos + 1 >= input.length()) {
            return 1;
        }
        if (input.charAt(pos) == '*' && input.charAt(pos + 1) == '*') {
            pos = pos + 2;
            if (input.charAt(pos) == '+') {
                pos++;
            }
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                sb.append(input.charAt(pos));
                ++pos;
            }
            curToken = String.valueOf(input.charAt(pos - 1));
            BigInteger bigInteger = new BigInteger(sb.toString());
            return bigInteger.intValue();
        }
        else {
            return 1;
        }
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        curToken = String.valueOf(c);
        if ("()+-*xyz,".indexOf(c) != -1) {
            pos += 1;
        }
        else if ("fghABE".indexOf(c) != -1) {
            pos += 2;
        }
        else if ("sc".indexOf(c) != -1) {
            pos += 4;
        }
    }

    public String peek() {
        return this.curToken;
    }
}
