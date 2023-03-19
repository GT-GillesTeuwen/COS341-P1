public class TerminalExpression extends AbstractExpression {
    private String symbol;

    public TerminalExpression(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public NFA interpret() {
        return new NFA(symbol);
    }

}
