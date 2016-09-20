/**
 * BlockDirectory
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class BlockDirectory extends Block {
  public static int DEFAULT_NUM_ENTRIES = 8;

  private DirectoryEntry[] entries;

  public BlockDirectory() {
    this(BlockDirectory.DEFAULT_NUM_ENTRIES);
  }

  public BlockDirectory(int numEntries) {
    super();

    entries = new DirectoryEntry[numEntries];

    for(int i = 0; i < entries.length; i++) {
      entries[i] = new DirectoryEntry();
    }

    setContent(this.toString());
  }

  public BlockDirectory(DirectoryEntry[] entries) {
    super();

    this.entries = entries;

    setContent(this.toString());
  }

  public static BlockDirectory Parse(String line) {
    int numEntries = (int)(line.length() / DirectoryEntry.LENGTH);

    DirectoryEntry[] entries = new DirectoryEntry[numEntries];

    for(int i = 0; i < numEntries; i++) {
      entries[i] = DirectoryEntry.Parse(line.substring(i * DirectoryEntry.LENGTH, (i + 1) * DirectoryEntry.LENGTH));
    }

    return new BlockDirectory(entries);
  }

  public DirectoryEntry getEntry(int i) {
    return entries[i];
  }

  /*
  public DirectoryEntry getEntry(String fileName) {
    for(int i = 0; i < entries.length; i++) {
      if(entries[i].getFileName().equals(fileName)) {
        return entries[i];
      }
    }
    return null;
  }

  public boolean contains(String fileName) {
    for(int i = 0; i < entries.length; i++) {
      if(entries[i].getFileName().equals(fileName)) {
        return true;
      }
    }
    return false;
  }
  */

  public int length() {
    return entries.length;
  }

  public int size() {
    int size = 0;

    for(int i = 0; i < entries.length; i++) {
      size = size + entries[i].size();
    }

    return size;
  }

  @Override
  public String toString() {
    String output = "";

    for(int i = 0; i < entries.length; i++) {
      output = output + entries[i].toString();
    }

    return output;
  }
}
