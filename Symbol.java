import java.util.HashMap;

public class Symbol
{
    public String symbol;
    public String symbolName;
    HashMap<String, String> map;
    public String fact;
    public Symbol(String symbol, String symbolName)
    {
        this.symbol = symbol;
        this.symbolName = symbolName;
    }

    public Symbol(String facts) {
        this.fact = facts;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "symbol ='" + symbol + '\'' +
                ", symbolName ='" + symbolName + '\'' +
                ", map =" + map +
                '}';
    }
}
