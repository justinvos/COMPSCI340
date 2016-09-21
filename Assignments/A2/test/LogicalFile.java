/**
 * LogicalFile
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class LogicalFile {
  private String content;

  public LogicalFile(String content) {
    this.content = content;
  }

  public int length() {
    return content.length();
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void appendContent(String content) {
    this.content += content;
  }


  /*
  public Block[] getBlocks() {
    int length = (int)Math.ceil((double)content.length() / Block.MAX_LENGTH);

    System.out.println(length);

    Block[] blocks = new Block[length];

    for(int i = 0; i < length; i++) {
      blocks[i] = new Block(content.substring(i * Block.MAX_LENGTH, Math.min((i + 1) * Block.MAX_LENGTH, content.length())));
    }

    return blocks;
  }*/
}
