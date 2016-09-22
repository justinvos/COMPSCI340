public class TinyDOS {
  public static Volume volume;

  public static void main(String[] args) {
    System.out.println("TinyDOS");

    volume = new Volume("test");
    volume.load();

    //System.out.println(volume.getRoot().get(0));

    LogicalDirectory dir = new LogicalDirectory(volume.getRoot());

    System.out.println(dir);

    /*Entry entry = dir.getChild("b");

    if(entry.isFile()) {
      EntryFile entryFile = (EntryFile)entry;

      System.out.println("isfile");
      System.out.println(entryFile);

      LogicalFile file = new LogicalFile(entryFile);
      System.out.println("|" + file + "|");
      //file.setContent("what is this" + Block.EMPTY + " huh");
    }*/



    //System.out.println(volume.getRoot().get(0));
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
