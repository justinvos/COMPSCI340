/**
 * Volume
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class Volume extends Drive {

  private BlockVolumeInfo volumeInfo;

  public Volume(String label) {
    super(label);
  }

  @Override
  public void format() {
    super.format();

    volumeInfo = new BlockVolumeInfo();
    writeBlock(0, volumeInfo);
    System.out.println("Vol format");
  }

  @Override
  public void load() {
    super.load();

    volumeInfo = BlockVolumeInfo.Parse(readBlock(0).getContent());
    System.out.println("Vol load");

    System.out.println(getRoot().getEntry(0).getFileName());
  }

  public BlockDirectory getRoot() {
    return volumeInfo.getDirectory();
  }
}
