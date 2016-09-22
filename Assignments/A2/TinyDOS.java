public class TinyDOS {
  public static Volume volume;

  public static void main(String[] args) {
    System.out.println("TinyDOS");

    volume = new Volume("test");
    volume.load();


    LogicalDirectory dir = new LogicalDirectory(volume.getRoot());
    System.out.println(dir);


    System.out.println(dir.makeDirectory("d"));
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
