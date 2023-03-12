import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    public static void main(String args[]) {
        run();
    }

    public static void run() {
        // Set up frame
        JFrame frame = new JFrame("COS 341 - P1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH + 50, HEIGHT);

        JTabbedPane tp = new JTabbedPane();
        tp.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Add input text field
        JTextField inputRegexFld = new JTextField();
        inputRegexFld.setMargin(new Insets(0, 20, 0, 0));
        inputRegexFld.setText("Enter|Regex|Here");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.weightx = 1;
        pane.add(inputRegexFld, c);

        // add go button
        JButton btn = new JButton();
        btn.setText("Go");
        btn.setMinimumSize(new Dimension(50, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        pane.add(btn, c);

        // Add NFA Check box
        JCheckBox nfaCheckBox = new JCheckBox("View NFA");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(nfaCheckBox, c);

        // Add DFA Check box
        JCheckBox dfaCheckBox = new JCheckBox("View DFA");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(dfaCheckBox, c);

        // Add DFA Check box
        JCheckBox nullState = new JCheckBox("All DFA states need transitions for all symbols");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        pane.add(nullState, c);

        // Add MinDFA Check box
        JCheckBox minDfaCheckBox = new JCheckBox("View Mininal DFA");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        pane.add(minDfaCheckBox, c);

        JLabel hintLbl = new JLabel("");

        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tp.removeAll();
                RegularExpression r = new RegularExpression(inputRegexFld.getText());
                r.insertBrackets();
                AbstractExpression a = null;
                try {
                    a = r.buildTree();
                } catch (Exception e1) {
                    System.out.println("Error encountered: " + e1.getMessage());
                    hintLbl.setText("Error encountered: " + e1.getMessage());
                    return;
                }
                NFA nfa = a.interpret();
                DFA dfa = new DFA(nfa, nullState.isSelected());
                MinimalDFA mDfa = new MinimalDFA(dfa);

                // Add visualisers to tabs
                if (nfaCheckBox.isSelected()) {
                    addNFAVisualiserTab(nfa, tp);
                }
                if (dfaCheckBox.isSelected()) {
                    addDFAVisualiserTab(dfa, tp);
                }
                if (minDfaCheckBox.isSelected()) {
                    addMinimalDFAVisualiserTab(mDfa, tp);
                }

                // Add text area to tabs
                JTextArea xmlTextArea = new JTextArea();
                xmlTextArea.setText(mDfa.toXML());
                xmlTextArea.setEditable(false);
                JScrollPane scroll = new JScrollPane(xmlTextArea);
                scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                tp.add("Mininal DFA XML", scroll);
                hintLbl.setText("Hint: You can drag the states of the above automata for better viewing");
            }
        });

        // Adds tabbedPane to content pane of frame
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        pane.add(tp, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 3;
        c.weighty = 0;
        c.weightx = 0.9;
        pane.add(hintLbl, c);
        frame.getContentPane().add(pane);
        frame.setVisible(true);
    }

    public static void addNFAVisualiserTab(NFA nfa, JTabbedPane tabbedPane) {
        FAVisualiser nfaVisual = new FAVisualiser(nfa);
        tabbedPane.add("NFA", nfaVisual);
    }

    public static void addDFAVisualiserTab(DFA dfa, JTabbedPane tabbedPane) {
        DFAVisualiser dfaVisual = new DFAVisualiser(dfa);
        tabbedPane.add("DFA", dfaVisual);
    }

    public static void addMinimalDFAVisualiserTab(MinimalDFA mDfa, JTabbedPane tabbedPane) {
        MinimalDFAVisualiser mDfaVisual = new MinimalDFAVisualiser(mDfa);
        tabbedPane.add("Minimal DFA", mDfaVisual);
    }
}
