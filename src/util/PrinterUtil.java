import java.io.FileWriter;

public class PrinterUtil {

    public static void printStruk(String text) {
        try {
            FileWriter fw = new FileWriter("struk.txt");
            fw.write(text);
            fw.close();

            Runtime.getRuntime().exec("notepad /p struk.txt");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}