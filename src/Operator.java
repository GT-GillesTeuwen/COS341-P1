public abstract class Operator extends AbstractExpression {

  private String symbol;
  protected AbstractExpression operandLeft;
  protected AbstractExpression operandRight;

  public Operator(String symbol) {
    this.symbol = symbol;
  }

  public abstract NFA interpret();

}