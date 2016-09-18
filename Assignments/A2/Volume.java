public class Volume extends Drive {

  public Volume(String label) {
    super(label);
  }

  @Override
  public void format() {
    super.format();

    System.out.println("Vol format");
  }
}
