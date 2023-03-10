import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) {
    boolean visualise = true;

    RegularExpression r = new RegularExpression("1234|8?");
    r.insertBrackets();
    AbstractExpression a = r.buildTree();
    NFA nfa = a.interpret();

   
    if (visualise) {
      FAVisualiser v3 = new FAVisualiser(nfa);
      JFrame frame3 = new JFrame();
      frame3.add(v3);
      frame3.setSize(800, 800);
      frame3.setLocation(200, 200);
      frame3.setVisible(true);
    }

    DFA d = new DFA(nfa,false);

    if (visualise) {
      DFAVisualiser v1 = new DFAVisualiser(d);
      JFrame frame = new JFrame();
      frame.add(v1);
      frame.setSize(800, 800);
      frame.setLocation(200, 200);
      frame.setVisible(true);
    }

    MinimalDFA md = new MinimalDFA(d);

    System.out.println(md.toXML());

    if (visualise) {
      MinimalDFAVisualiser v = new MinimalDFAVisualiser(md);
      JFrame frame2 = new JFrame();
      frame2.add(v);
      frame2.setSize(800, 800);
      frame2.setLocation(200, 200);
      frame2.setVisible(true);
    }

  }

}