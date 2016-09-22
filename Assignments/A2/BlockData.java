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

  @Override
  public boolean isEmpty() {
    return data.equals(Block.EMPTY);
  }

  @Override
  public String toString() {
    return data;
  }
}
