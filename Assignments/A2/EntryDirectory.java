public class EntryDirectory extends Entry {
  public BlockDirectory[] blocks;

  public EntryDirectory(BlockDirectory parent, int index) {
    this.parent = parent;
    setIndex(index);
    blocks = new BlockDirectory[Entry.NUM_ADDRESS];
    setDirectory();
  }

  public EntryDirectory(BlockVolumeInfo volumeInfo) {
    blocks = new BlockDirectory[1];
    blocks[0] = volumeInfo;
  }

  public static EntryDirectory Parse(BlockDirectory parent, int index, String line) {
    EntryDirectory entry = new EntryDirectory(parent, index);

    entry.setName(line.substring(2, 10).trim());
    entry.setSize(Integer.parseInt(line.substring(11, 15)));
    String[] stringAddresses = line.substring(16, 63).split(" ");
    int[] addresses = new int[Entry.NUM_ADDRESS];
    for(int slot = 0; slot < stringAddresses.length; slot++) {
      int address = Integer.parseInt(stringAddresses[slot]);
      if(address != 0) {
        entry.set(slot, BlockDirectory.Parse(address));
      }
    }

    return entry;
  }

  public BlockDirectory get(int slot) {
    return blocks[slot];
  }

  public void set(int slot, BlockDirectory block) {
    blocks[slot] = block;
  }

  public void remove(int slot) {
    BlockDirectory block = get(slot);
    if(block != null) {
      set(slot, null);
      new Block(block.getAddress()).write();
    }
  }

  public int size() {
    int size = 0;
    for(int slot = 0; slot < blocks.length; slot++) {
      BlockDirectory block = get(slot);
      if(block != null) {
        size += block.size();
      }
    }
    if(getSize() != size) {
      setSize(size);
      write();
    }
    return size;
  }

  public int length() {
    return blocks.length;
  }

  public Entry getFreeEntry() {
    for(int slot = 0; slot < length(); slot++) {
      Entry entry = ((BlockDirectory)get(slot)).getFreeEntry();
      if(entry != null) {
        return entry;
      }
    }
    return null;
  }


  @Override
  public String toString() {
    String output = "d:";

    output += TinyDOS.AddPostPadding(getName(), 8, " ") + " ";
    output += TinyDOS.AddPrePadding(String.valueOf(getSize()), 4, "0") + ":";

    for(int slot = 0; slot < blocks.length; slot++) {
      int address = 0;
      if(blocks[slot] != null) {
        address = blocks[slot].getAddress();
      }
      output += TinyDOS.AddPrePadding(String.valueOf(address), 3, "0") + " ";
    }

    return output;
  }
}
