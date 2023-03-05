public class StarOperator extends Operator {

    public StarOperator() {
        super("*");
        // TODO Auto-generated constructor stub
    }

    @Override
    public NFA interpret() {
        NFA cur = operandLeft.interpret();
        cur.manageStarOperator();
        return cur;
    }

}
