public class BlockVolumeInfo extends BlockDirectory {
  public static int VOLUME_INFO_ADDRESS = 0;
  public static int VOLUME_INFO_ENTRY_NUM = 6;

  private boolean[] bitmap;

  public BlockVolumeInfo() {
    super(BlockVolumeInfo.VOLUME_INFO_ADDRESS, BlockVolumeInfo.VOLUME_INFO_ENTRY_NUM);
    bitmap = new boolean[Drive.BLOCK_NUM];
  }

  public static BlockVolumeInfo Parse() {
    BlockVolumeInfo volumeInfo = new BlockVolumeInfo();

    String line = volumeInfo.read();

    String stringBitmap = line.substring(Drive.START_BLOCK_ADDRESS, Drive.END_BLOCK_ADDRESS + 1);
    for(int address = 0; address < stringBitmap.length(); address++) {
      volumeInfo.setBitmap(address, stringBitmap.substring(address, address + 1).equals("+"));
    }


    int startPosition = Drive.END_BLOCK_ADDRESS + 1;
    for(int index = 0; index < volumeInfo.length(); index++) {
      volumeInfo.set(index, Entry.Parse(volumeInfo, index, line.substring(startPosition + index * Entry.LENGTH, startPosition + (index + 1) * Entry.LENGTH)));
    }

    return volumeInfo;
  }

  public boolean getBitmap(int address) {
    return bitmap[address];
  }

  public void setBitmap(int address, boolean inUse) {
    bitmap[address] = inUse;
  }

  @Override
  public String toString() {
    String output = "";

    for(int i = Drive.START_BLOCK_ADDRESS; i <= Drive.END_BLOCK_ADDRESS; i++) {
      if(getBitmap(i)) {
        output = output + "+";
      } else {
        output = output + "-";
      }
    }

    output += super.toString();

    return output;
  }
}
