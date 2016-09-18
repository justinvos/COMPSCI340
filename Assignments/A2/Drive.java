import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Drive {
  public static int START_BLOCK_ADDRESS = 0;
  public static int END_BLOCK_ADDRESS = 127;

  public static int BLOCK_NUM = 128;
  public static String EMPTY_BLOCK = "";

  private String label;
  private File file;

  private Block[] blocks;

  public Drive(String label) {
    this.label = label;

    file = new File("/home/vos/Documents/university/COMPSCI340/Assignments/A2/" + label + ".drive");
    blocks = new Block[Drive.BLOCK_NUM];


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
    for(int i = START_BLOCK_ADDRESS; i <= Drive.END_BLOCK_ADDRESS; i++) {
      blocks[i] = new Block(Drive.EMPTY_BLOCK);
    }
    flush();

    /*
    try {
      PrintWriter out = new PrintWriter(file);
      for(int i = START_BLOCK_ADDRESS; i <= Drive.END_BLOCK_ADDRESS; i++) {
        out.println(Drive.EMPTY_BLOCK);
        out.println("** " + i + " **");
      }
      out.close();
    } catch(FileNotFoundException e) {
      System.out.println(e);
    }
    */
  }

  public void load() {
    if(isReady()) {
      try {
        Scanner in = new Scanner(file);
        for(int i = Drive.START_BLOCK_ADDRESS; i <= Drive.END_BLOCK_ADDRESS; i++) {
          blocks[i] = new Block(in.nextLine());
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
        out.println(blocks[i].getContent());
        out.println("** " + i + " **");
      }
      out.close();
    } catch(FileNotFoundException e) {
      System.out.println(e);
    }
  }


  public String getLabel() {
    return label;
  }

  public void writeBlock(int address, Block block) {
    if(isReady()) {
      if(blockExists(address)) {
        blocks[address] = block;
        flush();
      }
    }
  }

  public Block readBlock(int address) {
    if(isReady()) {
      return blocks[address];
    } else {
      return null;
    }
  }
}
