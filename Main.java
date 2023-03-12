import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) throws Exception {
    System.out.print("Choose mode to lauch in \n1:GUI\n2:CLI\n\t");
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));

    // Reading data using readLine
    int num = 0;
    boolean isANumber;
    do {
      try {
        num = Integer.parseInt(reader.readLine());
        if (num == 1 || num == 2) {
          isANumber = true;
        } else {
          isANumber = false;
          System.out.print("Choose mode to lauch in \n1:GUI\n2:CLI\n\t");
        }

      } catch (NumberFormatException e) {
        isANumber = false;
        System.out.print("Choose mode to lauch in \n1:GUI\n2:CLI\n\t");
      }
    } while (!isANumber);

    if (num == 1) {
      new GUI().run();
    } else if (num == 2) {
      new CLI().run();
    }

  }

}