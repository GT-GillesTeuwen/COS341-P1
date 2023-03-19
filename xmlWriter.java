import java.io.FileWriter;
import java.io.IOException;

public class xmlWriter {
    static class implement {
        public static void writeXml(String regex, String xml) {
            regex=regex.replace("|", "[or]");
            regex=regex.replace("?", "[optional]");
            regex=regex.replace(".", "[and]");
            regex=regex.replace("*", "[none or more]");
            regex=regex.replace("+", "[one or more]");
            try {
                FileWriter writer = new FileWriter(regex + ".xml", false);
                writer.write(xml);
                writer.close();
            } catch (IOException e) {
                try {
                    FileWriter writer2 = new FileWriter(regex + ".xml", true);
                    writer2.write(xml);
                    writer2.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }
}
