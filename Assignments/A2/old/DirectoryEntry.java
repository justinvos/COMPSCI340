/**
 * DirectoryEntry
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class DirectoryEntry {
  public static int NUM_BLOCK_ADDRESSES = 12;
  public static int LENGTH = 64;

  private Directory parent;
  private boolean isDirectory;
  private String fileName;
  private int size;
  private int[] addresses;

  public DirectoryEntry(Directory parent) {
    this(parent, false, "", 0, new int[DirectoryEntry.NUM_BLOCK_ADDRESSES]);
  }

  public DirectoryEntry(Directory parent, boolean isDirectory, String fileName) {
    this(parent, isDirectory, fileName, 0, new int[DirectoryEntry.NUM_BLOCK_ADDRESSES]);
  }

  public DirectoryEntry(Directory parent, boolean isDirectory, String fileName, int size, int[] addresses) {
    this.parent = parent;
    this.isDirectory = isDirectory;
    setFileName(fileName);
    setSize(size);
    setAddresses(addresses);
  }

  public static DirectoryEntry Parse(Directory parent, String line) {
    boolean isDirectory = line.substring(0, 2) == "d:";
    String fileName = line.substring(2, 10).trim();
    int size = Integer.parseInt(line.substring(11, 15));
    String[] stringAddresses = line.substring(16, 63).split(" ");
    int[] addresses = new int[DirectoryEntry.NUM_BLOCK_ADDRESSES];

    for(int i = 0; i < stringAddresses.length; i++) {
      addresses[i] = Integer.parseInt(stringAddresses[i]);
    }

    return new DirectoryEntry(parent, isDirectory, fileName, size, addresses);
  }

  public Directory getParent() {
    return parent;
  }

  public boolean isDirectory() {
    return isDirectory;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int length() {
    return addresses.length;
  }

  public int getAddress(int i) {
    return addresses[i];
  }

  public void setAddress(int index, int address) {
    addresses[index] = address;
  }

  public void setAddresses(int[] addresses) {
    if(addresses.length == 12) {
      this.addresses = addresses;
    }
  }

  public void removeAddress(int address) {
    for(int index = 0; index < length(); index++) {
      if(getAddress(index) == address) {
        setAddress(index, address);
      }
    }
  }

  public int size() {
    if(isDirectory) {
      return 0; // TODO
    } else {
      return size;
    }
  }

  @Override
  public String toString() {
    String output = "";

    if(isDirectory) {
      output = "d:";
    } else {
      output = "f:";
    }

    output = output + TinyDOS.AddPostPadding(fileName, 8, " ") + " ";
    output = output + TinyDOS.AddPrePadding(String.valueOf(this.size()), 4, "0") + ":";

    for(int i = 0; i < addresses.length; i++) {
      output = output + TinyDOS.AddPrePadding(String.valueOf(addresses[i]), 3, "0") + " ";
    }

    return output;
  }


}
