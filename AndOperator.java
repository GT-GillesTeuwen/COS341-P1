public class AndOperator extends Operator {

    public AndOperator() {
        super(".");
        // TODO Auto-generated constructor stub
    }

    @Override
    public NFA interpret() {
        NFA left = operandLeft.interpret();
        NFA right = operandRight.interpret();
        right.manageAndOperator(left);
        return right;
    }

}
