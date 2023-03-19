import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    specialCharacters.add('?');
    specialCharacters.add('+');
    this.regex = regex.replace(".", "");
    this.regex = this.regex.replace(" ", "");
    
    createAlphabet();
  }

  public void validateWithAlphabet() throws Exception{
    Set<Character> alphabet=new HashSet<>();
    for (int i = 65; i < 65+26; i++) {
      alphabet.add((char) i);
    }
    for (int i = 97; i < 97+26; i++) {
      alphabet.add((char) i);
    }
    for (int i = 48; i < 58; i++) {
      alphabet.add((char) i);
    }

    alphabet.add('|');
    alphabet.add('.');
    alphabet.add('*');
    alphabet.add('+');
    alphabet.add('?');
    alphabet.add('(');
    alphabet.add(')');

    for (int i = 0; i < regex.length(); i++) {
      if(!alphabet.contains(regex.charAt(i))){
        throw new Exception("Alphabet exception");
      }
    }
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

  public void copyRegexIntoBracketyBoy() {
    bracketeyBoy.clear();
    for (int index = 0; index < regex.length(); index++) {
      bracketeyBoy.add(regex.charAt(index));
    }
  }

  public void insertBrackets() throws Exception {
    try{
      
      addDots();
      }catch(StringIndexOutOfBoundsException e){
        throw new Exception("Empty string not allowed");
      }
    try{   
    doUnary('*');
    doUnary('+');
    doUnary('?');
    doBinary('.');
    doBinary('|');
    }catch(ArrayIndexOutOfBoundsException e){
      throw new Exception("Malformed Regex");
    }catch(IndexOutOfBoundsException e){
      throw new Exception("Malformed Regex");
    }
  }

  public void doUnary(char operator) throws ArrayIndexOutOfBoundsException, IndexOutOfBoundsException{

    int p = regex.indexOf(operator, 0);
    while (p != -1) {
      copyRegexIntoBracketyBoy();
      addBracketBackwards(p);
      bracketeyBoy.add(p + 2, ')');
      copyBraketyBoyIntoRegex();
      p = regex.indexOf(operator, p + 2);
    }
  }

  public void doBinary(char operator) throws ArrayIndexOutOfBoundsException, IndexOutOfBoundsException{
    int p = regex.indexOf(operator, 0);
    while (p != -1) {
      copyRegexIntoBracketyBoy();
      addBracketBackwards(p);
      addBracketsForwards(p + 1);
      copyBraketyBoyIntoRegex();
      p = regex.indexOf(operator, p + 2);
    }

  }

  public void addBracketBackwards(int i) throws ArrayIndexOutOfBoundsException, IndexOutOfBoundsException{
    
    int counter = 0;
    int index = i - 1;
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

  public void addBracketsForwards(int i) throws ArrayIndexOutOfBoundsException, IndexOutOfBoundsException{
    int counter = 0;
    int index = i + 1;
    do {
      if (bracketeyBoy.get(index) == ')') {
        counter++;
      }
      if (bracketeyBoy.get(index) == '(') {
        counter--;
      }
      index++;
    } while (counter != 0);
    bracketeyBoy.add(index, ')');
  }

  public void addDots() throws StringIndexOutOfBoundsException{
    String newRegex = "";
    for (int i = 0; i < regex.length() - 1; i++) {

      if (!specialCharacters.contains(regex.charAt(i)) && !specialCharacters.contains(regex.charAt(i + 1))) {
        newRegex += regex.charAt(i) + ".";
      }

      else if (!specialCharacters.contains(regex.charAt(i)) && regex.charAt(i + 1) == '(') {
        newRegex += regex.charAt(i) + ".";
      }

      else if ((regex.charAt(i) == '*' || regex.charAt(i) == '+' || regex.charAt(i) == '?')
          && regex.charAt(i + 1) != '|' && regex.charAt(i + 1) != ')') {
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

  public AbstractExpression buildTree() throws Exception {
    TreeNode current = new TreeNode();
    TreeNode root = current;
    boolean right = false;
    if (regex.length() == 1) {
      return new TerminalExpression(regex.charAt(0) + "");
    }
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
        if (current == null) {
          throw new Exception("Malformed Regex");
        }
        StarOperator s = new StarOperator();
        s.operandLeft = current.lNode.getE();
        current.setE(s);
        current = current.lNode;
      }

      else if (regex.charAt(i) == '+') {
        current = current.parent;
        if (current == null) {
          throw new Exception("Malformed Regex");
        }
        PlusOperator p = new PlusOperator();
        p.operandLeft = current.lNode.getE();
        current.setE(p);
        current = current.lNode;
      }

      else if (regex.charAt(i) == '?') {
        current = current.parent;
        if (current == null) {
          throw new Exception("Malformed Regex");
        }
        OptionalOperator o = new OptionalOperator();
        o.operandLeft = current.lNode.getE();
        current.setE(o);
        current = current.lNode;
      }

      else if (regex.charAt(i) == '.') {
        current = current.parent;
        if (current == null) {
          throw new Exception("Malformed Regex");
        }
        AndOperator a = new AndOperator();
        a.operandLeft = current.lNode.getE();
        current.setE(a);
        right = true;
      }

      else if (regex.charAt(i) == '|') {
        current = current.parent;
        if (current == null) {
          throw new Exception("Malformed Regex");
        }
        OrOperator o = new OrOperator();
        o.operandLeft = current.lNode.getE();
        current.setE(o);
        right = true;
      }

      else if (regex.charAt(i) == ')') {
        current = current.parent;
        if (current == null) {
          throw new Exception("Malformed Regex");
        }
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
          if (current == null) {
            throw new Exception("Malformed Regex");
          }
          current.rNode = new TreeNode();
          current.rNode.parent = current;
          current.rNode.setE(new TerminalExpression(regex.charAt(i) + ""));
          current = current.rNode;
        } else {
          if (current == null) {
            throw new Exception("Malformed Regex");
          }
          current.lNode = new TreeNode();
          current.lNode.parent = current;
          current.lNode.setE(new TerminalExpression(regex.charAt(i) + ""));
          current = current.lNode;
        }
      }
    }
    if(root.getE()==null){
      throw new Exception("Malformed Regex");
    }
    return root.getE();
  }
}