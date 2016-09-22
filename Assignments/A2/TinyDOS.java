public class TinyDOS {
  public static boolean isActive = true;
  public static Volume volume;

  public static void main(String[] args) {
    System.out.println("TinyDOS");

    reconnect("test");
    ls("/d/");
    print("/d/info");
    //mkfile("/d/info");
    //append("/d/info", "DONE");
    //delfile("/d/info");
    deldir("/d/");

    //LogicalFile logicalFile = new LogicalFile((EntryFile)volume.getEntry("/d/info"));
    //logicalFile.setContent("Hi again.");
  }

  public static void format(String label) {
    volume = new Volume(label);
    volume.format();
  }

  public static void reconnect(String label) {
    volume = new Volume(label);
    volume.load();
  }

  public static void ls(String directoryPath) {
    EntryDirectory entryDirectory = (EntryDirectory)volume.getEntry(directoryPath);

    if(entryDirectory != null) {
      LogicalDirectory logicalDirectory = new LogicalDirectory(entryDirectory);

      System.out.println(logicalDirectory);
    } else {
      System.out.println(directoryPath + " could not be found");
    }
  }

  public static void mkfile(String filePath) {
    String directoryPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

    System.out.println("Directory:" + directoryPath);
    System.out.println("File name:" + fileName);

    EntryDirectory entryDirectory = (EntryDirectory)volume.getEntry(directoryPath);

    if(entryDirectory != null) {
      LogicalDirectory logicalDirectory = new LogicalDirectory(entryDirectory);

      EntryFile entryFile = logicalDirectory.makeFile(fileName);

      if(entryFile != null) {
        System.out.println(filePath + " was created");
      } else {
        System.out.println(filePath + " was not created");
      }
    } else {
      System.out.println(directoryPath + " could not be found");
    }
  }

  public static void mkdir(String fullDirectoryPath) {
    String directoryPath = TinyDOS.TrimSlash(fullDirectoryPath);
    String parentDirectoryPath = directoryPath.substring(0, directoryPath.lastIndexOf("/") + 1);
    String directoryName = directoryPath.substring(directoryPath.lastIndexOf("/") + 1);

    System.out.println("Parent directory:" + parentDirectoryPath);
    System.out.println("Directory name:" + directoryName);

    EntryDirectory entryParentDirectory = (EntryDirectory)volume.getEntry(parentDirectoryPath);

    if(entryParentDirectory != null) {
      LogicalDirectory logicalDirectory = new LogicalDirectory(entryParentDirectory);

      EntryDirectory entryDirectory = logicalDirectory.makeDirectory(directoryName);

      if(entryDirectory != null) {
        System.out.println(fullDirectoryPath + " was created");
      } else {
        System.out.println(fullDirectoryPath + " was not created");
      }
    } else {
      System.out.println(parentDirectoryPath + " could not be found");
    }
  }

  public static void append(String path, String data) {

    EntryFile entryFile = (EntryFile)volume.getEntry(path);

    if(entryFile != null) {
      LogicalFile logicalFile = new LogicalFile(entryFile);
      logicalFile.appendContent(data);
      System.out.println("Appended successfully");
    } else {
      System.out.println(path + " was not found");
    }
  }

  public static void print(String filePath) {
    EntryFile entryFile = (EntryFile)volume.getEntry(filePath);

    if(entryFile != null) {
      LogicalFile logicalFile = new LogicalFile(entryFile);

      System.out.println(logicalFile);
    } else {
      System.out.println(filePath + " could not be found");
    }
  }

  public static void delfile(String filePath) {
    EntryFile entryFile = (EntryFile)volume.getEntry(filePath);

    if(entryFile != null) {
      LogicalFile logicalFile = new LogicalFile(entryFile);
      logicalFile.delete();
      System.out.println("Deleted successfully");
    } else {
      System.out.println(filePath + " could not be found");
    }
  }

  public static void deldir(String directoryPath) {
    EntryDirectory entryDirectory = (EntryDirectory)volume.getEntry(directoryPath);

    if(entryDirectory != null) {
      LogicalDirectory logicalDirectory = new LogicalDirectory(entryDirectory);
      logicalDirectory.delete();
      System.out.println("Deleted successfully");
    } else {
      System.out.println(directoryPath + " could not be found");
    }
  }

  public static void quit() {
    isActive = false;
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

  public static String TrimSlash(String content) {
    String temp = content;
    if(content.startsWith("/")) {
      temp = temp.substring(1);
    }
    if(content.endsWith("/")) {
      temp = temp.substring(0, content.length() - 1);
    }
    return temp;
  }
}
