public class BlockData extends Block {
  private String data;

  public BlockData(int address) {
    super(address);
    this.data = read();
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    return data;
  }
}
