public class Entry {
  public static int NUM_ADDRESSES = 12;

  private PhysicalDirectory parent;

  private boolean isDirectory;
  private String name;
  private int size;
  private int[] addresses;

  public Entry(PhysicalDirectory parent, String line) {
    this.parent = parent;
    isDirectory = line.substring(0, 2).equals("d:");
    name = line.substring(2, 10).trim();
    size = Integer.parseInt(line.substring(11, 15));
    String[] stringAddresses = line.substring(16, 63).split(" ");
    addresses = new int[DirectoryEntry.NUM_BLOCK_ADDRESSES];

    for(int i = 0; i < stringAddresses.length; i++) {
      addresses[i] = Integer.parseInt(stringAddresses[i]);
    }
  }

  public PhysicalDirectory getParent() {
    return parent;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    getParent().write();
  }

  public int size() {
    return size;
  }

  public int getAddress(int addressIndex) {
    return addresses[addressIndex];
  }

  public int findEmptyAddress() {
    for(int addressIndex = 0; addressIndex < addresses.length; addressIndex++) {
      if(getAddress(addressIndex) == 0) {
        return addressIndex;
      }
    }
    return -1;
  }

  public Entry findEmptyEntry() {
    for(int addressIndex = 0; addressIndex < addresses.length; addressIndex++) {
      int address = getAddress(addressIndex);
      PhysicalDirectory physicalDirectory = new PhysicalDirectory(Test.volume.getBlock(address));

      Entry emptyEntry = physicalDirectory.findEmptyEntry();
      if(emptyEntry != null) {
        return emptyEntry;
      }
    }
    return null;
  }


  @Override
  public String toString() {
    String output = "";

    if(isDirectory) {
      output = "d:";
    } else {
      output = "f:";
    }

    output = output + TinyDOS.AddPostPadding(getName(), 8, " ") + " ";
    output = output + TinyDOS.AddPrePadding(String.valueOf(this.size()), 4, "0") + ":";

    for(int i = 0; i < addresses.length; i++) {
      output = output + TinyDOS.AddPrePadding(String.valueOf(getAddress(i)), 3, "0") + " ";
    }

    return output;
  }
}
