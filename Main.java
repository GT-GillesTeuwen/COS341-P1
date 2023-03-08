import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) {
    RegularExpression r = new RegularExpression("3+");
    r.insertBrackets();
    AbstractExpression a = r.buildTree();
    NFA nfa = a.interpret();
    DFA d=new DFA(nfa);
    MinimalDFA md=new MinimalDFA(d);
    DFAVisualiser v = new DFAVisualiser(d);
    JFrame frame2 = new JFrame();
    frame2.add(v);
    frame2.setSize(800, 800);
    frame2.setLocation(200, 200);
    frame2.setVisible(true);
    System.out.println(md.toXML());
    System.out.println("a");

  
  }

}