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

  public static void main(String[] args) {


    Scanner in = new Scanner(System.in);

    while(active) {
      System.out.print("TinyDOS: ");
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
        } else {
          System.out.println("Incorrect number of arguments");
        }
        break;
      case "reconnect":
        if(input.length == 2) {
          System.out.println("Reconnecting to " + input[1]);
          currentVolume = new Volume(input[1]);
          currentVolume.load();

          System.out.println(currentVolume.readBlock(0).getContent());
        } else {
          System.out.println("Incorrect number of arguments");
        }
        break;
      case "ls":
        System.out.println("type size name");
        System.out.println("------------------");

        for(int i = 0; i < currentVolume.getLogicalRoot().length(); i++) {
          DirectoryEntry entry = currentVolume.getLogicalRoot().getEntry(i);

          if(!entry.getFileName().trim().equals("")) {
            if(entry.isDirectory()) {
              System.out.print("dir  ");
            } else {
              System.out.print("file ");
            }
            System.out.println(TinyDOS.AddPrePadding(String.valueOf(entry.size()), 4, "0") + " " + entry.getFileName());
          }
        }
        break;
      case "print":
        if(input.length == 2) {
          String path = input[1];
          LogicalFile file = LogicalFile.Extract(currentVolume, path);
          System.out.println(file.getContent());
        } else {
          System.out.println("Incorrect number of arguments");
        }
        break;
      case "append":
        if(input.length == 3) {
          if(input[2].startsWith("\"") && input[2].endsWith("\"")) {
            String path = input[1];
            String data = input[2].substring(1, input[2].length() - 1);

            System.out.println("PATH: " + path + " + " + data);

            DirectoryEntry entry = currentVolume.getDirectoryEntry(path);

            LogicalFile file = LogicalFile.Extract(currentVolume, entry);
            file.appendContent(data);

            currentVolume.write(entry, file);


          } else {
            System.out.println("Data argument should be enclosed in speech marks e.g. \"data\"");
          }
        } else {
          System.out.println("Incorrect number of arguments");
        }
        break;
      case "quit":
        TinyDOS.active = false;
        System.out.println("Quiting TinyDOS");
        break;
      case "write":
        int address = Integer.parseInt(input[1]);
        currentVolume.writeBlock(address, new Block(address, input[2]));
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
