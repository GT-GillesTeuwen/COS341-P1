public class PlusOperator extends Operator{
    
    public PlusOperator() {
        super("+");
        // TODO Auto-generated constructor stub
    }

    @Override
    public NFA interpret() {
        NFA cur = operandLeft.interpret();
        cur.manageStarOperator();
        return cur;
    }

}
