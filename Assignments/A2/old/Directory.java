/**
 * Directory
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */

public class Directory {
  public static int DEFAULT_NUM_ENTRIES = 8;

  private Block block;
  private DirectoryEntry[] entries;

  public Directory(Block block, int numEntries) {
    this.block = block;
    entries = new DirectoryEntry[numEntries];

    for(int i = 0; i < entries.length; i++) {
      entries[i] = new DirectoryEntry(this);
    }
  }

  public Directory(Block block, DirectoryEntry[] entries) {
    this.block = block;
    this.entries = entries;
  }

  public static Directory Parse(Block block, String line) {
    Directory directory = new Directory(block, 0);

    int numEntries = (int)(line.length() / DirectoryEntry.LENGTH);
    DirectoryEntry[] entries = new DirectoryEntry[numEntries];
    for(int i = 0; i < numEntries; i++) {
      entries[i] = DirectoryEntry.Parse(directory, line.substring(i * DirectoryEntry.LENGTH, (i + 1) * DirectoryEntry.LENGTH));
    }
    directory.setEntries(entries);

    return directory;
  }

  public Block getBlock() {
    return block;
  }

  public DirectoryEntry getEntry(int entryIndex) {
    return entries[entryIndex];
  }

  public void setEntries(DirectoryEntry[] entries) {
    this.entries = entries;
  }

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
