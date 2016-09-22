import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Drive
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class Drive {
  public static int START_BLOCK_ADDRESS = 0;
  public static int END_BLOCK_ADDRESS = 127;

  public static int BLOCK_NUM = 128;
  public static String EMPTY_BLOCK = "";

  private String label;
  private File file;

  private String[] memory;

  public Drive(String label) {
    this.label = label;

    file = new File(label + ".drive");
    memory = new String[Drive.BLOCK_NUM];


    if(file.length() == 0) { // is empty
      System.out.println("DRIVE EMPTY");
      //format();
    }
  }

  private boolean isReady() {
    return file.length() != 0;
  }

  private boolean blockExists(int address) {
    return address >= Drive.START_BLOCK_ADDRESS && address <= Drive.END_BLOCK_ADDRESS;
  }

  public void format() {
    for(int address = START_BLOCK_ADDRESS; address <= Drive.END_BLOCK_ADDRESS; address++) {
      memory[address] = Block.EMPTY;
    }
    flush();
  }

  public void load() {
    if(isReady()) {
      try {
        Scanner in = new Scanner(file);
        for(int address = Drive.START_BLOCK_ADDRESS; address <= Drive.END_BLOCK_ADDRESS; address++) {
          memory[address] = in.nextLine();
          in.nextLine();
        }
      } catch(FileNotFoundException e) {
        System.out.println(e);
      }
    }
  }

  public void flush() {
    try {
      PrintWriter out = new PrintWriter(file);
      for(int i = Drive.START_BLOCK_ADDRESS; i <= Drive.END_BLOCK_ADDRESS; i++) {
        out.println(memory[i]);
        out.println("** " + i + " **");
      }
      out.close();
    } catch(FileNotFoundException e) {
      System.out.println(e);
    }
  }

  public void write(Block block) {
    memory[block.getAddress()] = block.toString();
    flush();
  }

  public String read(int address) {
    return memory[address];
  }

  public String getLabel() {
    return label;
  }



  public int getEmptyBlock() {
    for(int address = Drive.START_BLOCK_ADDRESS; address <= Drive.END_BLOCK_ADDRESS; address++) {
      if(memory[address].equals(Block.EMPTY)) {
        return address;
      }
    }
    return -1;
  }
}
