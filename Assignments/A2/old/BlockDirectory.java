/**
 * BlockDirectory
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class BlockDirectory extends Block {

  public Directory directory;

  public BlockDirectory() {
    this(Directory.DEFAULT_NUM_ENTRIES);
  }

  public BlockDirectory(int numEntries) {
    super(-1);
    directory = new Directory(this, numEntries);
  }

  public BlockDirectory(DirectoryEntry[] entries) {
    super(-1);
    this.directory = new Directory(this, entries);
  }

  public BlockDirectory(Directory directory) {
    super(-1);
    this.directory = directory;
  }

  public static BlockDirectory Parse(String line) {
    BlockDirectory blockDirectory = new BlockDirectory();
    Directory directory = Directory.Parse(blockDirectory, line);
    blockDirectory.setDirectory(directory);
    return blockDirectory;
  }

  public Directory getDirectory() {
    return directory;
  }

  public void setDirectory(Directory directory) {
    this.directory = directory;
  }

  public int size() {
    return getDirectory().size();
  }

  @Override
  public String toString() {
    return directory.toString();
  }
}
