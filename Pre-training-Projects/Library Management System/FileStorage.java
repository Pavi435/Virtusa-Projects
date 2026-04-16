import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileStorage {

    public static void saveRecord(String fileName, String dataLine) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(dataLine);
        } catch (IOException e) {
            System.out.println("File Error: Could not save to " + fileName);
        }
    }

    public static List<String> readAllRecords(String fileName) {
        List<String> recordsList = new ArrayList<>();
        File f = new File(fileName);
        if (!f.exists()) return recordsList;

        try (Scanner fileScanner = new Scanner(f)) {
            while (fileScanner.hasNextLine()) {
                recordsList.add(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Error: " + fileName + " not found.");
        }
        return recordsList;
    }

    public static void overwriteFile(String fileName, List<String> allData) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, false))) {
            for (String line : allData) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.out.println("File Error: Could not update " + fileName);
        }
    }
}