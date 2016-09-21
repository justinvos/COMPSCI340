public class TinyDOS {
  public static Volume volume;

  public static void main(String[] args) {
    System.out.println("TinyDOS");

    volume = new Volume("test");
    volume.load();

    System.out.println(volume.getRoot().get(0));

    //volume.getRoot().mkfile("a");
  }


  // HELPER METHODS

  public static String AddPrePadding(String content, int length, String padding) {

    while(content.length() < length) {
      content = padding + content;
    }

    return content;
  }

  public static String AddPostPadding(String content, int length, String padding) {

    while(content.length() < length) {
      content = content + padding;
    }

    return content;
  }
}
