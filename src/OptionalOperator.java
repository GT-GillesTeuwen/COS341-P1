public class OptionalOperator extends Operator{
    public OptionalOperator() {
        super("?");
        // TODO Auto-generated constructor stub
        
    }

    @Override
    public NFA interpret() {
        NFA cur = operandLeft.interpret();
        cur.manageOptionalOperator();
        return cur;
        
    }
}
