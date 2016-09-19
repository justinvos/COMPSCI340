public class DirectoryEntry {
  public static int NUM_BLOCK_ADDRESSES = 12;

  private boolean isDirectory;
  private String fileName;
  private int[] addresses;

  public DirectoryEntry() {
    this.isDirectory = false;
    setFileName("");
    setAddresses(new int[DirectoryEntry.NUM_BLOCK_ADDRESSES]);
  }

  public DirectoryEntry(boolean isDirectory, String fileName) {
    this.isDirectory = isDirectory;
    setFileName(fileName);
    setAddresses(new int[DirectoryEntry.NUM_BLOCK_ADDRESSES]);
  }

  public DirectoryEntry(boolean isDirectory, String fileName, int[] addresses) {
    this.isDirectory = isDirectory;
    setFileName(fileName);
    setAddresses(addresses);
  }

  public static DirectoryEntry Parse(String line) {
    boolean isDirectory = line.substring(0, 2) == "d:";
    String fileName = line.substring(2, 9);
    String[] stringAddresses = line.substring(16, 63).split(" ");
    int[] addresses = new int[DirectoryEntry.NUM_BLOCK_ADDRESSES];

    for(int i = 0; i < stringAddresses.length; i++) {
      addresses[i] = Integer.parseInt(stringAddresses[i]);
    }

    return new DirectoryEntry(isDirectory, fileName, addresses);
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setAddresses(int[] addresses) {
    if(addresses.length == 12) {
      this.addresses = addresses;
    }
  }

  public int length() {
    return 0;
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
    output = output + TinyDOS.AddPrePadding(String.valueOf(length()), 4, "0") + ":";

    for(int i = 0; i < addresses.length; i++) {
      output = output + TinyDOS.AddPrePadding(String.valueOf(addresses[i]), 3, "0") + " ";
    }

    return output;
  }


}
