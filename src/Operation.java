import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class Operation {
  private String command;
  private Integer blockID;
  private Integer memSize;
  private int lineNumber;

  public Operation() {}
  /**
   * @param command D - Deallocate, O - output current status
   * @param blockID reference number of the block
   */
  public Operation(String command, Integer blockID, int lineNumber) {
    this.command = command;
    this.blockID = blockID;
    this.lineNumber = lineNumber;
  }

  /**
   * * @param command C - Compact memory O - output current status
   */
  public Operation(String command, Integer lineNumber) {
    this.command = command;
    this.lineNumber = lineNumber;
  }

  /**
   * @param command A - Allocate
   * @param blockID reference number
   * @param memSize bytes to be allocated
   */
  public Operation(String command, Integer blockID, int memSize, int lineNumber) {
    this.command = command;
    this.blockID = blockID;
    this.memSize = memSize;
    this.lineNumber = lineNumber;
  }

  // GETTERS & SETTERS
  public void setCommand(String command) {
    this.command = command;
  }

  public void setBlockID(Integer blockID) {
    this.blockID = blockID;
  }

  public void setMemSize(Integer memSize) {
    this.memSize = memSize;
  }

  public String getCommand() {
    return command;
  }

  public Integer getBlockID() {
    return blockID;
  }

  public int getSize() {
    return memSize;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  // IF COMMAND == A
  public boolean isAllocating() {
    return getCommand().equals("A");
  }

  // IF COMMAND == D
  public boolean isDeAllocating() {
    return getCommand().equals("D");
  }

  // IF COMMAND == C
  public boolean isCompacting() {
    return getCommand().equals("C");
  }

  // IF COMMAND == O
  public boolean isOutputing() {
    return getCommand().equals("O");
  }

  // method to compact the memory blocks
  public static void compact(List<MemBlock> blocks) {
    // Sort the blocks by start address
    Collections.sort(blocks, Comparator.comparingInt(MemBlock::getStart));

    // Iterate through the blocks and merge adjacent free blocks
    ListIterator<MemBlock> iterator = blocks.listIterator();
    MemBlock previous = null;
    while (iterator.hasNext()) {
      MemBlock current = iterator.next();
      if (previous != null && previous.isFree() && current.isFree()) {
        // merge two block:
        previous.setSize(previous.getSize() + current.getSize());
        iterator.remove();
      } else {
        previous = current;
      }
    }
  }

  @Override
  public String toString() {

    return "Operation [" + command + ", " + blockID + ", " + memSize + "]";
  }
}
