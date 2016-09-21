public class BlockDirectory extends Block {

  public static int DEFAULT_ENTRY_NUM = 8;

  private Entry[] entries;

  public BlockDirectory(int address, int entryNum) {
    super(address);
    this.entries = new Entry[entryNum];

    for(int index = 0; index < length(); index++) {
      entries[index] = new EntryFile(this, index);
    }
  }

  public BlockDirectory(int address) {
    this(address, BlockDirectory.DEFAULT_ENTRY_NUM);
  }

  public static BlockDirectory Parse(int address) {
    BlockDirectory directory = new BlockDirectory(address);

    if(address == 0) {
      directory = new BlockDirectory(address, BlockVolumeInfo.VOLUME_INFO_ENTRY_NUM);
    }
    String line = directory.read();

    for(int index = 0; index < directory.length(); index++) {
      directory.set(index, Entry.Parse(directory, index, line.substring(index * Entry.LENGTH, (index + 1) * Entry.LENGTH)));
    }

    return directory;
  }

  public int length() {
    return entries.length;
  }

  public Entry get(int index) {
    return entries[index];
  }

  public void set(int index, Entry entry) {
    entries[index] = entry;
  }

  public int find(String name) {
    for(int index = 0; index < entries.length; index++) {
      if(entries[index].getName().equals(name)) {
        return index;
      }
    }
    return -1;
  }

  public Entry get(String name) {
    int index = find(name);
    if(index != -1) {
      return get(index);
    } else {
      return null;
    }
  }

  public Entry getFreeEntry() {
    int index = find("");
    if(index != -1) {
      Entry entry = get(index);
      return entry;
    } else {
      return null;
    }
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    String output = "";

    for(int index = 0; index < entries.length; index++) {
      output += entries[index].toString();
    }

    return output;
  }
}
