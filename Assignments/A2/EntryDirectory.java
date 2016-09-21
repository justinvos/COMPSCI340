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
      entry.set(slot, new BlockDirectory(Integer.parseInt(stringAddresses[slot])));
    }

    return entry;
  }

  public BlockDirectory get(int slot) {
    return blocks[slot];
  }

  public void set(int slot, BlockDirectory block) {
    blocks[slot] = block;
  }

  public int length() {
    return blocks.length;
  }

  public Entry getChild(String name) {
    for(int slot = 0; slot < length(); slot++) {
      Block block = get(slot);
      if(block instanceof BlockDirectory) {
        BlockDirectory directory = (BlockDirectory)block;
        Entry child = directory.get(name);
        if(child != null) {
          return child;
        }
      } else {
        System.out.println("SHOULD NOT HAPPEN");
      }
    }
    return null;
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

  public Entry mkfile(String name) {
    Entry entry = getFreeEntry();
    entry.setName(name);
    entry.setFile();

    entry.getParent().write();

    return entry;
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
