package cel.cea.utils;

public enum TransitionType {
    WHITE("○"),
    BLACK("●");

    private String symbol;

    TransitionType(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }
}
