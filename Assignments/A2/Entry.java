/**
 * Entry
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public abstract class Entry {
  public static int LENGTH = 64;
  public static int NUM_ADDRESS = 12;
  public static int MAX_NAME_LENGTH = 8;

  private boolean isRoot = false;

  private boolean isDirectory = false;
  private String name = "";
  private int size = 0;
  private int index;

  public BlockDirectory parent = null;


  public static Entry Parse(BlockDirectory parent, int index, String line) {
    if(line.substring(0, 2).equals("d:")) {
      return EntryDirectory.Parse(parent, index, line);
    } else {
      return EntryFile.Parse(parent, index, line);
    }
  }

  public BlockDirectory getParent() {
    return parent;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public boolean isDirectory() {
    return isDirectory;
  }

  public boolean isFile() {
    return !isDirectory;
  }

  public void setDirectory() {
    isDirectory = true;
  }

  public void setFile() {
    isDirectory = false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void write() {
    getParent().write();
  }

  public abstract int size();

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public abstract String toString();
}
