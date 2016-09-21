public class EntryFile extends Entry {
  public BlockData[] blocks;

  public EntryFile(BlockDirectory parent, int index) {
    this.parent = parent;
    setIndex(index);
    blocks = new BlockData[Entry.NUM_ADDRESS];
  }

  public static EntryFile Parse(BlockDirectory parent, int index, String line) {
    EntryFile entry = new EntryFile(parent, index);

    entry.setName(line.substring(2, 10).trim());
    entry.setSize(Integer.parseInt(line.substring(11, 15)));
    String[] stringAddresses = line.substring(16, 63).split(" ");
    int[] addresses = new int[Entry.NUM_ADDRESS];
    for(int slot = 0; slot < stringAddresses.length; slot++) {
      entry.set(slot, new BlockData(Integer.parseInt(stringAddresses[slot])));
    }

    return entry;
  }


  public Block get(int slot) {
    return blocks[slot];
  }

  public void set(int slot, BlockData block) {
    blocks[slot] = block;
  }

  public int length() {
    return blocks.length;
  }

  @Override
  public String toString() {
    String output = "f:";

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
