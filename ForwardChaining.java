import java.util.*;
import java.util.stream.Collectors;
public class ForwardChaining
{
    String[] connectivesSymbols = {"(",")","~","&","|","=>","<=>"};
    ArrayList<Symbol> expression = new ArrayList<>();

    HashMap<String, Boolean> inferred = new HashMap<>();
    String[] subExpression;
    String preString="";
    StringBuilder result = new StringBuilder();
    boolean logic = false;

    public void readTell(String arg)
    {
        System.out.println("TELL: "+ arg);
        subExpression = arg.split(";");

        for (String sub:subExpression)
        {
            if(numberOfConnectives(sub) == 1)
            {
                String[] prop = addAtomicExp(sub);
            }
            else if(numberOfConnectives(sub) == 2)
            {
                if(sub.contains("=>"))
                {
                    String[] prop = sub.split("=>");
                }
                else if(sub.contains("<=>"))
                {
                    String[] prop = sub.split("<=>");
                }
            }
            else if(numberOfConnectives(sub) == 0)
            {
                Symbol sym = new Symbol(sub.trim());
                expression.add(sym);
            }
        }
    }

    public boolean ForwardChaining(String  fcLogic){

        for(Symbol sym:expression)
        {

            if (sym.getMap() != null && sym.getMap().containsValue(fcLogic))
            {
                logic = true;
                String keyToString = returnKey(sym, fcLogic);
                if (!keyToString.equals("")) {
                    preString = preString + (keyToString + " ");
                    result.append(keyToString).append(" ");
                    inferred.put(keyToString, true);
                    ForwardChaining(keyToString);
                }
            }

            if (sym.fact != null)
            {
                if (!inferred.containsKey(sym.fact))
                {
                    result.append(sym.fact).append(" ");
                    inferred.put(sym.fact, true);
                }
            }
        }
        return logic;
    }

    public String returnKey(Symbol sym, String logic)
    {
        Set<String> key = sym.getMap().entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), logic)).map(Map.Entry::getKey).collect(Collectors.toSet());
        String keyString = key.toString();
        keyString = keyString.substring(1, keyString.length() - 1);
        return keyString;
    }

    public void readAsk(String arg){
        System.out.println("ASK: "+ arg);
        String feedBack = ForwardChaining(arg.trim()) ? "YES: ":"NO: ";

        System.out.println(feedBack + printReverse(result.toString()) + arg);
    }

    public String[] addAtomicExp(String sub)
    {
        HashMap<String, String> fcLogics = new HashMap<>();
        String[] fcLogic = sub.split(returnConnectiveSymbol(sub));
        fcLogics.put(fcLogic[0].trim(), fcLogic[1].trim());
        Symbol sym = new Symbol(returnConnectiveSymbol(sub),returnConnectiveWord(returnConnectiveSymbol(sub)));
        sym.setMap(fcLogics);
        expression.add(sym);
        return fcLogic;
    }

    public String returnConnectiveSymbol(String subExp)
    {
        for(String con:connectivesSymbols)
        {
            if(subExp.contains(con))
            {
                return con;
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
            case "|" -> "OR";
            case "=>" -> "Implication";
            case "<=>" -> "Biconditional";
            default -> "";
        };
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
    public StringBuilder printReverse(String reverse)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String[] str = reverse.split(" ");
        for(int i = str.length - 1; i >= 0; i--)
        {
            stringBuilder.append(str[i]);
            stringBuilder.append(", ");
        }
        return stringBuilder;
    }
}
