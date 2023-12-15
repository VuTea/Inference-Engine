import java.util.*;
import java.util.stream.Collectors;

public class BackwardChaining {
    String[] connectivesSymbols = {"(",")","~","&","||","=>","<=>"};
    ArrayList<Symbol> expression = new ArrayList<>();
    String[] subExpression;
    String preString="";
    boolean logic = false;

    public void readTell(String arg)
    {
        System.out.println("TELL: "+ arg);
        subExpression = arg.split(";");
        for (String sub:subExpression)
        {
            if(numberOfConnectives(sub) == 1)
            {
                addAtomicExpOne(sub);
            }
            else if(numberOfConnectives(sub) == 2)
            {
                if(sub.contains("=>"))
                {
                    String[] prop = sub.split("=>");
                    addAtomicExpTwo(prop,"=>");
                }
                else if(sub.contains("<=>"))
                {
                    String[] prop = sub.split("<=>");
                    addAtomicExpTwo(prop, "<=>");
                }
            }
        }
    }

    public void readAsk(String arg)
    {
        System.out.println("ASK: "+ arg);
        arg = arg.trim();
        String result = printReverse(BackwardChaining(arg)) + arg;
        String feedBack = logic? "YES: ":"NO: ";
        System.out.println(feedBack + result);
    }

    public String BackwardChaining(String bcLogic)
    {
        for (Symbol sym: expression)
        {
            if(sym.getMap() != null && sym.getMap().containsValue(bcLogic))
            {
                logic = true;

                Set<String> key = sym.getMap().entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), bcLogic)).map(Map.Entry::getKey).collect(Collectors.toSet());
                String keyString = key.toString();
                keyString = keyString.substring(1, keyString.length() - 1);

                if(!keyString.equals(""))
                {
                    preString = preString + (keyString + " ");
                    BackwardChaining(keyString);
                }
            }
        }
        return preString;
    }

    public int numberOfConnectives(String subExp) //this function returns the number fo connectives in a statement
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

    public void addAtomicExpOne(String sub) //this function handles statements with one connective (sub)
    {
        HashMap<String, String> bcLogics = new HashMap<>();
        String[] bcLogic = sub.split(returnConnectiveSymbol(sub));
        bcLogics.put(bcLogic[0].trim(),bcLogic[1].trim());
        Symbol sym = new Symbol(returnConnectiveSymbol(sub),returnConnectiveWord(returnConnectiveSymbol(sub)));
        sym.setMap(bcLogics);
        expression.add(sym);
    }

    public void addAtomicExpTwo(String[] bcLogic, String con) //this function handles statements with two connectives (bcLogic and con)
    {
        HashMap<String, String> bcLogics = new HashMap<>();
        bcLogics.put(bcLogic[0].trim(),bcLogic[1].trim());
        Symbol sym = new Symbol(con,returnConnectiveWord(con));
        sym.setMap(bcLogics);
        expression.add(sym);
    }

    public String returnConnectiveSymbol(String subExp) //this function returns the connective symbols
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

    public String returnConnectiveWord(String con) //this function returns the passed connective symbols
    {
        return switch (con) {
            case "(" -> "Parenthesis";
            case "~" -> "Negation";
            case "&" -> "AND";
            case "|" -> "OR";
            case "=>" -> "Implication";
            case "<=>" -> "Biconditional";
            default -> "";
        };
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
