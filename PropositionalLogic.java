import java.util.*;
import java.util.stream.Collectors;

public class PropositionalLogic {
    String[] connectivesSymbols = new String[]{"(", ")", "~", "&", "|", "=>", "<=>"};
     ArrayList<Symbol> expression =new ArrayList<>();
    String[] subExpression;
    int count = 0;
    boolean logic = false;

    public void readTell(String intake)
    {
        String[] pLogic;
        System.out.println("TELL: "+ intake);
        subExpression = intake.split(";");
        for (String conSym:subExpression)
        {
            if(numberOfConnectives(conSym) == 1)
            {
                addAtomicExpOne(conSym);
            }
            else if(numberOfConnectives(conSym) == 2)
            {
                if(conSym.contains("=>"))
                {
                    pLogic = conSym.split("=>");
                    addAtomicExpTwo(pLogic,"=>");
                }
                else if(conSym.contains("<=>"))
                {
                    pLogic = conSym.split("<=>");
                    addAtomicExpTwo(pLogic, "<=>");
                }
            }
        }
    }

    public void readAsk(String arg)
    {
        System.out.println("ASK: "+ arg);
        int result = processLogic(arg);
        String feedBack = logic? "YES: ":"NO: ";
        System.out.println(feedBack + result);
    }

    public int processLogic(String pLogics)
    {
        for (Symbol sym:expression)
        {
            boolean mapValue = sym.getMap().containsValue(pLogics);
            if(mapValue)
            {
                logic = true;

                Set<String> key = sym.getMap().entrySet().stream().filter(entry->Objects.equals(entry.getValue(),pLogics)).map(Map.Entry::getKey).collect(Collectors.toSet());

                String keyString = key.toString();

                int keyLength = keyString.length();
                keyString = keyString.substring(1, keyLength - 1);
                if (!keyString.equals(" "))
                {
                   count++;
                   processLogic(keyString.trim());
                }
            }
        }
        return count;
    }

    public int numberOfConnectives(String subExp)
    {
        int count = 0;
        int i;

        String[] con = connectivesSymbols;

        for(i = 0; i < con.length; i++)
        {
            if(subExp.contains(con[i]))
            {
                count++;
            }
        }
        return count;
    }

    public void addAtomicExpOne(String sub)
    {
        HashMap<String, String> pLogics = new HashMap<>();
        String[] pLogic = sub.split(returnConnectiveSymbol(sub));
        String pLogic1 = pLogic[0].trim();
        String pLogic2 = pLogic[1].trim();
        pLogics.put(pLogic1, pLogic2);
        Symbol sym = new Symbol(returnConnectiveSymbol(sub), returnConnectiveWord(returnConnectiveSymbol(sub)));
        sym.setMap(pLogics);
        expression.add(sym);
    }

    public void addAtomicExpTwo(String[] pLogic,String conSym)
    {
        HashMap<String, String> pLogics = new HashMap<>();
        String pLogic1 = pLogic[0].trim();
        String pLogic2 = pLogic[1].trim();

        pLogics.put(pLogic1, pLogic2);

        Symbol sym = new Symbol(conSym,returnConnectiveWord(conSym));
        sym.setMap(pLogics);
        expression.add(sym);
    }

    public String returnConnectiveSymbol(String subExp)
    {
        for(String conSym:connectivesSymbols)
        {
            if(subExp.contains(conSym))
            {
                return conSym;
            }
        }
        return "";
    }

    public String returnConnectiveWord(String conSym)
    {
        return switch (conSym) {
            case "(" -> "Parenthesis";
            case "~" -> "Negation";
            case "&" -> "AND";
            case "||" -> "OR";
            case "=>" -> "Implication";
            case "<=>" -> "Biconditional";
            default -> "";
        };
    }
}
