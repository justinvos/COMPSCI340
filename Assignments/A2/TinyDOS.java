import java.util.Scanner;


/**
 * TinyDOS is the entry-point for the application.
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class TinyDOS {
  public static boolean active = true;
  public static Volume currentVolume;
  public static BlockDirectory currentDirectory;

  public static void main(String[] args) {
    System.out.println("TinyDOS");

    Scanner in = new Scanner(System.in);

    while(active) {
      if(currentVolume != null) {
        System.out.print(currentVolume.getLabel());
      }

      System.out.print("$ ");
      handleInput(in.nextLine().split(" "));
    }
  }

  public static void handleInput(String[] input) {
    String command = input[0];

    switch(command) {
      case "format":
        if(input.length == 2) {
          System.out.println("Formatting " + input[1]);
          currentVolume = new Volume(input[1]);
          currentVolume.format();
          currentDirectory = currentVolume.getRoot();
        } else {
          System.out.println("Incorrect number of arguments");
        }
        break;
      case "reconnect":
        if(input.length == 2) {
          System.out.println("Reconnecting to " + input[1]);
          currentVolume = new Volume(input[1]);
          currentVolume.load();
          currentDirectory = currentVolume.getRoot();

          System.out.println(currentVolume.readBlock(0).getContent());
        } else {
          System.out.println("Incorrect number of arguments");
        }
        break;
      case "ls":
        System.out.println("type size name");
        System.out.println("------------------");

        for(int i = 0; i < currentDirectory.length(); i++) {
          DirectoryEntry entry = currentDirectory.getEntry(i);

          if(!entry.getFileName().equals("")) {
            if(entry.isDirectory()) {
              System.out.print("dir  ");
            } else {
              System.out.print("file ");
            }
            System.out.println(TinyDOS.AddPrePadding(String.valueOf(entry.size()), 4, "0") + " " + entry.getFileName());
          }
        }
        break;
      case "size":
        System.out.println(currentVolume.getRoot().size());
        break;
      case "quit":
        TinyDOS.active = false;
        System.out.println("Quiting TinyDOS");
        break;
      case "write":
        currentVolume.writeBlock(Integer.parseInt(input[1]), new Block(input[2]));
        break;
      case "read":
        System.out.println(currentVolume.readBlock(Integer.parseInt(input[1])).getContent());
        break;
      default:
        System.out.println(input[0] + ": command not found");
        break;
    }
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
