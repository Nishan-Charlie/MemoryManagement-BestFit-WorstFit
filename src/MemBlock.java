import java.io.IOException;

public class MemBlock {
  private Integer id;
  private int start;
  private int size;
  private int end;

  // will be used to determine the memory size
  public MemBlock() throws NumberFormatException, IOException {
    this.size = IO.getMaxMemory();
  }

  public MemBlock(Integer id, int start, int size) {
    this.size = size;
    this.id = id;
    this.start = start;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Integer getId() {
    return id;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return start + size - 1;
  }

  public int getSize() {
    return size;
  }

  public boolean isFree() {
    return id == null;
  }

  public void allocate(int size) {
    this.size = size;
  }

  public void deallocate() {
    this.id = null;
  }

  public void setEnd(int end) {
    this.end = end;
  }
}