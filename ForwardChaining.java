import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ForwardChaining {
    String[] connectivesSymbols = {"<=>", "=>", "~", "&", "|"};
    ArrayList<Symbol> expression = new ArrayList<>();

    HashMap<String, Boolean> inferred = new HashMap<>();
    String[] subExpression;
    StringBuilder result = new StringBuilder();
    boolean logic = false;

    public void readTell(String arg) {
        System.out.println("TELL: " + arg);
        subExpression = arg.split(";");

        for (String sub : subExpression) {
            String connective = returnConnectiveSymbol(sub);
            if (!connective.isEmpty()) {
                addAtomicExp(sub, connective);
            } else {
                Symbol sym = new Symbol(sub.trim());
                expression.add(sym);
            }
        }
    }

    public boolean forwardChaining(String fcLogic, Set<String> visiting) {
        if (visiting.contains(fcLogic)) {
            return logic;
        }
        visiting.add(fcLogic);

        for (Symbol sym : expression) {
            if (sym.getMap() != null && sym.getMap().containsValue(fcLogic)) {
                logic = true;
                String keyToString = returnKey(sym, fcLogic);
                if (!keyToString.isEmpty()) {
                    if (!inferred.containsKey(keyToString)) {
                        result.append(keyToString).append(" ");
                        inferred.put(keyToString, true);
                    }
                    forwardChaining(keyToString, visiting);
                }
            }

            if (sym.fact != null && !inferred.containsKey(sym.fact)) {
                result.append(sym.fact).append(" ");
                inferred.put(sym.fact, true);
            }
        }
        return logic;
    }

    public String returnKey(Symbol sym, String logic) {
        Set<String> key = sym.getMap().entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), logic))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        String keyString = key.toString();
        return keyString.substring(1, keyString.length() - 1).trim();
    }

    public void readAsk(String arg) {
        logic = false;
        inferred.clear();
        result = new StringBuilder();

        String trimmedArg = arg.trim();
        System.out.println("ASK: " + trimmedArg);
        String feedBack = forwardChaining(trimmedArg, new HashSet<>()) ? "YES: " : "NO: ";

        String chain = printReverse(result.toString()).toString();
        if (!chain.isEmpty()) {
            System.out.println(feedBack + chain + ", " + trimmedArg);
        } else {
            System.out.println(feedBack + trimmedArg);
        }
    }

    public void addAtomicExp(String sub, String connective) {
        HashMap<String, String> fcLogics = new HashMap<>();
        String[] fcLogic = sub.split(Pattern.quote(connective), 2);
        if (fcLogic.length == 2) {
            fcLogics.put(fcLogic[0].trim(), fcLogic[1].trim());
            Symbol sym = new Symbol(connective, returnConnectiveWord(connective));
            sym.setMap(fcLogics);
            expression.add(sym);
        }
    }

    public String returnConnectiveSymbol(String subExp) {
        for (String con : connectivesSymbols) {
            if (subExp.contains(con)) {
                return con;
            }
        }
        return "";
    }

    public String returnConnectiveWord(String conSym) {
        return switch (conSym) {
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
