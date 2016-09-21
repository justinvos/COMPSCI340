/**
 * BlockVolumeInfo
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class BlockVolumeInfo extends Block {
  public static int ADDRESS = 0;
  public static int ROOT_NUM_ENTRIES = 6;

  private boolean[] bitmap;

  private Directory directory;

  public BlockVolumeInfo() {
    super(BlockVolumeInfo.ADDRESS);
    bitmap = new boolean[Drive.BLOCK_NUM];
    bitmap[Drive.START_BLOCK_ADDRESS] = true;

    directory = new Directory(this, BlockVolumeInfo.ROOT_NUM_ENTRIES);
  }

  public BlockVolumeInfo(boolean[] bitmap) {
    super(BlockVolumeInfo.ADDRESS);
    this.bitmap = bitmap;
    this.directory = new Directory(this, 0);
  }

  public BlockVolumeInfo(boolean[] bitmap, Directory directory) {
    super(BlockVolumeInfo.ADDRESS);
    this.bitmap = bitmap;
    this.directory = directory;
  }

  public static BlockVolumeInfo Parse(String line) {

    boolean[] bitmap = new boolean[Drive.BLOCK_NUM];
    String stringBitmap = line.substring(Drive.START_BLOCK_ADDRESS, Drive.END_BLOCK_ADDRESS + 1);
    for(int i = 0; i < stringBitmap.length(); i++) {
      bitmap[i] = stringBitmap.substring(i, i+1).equals("+");
    }

    BlockVolumeInfo volumeInfo = new BlockVolumeInfo(bitmap);

    Directory directory = Directory.Parse(volumeInfo, line.substring(Drive.END_BLOCK_ADDRESS + 1));

    volumeInfo.setDirectory(directory);

    return volumeInfo;
  }

  public boolean isBlockEmpty(int address) {
    return !bitmap[address];
  }

  public boolean getBitmap(int address) {
    return bitmap[address];
  }

  public void setBitmap(int address, boolean inUse) {
    bitmap[address] = inUse;
  }

  public Directory getDirectory() {
    return directory;
  }

  public void setDirectory(Directory directory) {
    this.directory = directory;
  }

  @Override
  public String getContent() {
    return this.toString();
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

    output = output + getDirectory().toString();

    return output;
  }
}
