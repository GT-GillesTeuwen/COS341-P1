public class Main {
  public static void main(String[] args) {
    RegularExpression r = new RegularExpression("a.b*");
    r.insertBrackets();
    AbstractExpression a = r.buildTree();
    NFA nfa = a.interpret();
    
    DFA d=new DFA(nfa);
  }

}