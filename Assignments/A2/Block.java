public class Block {
  public static int MAX_LENGTH = 512;
  public static String EMPTY = "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";

  private int address;

  public Block(int address) {
    this.address = address;
  }

  public int getAddress() {
    return address;
  }

  public String read() {
    return TinyDOS.volume.read(getAddress());
  }

  public void write() {
    TinyDOS.volume.write(this);
  }

  public boolean isEmpty() {
    return true;
  }

  @Override
  public String toString() {
    return Block.EMPTY;
  }
}
