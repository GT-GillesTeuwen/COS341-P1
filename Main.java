import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) {
    RegularExpression r = new RegularExpression("0|(1|2)|(34)*");
    r.insertBrackets();
    AbstractExpression a = r.buildTree();
    NFA nfa = a.interpret();
    // create an instance of JFrame class
    JFrame frame = new JFrame();
    // set size, layout and location for frame.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    FAVisualiser p = new FAVisualiser(nfa);
    frame.add(p);
    frame.setSize(800, 800);
    frame.setLocation(200, 200);
    frame.setVisible(true);

    
    DFA d=new DFA(nfa);
    DFAVisualiser v = new DFAVisualiser(d);
    JFrame frame2 = new JFrame();
    frame2.add(v);
    frame2.setSize(800, 800);
    frame2.setLocation(200, 200);
    frame2.setVisible(true);

    MinimalDFA md=new MinimalDFA(d);

  System.out.println(d.toXML());
  }

}