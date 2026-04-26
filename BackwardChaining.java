import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BackwardChaining {
    String[] connectivesSymbols = {"<=>", "=>", "~", "&", "|"};
    ArrayList<Symbol> expression = new ArrayList<>();
    String[] subExpression;
    String preString = "";
    boolean logic = false;

    public void readTell(String arg) {
        System.out.println("TELL: " + arg);
        subExpression = arg.split(";");
        for (String sub : subExpression) {
            String connective = returnConnectiveSymbol(sub);
            if (!connective.isEmpty()) {
                String[] prop = sub.split(Pattern.quote(connective), 2);
                if (prop.length == 2) {
                    addAtomicExpTwo(prop, connective);
                }
            }
        }
    }

    public void readAsk(String arg) {
        logic = false;
        preString = "";

        System.out.println("ASK: " + arg);
        arg = arg.trim();
        String result = printReverse(backwardChaining(arg)).toString();
        String feedBack = logic ? "YES: " : "NO: ";
        if (!result.isEmpty()) {
            System.out.println(feedBack + result + ", " + arg);
        } else {
            System.out.println(feedBack + arg);
        }
    }

    public String backwardChaining(String bcLogic) {
        for (Symbol sym : expression) {
            if (sym.getMap() != null && sym.getMap().containsValue(bcLogic)) {
                logic = true;

                Set<String> key = sym.getMap().entrySet().stream()
                        .filter(entry -> Objects.equals(entry.getValue(), bcLogic))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                String keyString = key.toString();
                keyString = keyString.substring(1, keyString.length() - 1).trim();

                if (!keyString.isEmpty()) {
                    preString = preString + keyString + " ";
                    backwardChaining(keyString);
                }
            }
        }
        return preString;
    }

    public void addAtomicExpTwo(String[] bcLogic, String con) {
        HashMap<String, String> bcLogics = new HashMap<>();
        bcLogics.put(bcLogic[0].trim(), bcLogic[1].trim());
        Symbol sym = new Symbol(con, returnConnectiveWord(con));
        sym.setMap(bcLogics);
        expression.add(sym);
    }

    public String returnConnectiveSymbol(String subExp) {
        for (String con : connectivesSymbols) {
            if (subExp.contains(con)) {
                return con;
            }
        }
        return "";
    }

    public String returnConnectiveWord(String con) {
        return switch (con) {
            case "~" -> "Negation";
            case "&" -> "AND";
            case "|" -> "OR";
            case "=>" -> "Implication";
            case "<=>" -> "Biconditional";
            default -> "";
        };
    }

    public StringBuilder printReverse(String reverse) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] str = reverse.trim().isEmpty() ? new String[0] : reverse.trim().split("\\s+");
        for (int i = str.length - 1; i >= 0; i--) {
            stringBuilder.append(str[i]);
            if (i > 0) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder;
    }
}
