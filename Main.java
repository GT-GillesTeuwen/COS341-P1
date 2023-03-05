public class Main {
  public static void main(String[] args) {
    RegularExpression r = new RegularExpression("(ab)*|c*");
    r.insertBrackets();
    AbstractExpression a = r.buildTree();
    NFA nfa = a.interpret();
    System.out.println("a");
  }

}