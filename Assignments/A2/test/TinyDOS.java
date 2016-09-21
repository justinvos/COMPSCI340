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
      //handleInput(in.nextLine().split(" "));
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
