import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {
    public static void main(String[] args) throws IOException {
        run();
    }

    public static void run() throws IOException {
        System.out.print("How many regular expressions would you like to convert?\t");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        // Reading data using readLine
        int num = 0;
        boolean isANumber;
        do {
            try {
                num = Integer.parseInt(reader.readLine());
                isANumber = true;
            } catch (NumberFormatException e) {
                isANumber = false;
                System.out.print("How many regular expressions would you like to convert?\t");
            }
        } while (!isANumber);

        // Check if null state is allowed/required
        boolean nullAllowed;
        System.out.print("Should every DFA state have tansitions for every relevant symbol? (y/n)\t");
        reader = new BufferedReader(
                new InputStreamReader(System.in));

        String nullAllowedStr = (reader.readLine());
        if (nullAllowedStr.equalsIgnoreCase("y")) {
            nullAllowed = true;
        } else {
            nullAllowed = false;
        }

        // Printing the read line

        for (int i = 0; i < num; i++) {
            System.out.print("\n\n\nEnter regular expression number " + (i + 1) + ": ");
            reader = new BufferedReader(
                    new InputStreamReader(System.in));
            String regex = (reader.readLine());
            RegularExpression r = new RegularExpression(regex);
            try {
                r.insertBrackets();
                AbstractExpression a = null;
                try {
                    a = r.buildTree();
                    NFA nfa = a.interpret();
                    DFA d = new DFA(nfa, nullAllowed);
                    MinimalDFA md = new MinimalDFA(d);
                    System.out.println("\nXML for (" + regex + "):\n\n" + md.toXML());
                    xmlWriter.implement.writeXml(regex, md.toXML());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.out.println("Error encountered: " + e1.getMessage());

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Error encountered: " + e.getMessage());

            }

        }
        String answer;
        do {
            System.out.print("Enter q to quit: ");
            reader = new BufferedReader(
                    new InputStreamReader(System.in));
            answer = (reader.readLine());
        } while (!answer.equalsIgnoreCase("q"));
    }
}
