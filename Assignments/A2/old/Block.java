/**
 * Block
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class Block {
  public static int MAX_LENGTH = 512;

  private int address;
  private String content;

  public Block(int address) {
    this(address, Drive.EMPTY_BLOCK);
  }

  public Block(int address, String content) {
    this.address = address;
    this.content = content;
  }

  public static Block Parse(int address, String line) {
    return new Block(address, line);
  }

  public int getAddress() {
    return address;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return content;
  }
}
