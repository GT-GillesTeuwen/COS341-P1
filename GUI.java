import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class GUI{
    public static void main(String args[]){
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);
        JTabbedPane tp=new JTabbedPane(); 
        RegularExpression r=new RegularExpression("gilles|abc|-?(0|1|2)");
        r.insertBrackets();
        AbstractExpression a = r.buildTree();
        NFA nfa = a.interpret();
        DFA dfa = new DFA(nfa, false);
        MinimalDFA mDfa=new MinimalDFA(dfa);
        FAVisualiser nfaVisual=new FAVisualiser(nfa);
        DFAVisualiser dfaVisual=new DFAVisualiser(dfa);
        MinimalDFAVisualiser mDfaVisual=new MinimalDFAVisualiser(mDfa);
        tp.add("NFA",nfaVisual);
        tp.add("DFA",dfaVisual);
        tp.add("Minimal DFA",mDfaVisual);
        JTextArea xmlTextArea=new JTextArea();
        xmlTextArea.setText(mDfa.toXML());
        xmlTextArea.setEditable(false);
        JScrollPane scroll = new JScrollPane (xmlTextArea);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
          scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tp.add("Mininal DFA XML",scroll);
        frame.getContentPane().add(tp); // Adds Button to content pane of frame
        frame.setVisible(true);
     }
}
