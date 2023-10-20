import java.io.IOException;
import java.util.ArrayList;

public class App {

  // main method has three MemManager that is each manager responsible is
  // responsible for each fit strategy.
  public static void main(String[] args) throws IOException {

    ArrayList<Operation> operations = IO.readFile("memFile.in");
    ArrayList<StringBuilder> intermediateOutputs = new ArrayList<>();
    StringBuilder finalOutput = new StringBuilder();
    int intermediateCounter = 0;

    // #### FIRST FIT MANAGER ####

    MemManager firstFitManager = new MemManager();
    for (Operation op : operations) {
      if (op.isAllocating()) { // if command is A
        firstFitManager.firstFit(op);
      } else if (op.isDeAllocating()) { // if command is D
        firstFitManager.deallocate(op);
      } else if (op.isCompacting()) { // if command is C

        firstFitManager.compact();
      } else if (op.isOutputing()) { // if command is O
        try {
          intermediateOutputs.get(intermediateCounter).append("First fit\n");
          intermediateOutputs.get(intermediateCounter).append(firstFitManager.output());
        } catch (Exception e) {
          intermediateOutputs.add(new StringBuilder());
          intermediateOutputs.get(intermediateCounter).append("First fit\n");
          intermediateOutputs.get(intermediateCounter).append(firstFitManager.output());
        }
        intermediateCounter++;
      }
    }
    finalOutput.append("First fit\n");
    finalOutput.append(firstFitManager.output());

    // #### BEST FIT MANAGER ####
    MemManager bestFitManager = new MemManager();
    intermediateCounter = 0;
    for (Operation op : operations) {
      if (op.isAllocating()) { // if command is A
        bestFitManager.bestFit(op);
      } else if (op.isDeAllocating()) { // if command is D
        bestFitManager.deallocate(op);
      } else if (op.isCompacting()) { // if command is C
        bestFitManager.compact();
      } else if (op.isOutputing()) { // if command is O
        try {
          intermediateOutputs.get(intermediateCounter).append("Best fit\n");
          intermediateOutputs.get(intermediateCounter).append(bestFitManager.output());
        } catch (Exception e) {
          intermediateOutputs.add(new StringBuilder());
          intermediateOutputs.get(intermediateCounter).append("Best fit\n");
          intermediateOutputs.get(intermediateCounter).append(bestFitManager.output());
        }
        intermediateCounter++;
      }
    }
    finalOutput.append("Best fit\n");
    finalOutput.append(bestFitManager.output());

    // #### WORST FIT MANAGER ####
    MemManager worstFitManager = new MemManager();
    intermediateCounter = 0;
    for (Operation op : operations) {
      if (op.isAllocating()) { // if command is A
        worstFitManager.worstFit(op);
      } else if (op.isDeAllocating()) { // if command is D
        worstFitManager.deallocate(op);
      } else if (op.isCompacting()) { // if command is C
        worstFitManager.compact();
      } else if (op.isOutputing()) { // if command is O
        try {
          intermediateOutputs.get(intermediateCounter).append("Worst fit\n");
          intermediateOutputs.get(intermediateCounter).append(worstFitManager.output());
        } catch (Exception e) {
          intermediateOutputs.add(new StringBuilder());
          intermediateOutputs.get(intermediateCounter).append("Worst fit\n");
          intermediateOutputs.get(intermediateCounter).append(worstFitManager.output());
        }
        intermediateCounter++;
      }
    }
    finalOutput.append("Worst fit\n");
    finalOutput.append(worstFitManager.output());

    // prints out intermediate output file
    for (StringBuilder stringBuilder : intermediateOutputs) {
      IO.writeOutput("memFile.out" + intermediateOutputs.indexOf(stringBuilder), stringBuilder.toString());
    }
    // prints out intermediate final file
    IO.writeOutput("memFile.out", finalOutput.toString());
  }
}