import java.util.Scanner;

public class TinyDOS {
  public static boolean active = true;
  public static Volume currentVolume;

  public static void main(String[] args) {
    System.out.println("TinyDOS");

    currentVolume = new Volume("justin");

    Scanner in = new Scanner(System.in);

    while(active) {
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


  // HELPER METHOD

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
