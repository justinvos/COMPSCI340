/**
 * EntryFile
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
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

    for(int slot = 0; slot < stringAddresses.length; slot++) {
      int address = Integer.parseInt(stringAddresses[slot]);
      if(address != 0) {
        entry.set(slot, new BlockData(address));
      }
    }

    return entry;
  }


  public BlockData get(int slot) {
    return blocks[slot];
  }

  public void set(int slot, BlockData block) {
    blocks[slot] = block;
  }

  public int length() {
    return blocks.length;
  }

  public int size() {
    int size = 0;
    for(int slot = 0; slot < blocks.length; slot++) {
      BlockData block = get(slot);
      if(block != null) {
        size += block.length();
      }
    }
    if(getSize() != size) {
      setSize(size);
      write();
    }
    return size;
  }

  @Override
  public String toString() {
    String output = "f:";

    output += TinyDOS.AddPostPadding(getName(), 8, " ") + " ";
    output += TinyDOS.AddPrePadding(String.valueOf(getSize()), 4, "0") + ":";

    for(int slot = 0; slot < length(); slot++) {
      int address = 0;
      if(blocks[slot] != null) {
        address = blocks[slot].getAddress();
      }
      output += TinyDOS.AddPrePadding(String.valueOf(address), 3, "0") + " ";
    }

    return output;
  }
}
