import java.util.ArrayList;

public class Tests {
    private static int TESTS;
    private static int PASSED;
    private static String testName;

    private static int TTL_SETS;
    private static int TTL_PERFECT;
    private static ArrayList<String> badTests = new ArrayList<>();

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void breakLine() {
        System.out.println("======================================");
    }

    public static void startTest(String name) {
        breakLine();
        System.out.println(ANSI_PURPLE + "Starting Test: " + name + ANSI_RESET);
        testName = name;
        breakLine();
    }

    public static void assertEquals(String actual, String expected) {
        TESTS++;
        if (actual.equals(expected)) {
            PASSED++;
        } else {
            System.out.println(
                    ANSI_RED + "Test: " + TESTS + "\tExpected '" + expected + "' got '" + actual + "'" + ANSI_RESET);
        }
    }

    public static void assertEquals(int actual, int expected) {
        TESTS++;
        if (actual == expected) {
            PASSED++;
        } else {
            System.out.println(
                    ANSI_RED + "Test: " + TESTS + "\tExpected " + expected + " got " + actual + "" + ANSI_RESET);
        }
    }

    public static void endTest() {
        String out = "";
        TTL_SETS++;
        if (PASSED != TESTS) {
            badTests.add(testName);
            if (PASSED == 0) {
                out += ANSI_RED;
            } else {
                out += ANSI_YELLOW;
            }
        } else {
            out += ANSI_GREEN;
            TTL_PERFECT++;
        }
        out += "Passed " + PASSED + "/" + TESTS + ANSI_RESET;

        TESTS = 0;
        PASSED = 0;

        System.out.println(out);
        System.out.println("Ending Test: " + testName);
        breakLine();
        System.out.println("\n");
    }

    public static void printSummary() {
        System.out.println("\n");
        breakLine();
        System.out.println(ANSI_BLUE + "Summary:" + ANSI_RESET);
        String out = "";
        if (TTL_SETS != TTL_PERFECT) {
            if (TTL_PERFECT == 0) {
                out += ANSI_RED;
            } else {
                out += ANSI_YELLOW;
            }
        } else {
            out += ANSI_GREEN;
        }

        out += "Total Perfect Sets " + TTL_PERFECT + "/" + TTL_SETS + ANSI_RESET + "\n";

        if (TTL_PERFECT != TTL_SETS) {
            out += "Check the following tests: ";
            for (int i = 0; i < badTests.size(); i++) {
                out += "\n\t" + badTests.get(i);
            }
            out += ("\n");
        }

        System.out.println(out);
        breakLine();
    }

    public static RegularExpression makRegularExpression(String regex) {
        return new RegularExpression(regex);
    }

    public static void main(String[] args) {
        testRegex();
        printSummary();
    }

    public static void testRegex() {
        addingDots();
        basicOperationTest();
        twoOperationTest();
        extraBracketsTest1();
        extraBracketsTest2();
        longExpressionTest();
    }

    public static void addingDots() {
        startTest("Adding dots");
        RegularExpression r = new RegularExpression("ab");
        assertEquals(r.getRegex(), "a.b");

        r = new RegularExpression("ab|c");
        assertEquals(r.getRegex(), "a.b|c");

        r = new RegularExpression("abc");
        assertEquals(r.getRegex(), "a.b.c");

        r = new RegularExpression("ab*c");
        assertEquals(r.getRegex(), "a.b*.c");

        r = new RegularExpression("(ab)*(cd)");
        assertEquals(r.getRegex(), "(a.b)*.(c.d)");

        r = makRegularExpression("a*.b");
        assertEquals(r.getRegex(), "a*.b");
        endTest();
    }

    public static void basicOperationTest() {
        startTest("Basic operations");

        RegularExpression r = new RegularExpression("a|b");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a|b)");

        r = makRegularExpression("a.b");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a.b)");

        r = makRegularExpression("a*");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a*)");

        r = makRegularExpression("a?");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a?)");

        r = makRegularExpression("a+");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a+)");

        endTest();
    }

    public static void twoOperationTest() {
        startTest("Two operations");

        RegularExpression r = makRegularExpression("a.b.c");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a.b).c)");

        r = makRegularExpression("a|b|c");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a|b)|c)");

        r = makRegularExpression("a*.b");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a*).b)");

        r = makRegularExpression("a.b*");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a.(b*))");

        r = makRegularExpression("a*|b");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a*)|b)");

        r = makRegularExpression("a|b*");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a|(b*))");

        r = makRegularExpression("a.b|c");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a.b)|c)");

        r = makRegularExpression("a|b.c");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a|(b.c))");

        endTest();
    }

    public static void extraBracketsTest1() {
        startTest("Extra brackets 1");

        RegularExpression r = new RegularExpression("(a|b)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a|b))");

        r = makRegularExpression("(a.b)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a.b))");

        r = makRegularExpression("(a*)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((a*))");

        endTest();
    }

    public static void extraBracketsTest2() {
        startTest("Extra brackets 2");

        RegularExpression r = makRegularExpression("(a.b).c");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(((a.b)).c)");

        r = makRegularExpression("(a.b.c)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(((a.b).c))");

        r = makRegularExpression("a.(b|c)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a.((b|c)))");

        r=makRegularExpression("(((a).(b))*)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(((((a).(b)))*))");
        endTest();
    }

    public static void longExpressionTest() {
        startTest("Long expression");

        RegularExpression r = makRegularExpression("(1|2|3|4|5|6|7|8|9)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(((((((((1|2)|3)|4)|5)|6)|7)|8)|9))");

        r = makRegularExpression("(1|2|3|4|5|6|7|8|9).(0)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((((((((((1|2)|3)|4)|5)|6)|7)|8)|9)).(0))");
        
        r = makRegularExpression("(1|2|3|4|5|6|7|8|9).(0|1|2|3|4|5|6|7|8|9)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "((((((((((1|2)|3)|4)|5)|6)|7)|8)|9)).((((((((((0|1)|2)|3)|4)|5)|6)|7)|8)|9)))");

        r = makRegularExpression("(g)?.(d)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(((g)?).(d))");

        r = makRegularExpression("(g)*.(dick)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(((g)*).((((d.i).c).k)))");

        r = makRegularExpression("a.(dick)");
        r.insertBrackets();
        assertEquals(r.getRegex(), "(a.((((d.i).c).k)))");

        endTest();
    }
}
