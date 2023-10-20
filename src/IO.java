import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IO {
  public static int maxMemory;

  // method to get the max memory
  public static int getMaxMemory() throws NumberFormatException, IOException {
    FileReader reader = new FileReader("src/memFile.in");
    BufferedReader bufferedReader = new BufferedReader(reader);
    maxMemory = Integer.parseInt(bufferedReader.readLine().replace(";", ""));
    bufferedReader.close();
    return maxMemory;
  }

  // method to get read the file and return List of Operations
  public static ArrayList<Operation> readFile(String fileName) throws IOException {
    ArrayList<Operation> operations = new ArrayList<Operation>();
    FileReader reader = new FileReader(fileName);
    BufferedReader bufferedReader = new BufferedReader(reader);

    // GETTING MEMORY SIZE:
    maxMemory = getMaxMemory();

    String line;
    int currLineNumber = 0;
    // getting the commands,id, and sizes to lists from the file:
    while ((line = bufferedReader.readLine()) != null) {
      String[] fileContent = line.split(";");
      if (fileContent[0].equals("A")) {
        operations.add(new Operation(fileContent[0], Integer.parseInt(fileContent[1]), Integer.parseInt(fileContent[2]),
            currLineNumber++));
      } else if (fileContent[0].equals("D")) {
        operations.add(new Operation(fileContent[0], Integer.parseInt(fileContent[1]), currLineNumber++));
      } else {
        operations.add(new Operation(fileContent[0], currLineNumber++));
      }
    }
    bufferedReader.close();
    //for (Operation operation : operations) {
    //System.out.println(operation);
    //}
    return operations;
  }

  // method to print the output of operations to a file
  public static void writeOutput(String filePath, String output) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
    writer.write(output);
    writer.close();
  }
}
