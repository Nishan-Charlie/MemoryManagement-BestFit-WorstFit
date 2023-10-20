import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

public class MemManager {
  ArrayList<MemBlock> freeMem; // ArrayList to store the blocks of free memory
  ArrayList<MemBlock> allocatedMem; // ArrayList to store the blocks of allocated memory
  ArrayList<String> errors; // ArrayList to store the errors
  ArrayList<Integer> idFails; // ArrayList to store the Id for failures

  public MemManager() throws NumberFormatException, IOException {
    freeMem = new ArrayList<>();
    allocatedMem = new ArrayList<>();
    errors = new ArrayList<>();
    idFails = new ArrayList<>();
    freeMem.add(new MemBlock()); // Free MemBlock of size 1000 created
  }

  // Method to perform first fit memory allocation: first free block --> large
  // enough to fit in the memory.
  public void firstFit(Operation op) {
    for (MemBlock block : freeMem) {
      if (block.getSize() >= op.getSize()) {
        allocate(block, op);
        
        return;
      }
    }
    // If no suitable block is found
    errors.add("A;" + op.getLineNumber() + ";" + getLargestFreeBlockSize());
    idFails.add(op.getBlockID());
  }

  // Method to perform best fit memory allocation --> first smallest free block
  // --> large enough to fit in the memory
  public void bestFit(Operation op) {
    MemBlock bestBlock = null;
    for (MemBlock block : freeMem) {
      if (block.getSize() >= op.getSize()) {
        if (bestBlock == null || block.getSize() < bestBlock.getSize()) {
          bestBlock = block;
        }
      }
    }
    if (bestBlock != null) {
      // Allocate the memory and update the free memory block
      allocate(bestBlock, op);
      return;
    } else {
      // If no suitable block is found
      errors.add("A;" + op.getLineNumber() + ";" + getLargestFreeBlockSize());
      idFails.add(op.getBlockID());
    }
  }

  // Method to perform worst fit memory allocation --> first largest free block
  // --> large enough to fit in the memory
  public void worstFit(Operation op) {
    MemBlock worstBlock = null;
    for (MemBlock block : freeMem) {
      if (block.getSize() >= op.getSize()) {
        if (worstBlock == null || block.getSize() > worstBlock.getSize()) {
          worstBlock = block;
        }
      }
    }
    if (worstBlock != null) {
      allocate(worstBlock, op);
      return;
    } else {
      // If no suitable block is found
      errors.add("A;" + op.getLineNumber() + ";" + getLargestFreeBlockSize());
      idFails.add(op.getBlockID());
    }
  }

  // Method to allocate memory and update the free memory block:
  // Create a new block for the allocated memory and add it to the allocated
  // memory list
  public void allocate(MemBlock block, Operation op) {
    MemBlock allocatedBlock = new MemBlock(op.getBlockID(), block.getStart(), op.getSize());
    allocatedMem.add(allocatedBlock);
    // Update the free memory block
    MemBlock newFreeBlock = new MemBlock(null, allocatedBlock.getEnd() + 1, block.getSize() - allocatedBlock.getSize());
    freeMem.add(newFreeBlock);
    freeMem.remove(block);
  }

  // Method to deallocate a block of memory
  // Find the allocated block and add it to the free memory list
  public void deallocate(Operation op) {
    for (MemBlock block : allocatedMem) {
      if (block.getId() == op.getBlockID()) {
        freeMem.add(block);
        block.setId(null);

        Collections.sort(freeMem, Comparator.comparingInt(MemBlock::getStart));
        ListIterator<MemBlock> iterator = freeMem.listIterator();
        MemBlock previous = null;
        while (iterator.hasNext()) {
          MemBlock current = iterator.next();
          if (previous != null && previous.getEnd() == current.getStart() - 1) {
            // merge two block:
            previous.setSize(previous.getSize() + current.getSize());
            iterator.remove();
            freeMem.remove(current);
          } else {
            previous = current;
          }
        }
        // Remove the block from the list of allocated memory
        allocatedMem.remove(block);
        return;
      }
    }
    // Add deallocation errors if they occured
    if (idFails.contains(op.getBlockID())) {
      errors.add("D;" + op.getLineNumber() + ";" + "1");
    } else {
      errors.add("D;" + op.getLineNumber() + ";" + "0");
    }
  }

  // Method to compact the free memory
  public void compact() throws NumberFormatException, IOException {
    Collections.sort(allocatedMem, Comparator.comparingInt(MemBlock::getStart));

    int currentPos = 0;
    for (MemBlock allocatedBlock : allocatedMem) {
      allocatedBlock.setStart(currentPos);
      currentPos += allocatedBlock.getSize();
    }
    freeMem.clear();
    freeMem.add(new MemBlock(null, currentPos, IO.getMaxMemory() - currentPos));
  }

  // Method to calculate the external fragmentation of the memory
  public String fragmentation() {
    freeMem.sort((a, b) -> b.getSize() - a.getSize());
    int largestBlock = getLargestFreeBlockSize();
    int totalFree = 0;
    for (MemBlock block : freeMem) {
      totalFree += block.getSize();
    }
    double fragmentation = 1 - ((double) largestBlock / totalFree);
    return String.format("%.6f\n", fragmentation);
  }

  // Method to get the largest free block size
  public int getLargestFreeBlockSize() {
    int largestBlock = 0;
    for (MemBlock block : freeMem) {
      largestBlock = Math.max(largestBlock, block.getSize());
    }
    return largestBlock;
  }

  // Method to output the current status of the memory
  public StringBuilder output() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Allocated blocks\n");
    Collections.sort(allocatedMem, Comparator.comparingInt(MemBlock::getId));
    for (MemBlock block : allocatedMem) {
      stringBuilder.append(block.getId() + ";" + block.getStart() + ";" + block.getEnd() + "\n");
    }
    stringBuilder.append("Free blocks\n");
    Collections.sort(freeMem, Comparator.comparingInt(MemBlock::getStart));
    for (MemBlock block : freeMem) {
      
      stringBuilder.append(block.getStart() + ";" + block.getEnd() + "\n");
    }
    stringBuilder.append("Fragmentation\n" + fragmentation());
    stringBuilder.append("Errors\n");

    if (errors.isEmpty()) {
      stringBuilder.append("None\n");
    } else {
      for (String error : errors) {
        stringBuilder.append(error + "\n");
      }
    }
    stringBuilder.append("\n");
    return stringBuilder;
  }
}