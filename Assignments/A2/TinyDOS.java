import java.util.Scanner;
import java.util.NoSuchElementException;
/**
 * TinyDOS
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class TinyDOS {
  public static boolean active = true;
  public static Volume volume;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    while(active) {
      System.out.print("TinyDOS: ");
      if(volume != null) {
        System.out.print(volume.getLabel());
      }

      System.out.print("$ ");
      try {
        String input = in.nextLine();
        if(input.length() > 0) {
          handleInput(input);
        }
      } catch(NoSuchElementException e) {
        System.out.println();
        quit();
      }

      System.out.println();
    }
  }

  public static void handleInput(String line) {
    System.out.println();

    String[] arg = line.split(" ");
    String command = arg[0];

    switch(command) {
      case "format":
        if(arg.length == 2) {
          format(arg[1]);
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "reconnect":
        if(arg.length == 2) {
          reconnect(arg[1]);
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "ls":
        if(arg.length == 2) {
          if(volume != null) {
            ls(arg[1]);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "mkfile":
        if(arg.length == 2) {
          if(volume != null) {
            mkfile(arg[1]);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "mkdir":
        if(arg.length == 2) {
          if(volume != null) {
            mkdir(arg[1]);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "append":
        if(arg.length >= 3) {
          if(volume != null) {
            String filePath = arg[1];
            String data = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
            append(filePath, data);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "print":
        if(arg.length == 2) {
          if(volume != null) {
            print(arg[1]);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "delfile":
        if(arg.length == 2) {
          if(volume != null) {
            delfile(arg[1]);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "deldir":
        if(arg.length == 2) {
          if(volume != null) {
            deldir(arg[1]);
          } else {
            System.out.println("No volume has been loaded");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
      break;
      case "quit":
        quit();
      break;
      default:
        System.out.println(arg[0] + ": command not found");
      break;
    }
  }

  public static void format(String label) {
    volume = new Volume(label);
    volume.format();
    System.out.println("Formatting " + label);
  }

  public static void reconnect(String label) {
    volume = new Volume(label);
    volume.load();
    System.out.println("Reconnecting to " + label);
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
    fileName = fileName.substring(0, Math.min(fileName.length(), Entry.MAX_NAME_LENGTH));

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
    directoryName = directoryName.substring(0, Math.min(directoryName.length(), Entry.MAX_NAME_LENGTH));

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
    active = false;
    System.out.println("Quitting");
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
