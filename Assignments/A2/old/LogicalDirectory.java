import java.util.ArrayList;

/**
 * LogicalDirectory
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class LogicalDirectory {
  private ArrayList<DirectoryEntry> entries;

  public LogicalDirectory(ArrayList<DirectoryEntry> entries) {
    this.entries = entries;
  }

  public static LogicalDirectory Extract(Volume volume, DirectoryEntry dirEntry) {
    ArrayList<DirectoryEntry> entries = new ArrayList<DirectoryEntry>();

    for(int blockIndex = 0; blockIndex < dirEntry.length(); blockIndex++) {
      BlockDirectory blockDirectory = BlockDirectory.Parse(volume.readBlock(dirEntry.getAddress(blockIndex)).getContent());

      for(int entryIndex = 0; entryIndex < blockDirectory.getDirectory().length(); entryIndex++) {
        entries.add(blockDirectory.getDirectory().getEntry(entryIndex));
      }
    }

    return new LogicalDirectory(entries);
  }

  public int length() {
    return entries.size();
  }

  public boolean contains(String name) {
    return find(name) != -1;
  }

  public DirectoryEntry getEntry(int i) {
    return entries.get(i);
  }

  public int find(String name) {
    for(int i = 0; i < entries.size(); i++) {
      if(entries.get(i).getFileName().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  public DirectoryEntry getEntry(String name) {
    int index = find(name);
    if(index == -1) {
      return null;
    } else {
      return getEntry(index);
    }
  }
}
