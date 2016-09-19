public class BlockVolumeInfo extends Block {
  public static int NUM_DIRECTORY_ENTRIES = 6;
  public static int START_DIRECTORY_ENTRY = 0;
  public static int END_DIRECTORY_ENTRY = 5;

  private boolean[] bitmap;
  private DirectoryEntry[] directoryEntries;

  public BlockVolumeInfo() {
    super("");

    bitmap = new boolean[Drive.BLOCK_NUM];
    bitmap[Drive.START_BLOCK_ADDRESS] = true;

    directoryEntries = new DirectoryEntry[BlockVolumeInfo.NUM_DIRECTORY_ENTRIES];
    for(int i = BlockVolumeInfo.START_DIRECTORY_ENTRY; i <= BlockVolumeInfo.END_DIRECTORY_ENTRY; i++) {
      directoryEntries[i] = new DirectoryEntry();
    }

    setContent(this.toString());
  }

  public BlockVolumeInfo(boolean[] bitmap, DirectoryEntry[] directoryEntries) {
    super("");

    this.bitmap = bitmap;
    this.directoryEntries = directoryEntries;

    setContent(this.toString());
  }

  public void setDirectoryEntry(int i, DirectoryEntry directoryEntry) {
    if(i >= BlockVolumeInfo.START_DIRECTORY_ENTRY && i <= BlockVolumeInfo.END_DIRECTORY_ENTRY) {
      directoryEntries[i] = directoryEntry;
      setContent(this.toString());
    }
  }

  @Override
  public String toString() {
    String output = "";

    for(int i = Drive.START_BLOCK_ADDRESS; i <= Drive.END_BLOCK_ADDRESS; i++) {
      if(bitmap[i]) {
        output = output + "+";
      } else {
        output = output + "-";
      }
    }

    for(int i = BlockVolumeInfo.START_DIRECTORY_ENTRY; i <= BlockVolumeInfo.END_DIRECTORY_ENTRY; i++) {
      output = output + directoryEntries[i].toString();
    }

    return output;
  }
}
