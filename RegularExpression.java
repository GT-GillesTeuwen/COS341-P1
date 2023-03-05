import java.util.ArrayList;
import java.util.Iterator;

public class RegularExpression {
  private String regex;
  private ArrayList<Character> alphabet;
  private ArrayList<Character> bracketeyBoy;
  private ArrayList<Character> specialCharacters;

  public RegularExpression(String regex) {
    this.alphabet = new ArrayList<>();
    this.bracketeyBoy = new ArrayList<>();
    this.specialCharacters = new ArrayList<>();
    specialCharacters.add('(');
    specialCharacters.add(')');
    specialCharacters.add('*');
    specialCharacters.add('|');
    specialCharacters.add('.');
    this.regex = regex.replace(".", "");

    addDots();
    createAlphabet();
  }

  public String getRegex() {
    return regex;
  }

  public void createAlphabet() {
    for (int i = 0; i < regex.length(); i++) {
      if (!alphabet.contains(regex.charAt(i))) {
        alphabet.add(regex.charAt(i));
      }
    }
  }

  public void printAlphabet() {
    for (int i = 0; i < alphabet.size(); i++) {
      System.out.print(alphabet.get(i) + ",");
    }
  }

  public void bracketyBoy() {
    for (int i = 0; i < bracketeyBoy.size(); i++) {
      System.out.print(bracketeyBoy.get(i));
    }
    System.out.println("");
  }

  public void copyBraketyBoyIntoRegex() {
    regex = "";
    for (int i = 0; i < bracketeyBoy.size(); i++) {
      regex += bracketeyBoy.get(i);
    }
    bracketeyBoy = new ArrayList<>();
  }

  public void insertBrackets() {
    doStars();
    copyBraketyBoyIntoRegex();
    doBinary('.');
    copyBraketyBoyIntoRegex();
    doBinary('|');
    copyBraketyBoyIntoRegex();
  }

  public void doStars() {
    for (int i = 0; i < regex.length(); i++) {
      bracketeyBoy.add(regex.charAt(i));
      if (regex.charAt(i) == '*') {
        addBracketBackwards(i);
        bracketeyBoy.add(')');
      }
    }
  }

  public void doBinary(char operator) {
    for (int i = 0; i < regex.length(); i++) {
      bracketeyBoy.add(regex.charAt(i));
      if (regex.charAt(i) == operator) {
        addBracketBackwards(i);
        i = addBracketsForwards(i);
      }
    }
  }

  public void addBracketBackwards(int i) {
    int counter = 0;
    int index = bracketeyBoy.size() - 2;
    do {
      if (bracketeyBoy.get(index) == ')') {
        counter++;
      }
      if (bracketeyBoy.get(index) == '(') {
        counter--;
      }
      index--;
    } while (counter != 0);

    bracketeyBoy.add(index + 1, '(');

  }

  public int addBracketsForwards(int i) {
    i++;
    int counter = 0;
    do {
      if (regex.charAt(i) == '(') {
        counter++;
      }
      if (regex.charAt(i) == ')') {
        counter--;
      }
      bracketeyBoy.add(regex.charAt(i));
      i++;
    } while (counter != 0);

    bracketeyBoy.add(')');
    return i - 1;
  }

  private void addDots() {
    String newRegex = "";
    for (int i = 0; i < regex.length() - 1; i++) {

      if (!specialCharacters.contains(regex.charAt(i)) && !specialCharacters.contains(regex.charAt(i + 1))) {
        newRegex += regex.charAt(i) + ".";
      }

      else if (!specialCharacters.contains(regex.charAt(i)) && regex.charAt(i + 1) == '(') {
        newRegex += regex.charAt(i) + ".";
      }

      else if (regex.charAt(i) == '*' && regex.charAt(i + 1) != '|' && regex.charAt(i + 1) != ')') {
        newRegex += regex.charAt(i) + ".";
      }

      else if (regex.charAt(i) == ')' && !specialCharacters.contains(regex.charAt(i + 1))) {
        newRegex += regex.charAt(i) + ".";
      }

      else if (regex.charAt(i) == ')' && regex.charAt(i + 1) == '(') {
        newRegex += regex.charAt(i) + ".";
      }

      else {
        newRegex += regex.charAt(i);
      }

    }
    regex = newRegex + regex.charAt(regex.length() - 1);
  }

  public AbstractExpression buildTree() {
    TreeNode current = new TreeNode();
    TreeNode root = current;
    boolean right = false;
    for (int i = 1; i < regex.length(); i++) {

      if (regex.charAt(i) == '(') {
        if (right) {
          current.rNode = new TreeNode();
          current.rNode.parent = current;
          current = current.rNode;
        } else {
          current.lNode = new TreeNode();
          current.lNode.parent = current;
          current = current.lNode;
        }

        right = false;
      }

      else if (regex.charAt(i) == '*') {
        current = current.parent;
        StarOperator s = new StarOperator();
        s.operandLeft = current.lNode.getE();
        current.setE(s);
        current = current.lNode;
      }

      else if (regex.charAt(i) == '.') {
        current = current.parent;
        AndOperator a = new AndOperator();
        a.operandLeft = current.lNode.getE();
        current.setE(a);
        right = true;
      }

      else if (regex.charAt(i) == '|') {
        current = current.parent;
        OrOperator o = new OrOperator();
        o.operandLeft = current.lNode.getE();
        current.setE(o);
        right = true;
      }

      else if (regex.charAt(i) == ')') {
        current = current.parent;
        if (current.getE() == null) {
          EmptyOperator e = new EmptyOperator();
          e.operandLeft = current.lNode.getE();
          current.setE(e);

        }
        if (current.rNode != null) {
          ((Operator) current.getE()).operandRight = current.rNode.getE();
        }

        right = false;
      }

      else {
        if (right) {
          current.rNode = new TreeNode();
          current.rNode.parent = current;
          current.rNode.setE(new TerminalExpression(regex.charAt(i) + ""));
          current = current.rNode;
        } else {
          current.lNode = new TreeNode();
          current.lNode.parent = current;
          current.lNode.setE(new TerminalExpression(regex.charAt(i) + ""));
          current = current.lNode;
        }
      }
    }
    return root.getE();
  }
}