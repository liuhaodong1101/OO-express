package expr;

import dealpart.Lexer;
import dealpart.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static dealpart.InputHandler.deleteSpace;

public class MakeFunc {
    private static final HashMap<String,String> FUNC_MAP = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> PARA_MAP = new HashMap<>();

    public static int getNumberOfParas(String name) {
        return PARA_MAP.get(name).size();
    }

    public static void newFunc(String input) {
        String funcName = input.charAt(0) + "";
        FUNC_MAP.put(funcName,input.split("=")[1]);
        String define = input.split("=")[0];
        String paraString = define.substring(define.indexOf("(") + 1,define.indexOf(")"));
        String[] paraArray = paraString.split(",");
        ArrayList<String> paraList = new ArrayList<>();
        Collections.addAll(paraList, paraArray);
        PARA_MAP.put(funcName,paraList);
    }

    public static String useFunc(String funcName, ArrayList<Factor> factors) {
        String funcAble = FUNC_MAP.get(funcName);
        ArrayList<String> paraList = PARA_MAP.get(funcName);
        Lexer lexer = new Lexer(deleteSpace(funcAble));
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        funcAble = expr.toString();
        for (int j = 0;j < paraList.size();j++) {
            funcAble = funcAble.replace(paraList.get(j),(char)(112 + j) + "");
        }
        for (int j = 0;j < factors.size();j++) {
            funcAble = funcAble.replace((char)(112 + j) + "","(" + factors.get(j).toString() + ")");
        }
        return funcAble;
    }

}
