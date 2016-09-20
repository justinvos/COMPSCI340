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
  private String content;

  public Block() {
    this.content = "";
  }

  public Block(String content) {
    this.content = content;
  }

  public static Block Parse(String line) {
    return new Block(line);
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
