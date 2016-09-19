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
  public static int ROOT_NUM_ENTRIES = 6;

  private boolean[] bitmap;

  private BlockDirectory directory;

  public BlockVolumeInfo() {
    super();

    bitmap = new boolean[Drive.BLOCK_NUM];
    bitmap[Drive.START_BLOCK_ADDRESS] = true;

    directory = new BlockDirectory(BlockVolumeInfo.ROOT_NUM_ENTRIES);

    setContent(this.toString());
  }

  public BlockVolumeInfo(boolean[] bitmap, BlockDirectory directory) {
    super("");

    this.bitmap = bitmap;
    this.directory = directory;

    setContent(this.toString());
  }

  public static BlockVolumeInfo Parse(String line) {

    boolean[] bitmap = new boolean[Drive.BLOCK_NUM];
    String stringBitmap = line.substring(Drive.START_BLOCK_ADDRESS, Drive.END_BLOCK_ADDRESS + 1);
    for(int i = 0; i < stringBitmap.length(); i++) {
      bitmap[i] = stringBitmap.substring(i, i+1).equals("+");
    }

    BlockDirectory directory = BlockDirectory.Parse(line.substring(Drive.END_BLOCK_ADDRESS + 1));

    return new BlockVolumeInfo(bitmap, directory);
  }

  public BlockDirectory getDirectory() {
    return directory;
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

    output = output + directory.toString();

    return output;
  }
}
