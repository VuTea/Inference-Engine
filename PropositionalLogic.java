import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PropositionalLogic {
    String[] connectivesSymbols = new String[]{"<=>", "=>", "~", "&", "|"};
    ArrayList<Symbol> expression = new ArrayList<>();
    String[] subExpression;
    int count = 0;
    boolean logic = false;

    public void readTell(String intake) {
        System.out.println("TELL: " + intake);
        subExpression = intake.split(";");
        for (String conSym : subExpression) {
            String connective = returnConnectiveSymbol(conSym);
            if (connective.isEmpty()) {
                continue;
            }

            String[] pLogic = conSym.split(Pattern.quote(connective), 2);
            if (pLogic.length == 2) {
                addAtomicExpTwo(pLogic, connective);
            }
        }
    }

    public void readAsk(String arg) {
        logic = false;
        count = 0;

        System.out.println("ASK: " + arg);
        int result = processLogic(arg.trim(), new HashSet<>());
        String feedBack = logic ? "YES: " : "NO: ";
        System.out.println(feedBack + result);
    }

    public int processLogic(String pLogics, Set<String> visited) {
        if (visited.contains(pLogics)) {
            return count;
        }
        visited.add(pLogics);

        for (Symbol sym : expression) {
            boolean mapValue = sym.getMap().containsValue(pLogics);
            if (mapValue) {
                logic = true;

                Set<String> key = sym.getMap().entrySet().stream()
                        .filter(entry -> Objects.equals(entry.getValue(), pLogics))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

                String keyString = key.toString();
                int keyLength = keyString.length();
                keyString = keyString.substring(1, keyLength - 1).trim();
                if (!keyString.isEmpty()) {
                    count++;
                    processLogic(keyString, visited);
                }
            }
        }
        return count;
    }

    public void addAtomicExpTwo(String[] pLogic, String conSym) {
        HashMap<String, String> pLogics = new HashMap<>();
        String pLogic1 = pLogic[0].trim();
        String pLogic2 = pLogic[1].trim();

        pLogics.put(pLogic1, pLogic2);

        Symbol sym = new Symbol(conSym, returnConnectiveWord(conSym));
        sym.setMap(pLogics);
        expression.add(sym);
    }

    public String returnConnectiveSymbol(String subExp) {
        for (String conSym : connectivesSymbols) {
            if (subExp.contains(conSym)) {
                return conSym;
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
}
