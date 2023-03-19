public class EmptyOperator extends Operator {

    public EmptyOperator() {
        super("-");
        // TODO Auto-generated constructor stub
    }

    @Override
    public NFA interpret() {
        return operandLeft.interpret();
    }

}