public class OrOperator extends Operator {

    public OrOperator() {
        super("+");
        // TODO Auto-generated constructor stub
    }

    @Override
    public NFA interpret() {
        NFA left = operandLeft.interpret();
        NFA right = operandRight.interpret();
        right.manageOrOperator(left);
        return right;
    }

}
