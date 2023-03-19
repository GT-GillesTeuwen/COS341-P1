public abstract class UnaryOperator extends Operator {

    protected AbstractExpression operand;

    public UnaryOperator(String symbol) {
        super(symbol);
    }

    @Override
    public abstract NFA interpret();

}
