/**
 * BlockData
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class BlockData extends Block {
  private String data;

  public BlockData(int address) {
    super(address);
    this.data = read();
  }

  public BlockData(int address, String data) {
    super(address);
    setData(data);
  }

  public void setData(String data) {
    this.data = data;
    write();
  }

  public int length() {
    return data.length();
  }

  @Override
  public boolean isEmpty() {
    return data.equals(Block.EMPTY);
  }

  @Override
  public String toString() {
    return data;
  }
}
