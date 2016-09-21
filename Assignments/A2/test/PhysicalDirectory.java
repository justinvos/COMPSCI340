public class PhysicalDirectory {
  public static int DEFAULT_START_POSITION = 0;
  public static int DEFAULT_END_POSITION = 512;

  private Block block;
  private int startPosition;
  private int endPosition;

  private Entry[] entries;

  public PhysicalDirectory(Block block) {
    this(block, PhysicalDirectory.DEFAULT_START_POSITION, PhysicalDirectory.DEFAULT_END_POSITION);
  }

  public PhysicalDirectory(Block block, int startPosition, int endPosition) {
    this.block = block;
    this.startPosition = startPosition;
    this.endPosition = endPosition;

    int length = (endPosition - startPosition) / DirectoryEntry.LENGTH;

    entries = new Entry[length];

    for(int index = 0; index < entries.length; index++) {
      int entryStartPosition = startPosition + index * DirectoryEntry.LENGTH;
      int entryEndPosition = startPosition + (index + 1) * DirectoryEntry.LENGTH;

      entries[index] = new Entry(this, block.toString().substring(entryStartPosition, entryEndPosition));
    }
  }

  public void write() {
    System.out.println(block.toString());
    block.setContent(this.toString());
    System.out.println(block.toString());
    block.write();
  }

  public int getAddress() {
    return block.getAddress();
  }

  public Entry get(int index) {
    return entries[index];
  }

  public int length() {
    return entries.length;
  }

  public void set(int index, Entry entry) {
    entries[index] = entry;
  }

  public Entry findEmptyEntry() {
    for(int index = 0; index < length(); index++) {
      if(entries[index].getName().trim().equals("")) {
        return entries[index];
      }
    }
    return null;
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
