public class Volume extends Drive {

  public Volume(String label) {
    super(label);
  }

  @Override
  public void format() {
    super.format();

    writeBlock(0, new BlockVolumeInfo());
    System.out.println("Vol format");
  }
}
