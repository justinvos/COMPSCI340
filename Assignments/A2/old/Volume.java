import java.util.ArrayList;
/**
 * Volume
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class Volume extends Drive {

  private BlockVolumeInfo volumeInfo;

  public Volume(String label) {
    super(label);
  }

  @Override
  public void format() {
    super.format();

    volumeInfo = new BlockVolumeInfo();
    writeBlock(0, volumeInfo);
    System.out.println("Vol format");
  }

  @Override
  public void load() {
    super.load();

    volumeInfo = BlockVolumeInfo.Parse(readBlock(0).getContent());
    System.out.println("Vol load");

    System.out.println(getLogicalRoot().length());
  }

  public LogicalDirectory getLogicalRoot() {
    ArrayList<DirectoryEntry> entries = new ArrayList<DirectoryEntry>();

    for(int i = 0; i < volumeInfo.getDirectory().length(); i++) {
      DirectoryEntry entry = volumeInfo.getDirectory().getEntry(i);

      if(!entry.getFileName().trim().equals("")) {
        entries.add(entry);
      }
    }

    return new LogicalDirectory(entries);
  }

  public Directory getRoot() {
    return volumeInfo.getDirectory();
  }

  public DirectoryEntry getDirectoryEntry(String path) {
    if(path.startsWith("/")) {
      path = path.substring(1);
    }
    String[] pathParts = path.split("/");
    LogicalDirectory dir = getLogicalRoot();
    for(int i = 0; i < pathParts.length - 1; i++) {
      if(dir.contains(pathParts[i])) {
        dir = LogicalDirectory.Extract(this, dir.getEntry(pathParts[i]));
      }
    }
    DirectoryEntry dirEntry = dir.getEntry(pathParts[pathParts.length - 1]);
    return dirEntry;
  }

  public int getFirstEmptyBlock() {
    for(int address = Drive.START_BLOCK_ADDRESS; address < Drive.END_BLOCK_ADDRESS; address++) {
      if(volumeInfo.isBlockEmpty(address)) {
        return address;
      }
    }
    return -1;
  }


  public void write(DirectoryEntry entry, LogicalFile file) {
    int length = (int)Math.ceil((double)file.length() / Block.MAX_LENGTH);
    String[] chunks = new String[length];
    for(int i = 0; i < length; i++) {
      chunks[i] = file.getContent().substring(i * Block.MAX_LENGTH, Math.min((i + 1) * Block.MAX_LENGTH, file.length()));
    }

    for(int entryIndex = 0; entryIndex < entry.length(); entryIndex++) {
      int address = entry.getAddress(entryIndex);

      if(entryIndex < chunks.length) {
        writeBlock(address, new Block(address, chunks[entryIndex]));
      } else {
        writeBlock(address, new Block(address));
        volumeInfo.setBitmap(address, false);
        writeBlock(0, volumeInfo);
        entry.removeAddress(address); //TODO writeBlock

        Directory parentDir = entry.getParent();
        Block block = parentDir.getBlock();
        writeBlock(block.getAddress(), block);
      }
    }
  }
  /*
  public void write(DirectoryEntry entry, Block[] blocks) {
    for(int entryIndex = 0; entryIndex < entry.length(); entryIndex++) {
      int address = entry.getAddress(entryIndex);
      if(address == 0) {
        address = getFirstEmptyBlock();
      }
      if(entryIndex < blocks.length) {
        writeBlock(address, blocks[entryIndex]);
      } else {
        writeBlock(address, new Block());
        volumeInfo.setBitmap(address, false);
        writeBlock(0, volumeInfo);
        entry.removeAddress(address); //TODO writeBlock
        //writeBlock();
      }
    }
  }
  */

  /*
  public DirectoryEntry getDirectoryEntry(String path) {
    if(!path.startsWith("/")) {
      return null;
    }

    String[] pathParts = path.split("/");

    BlockDirectory currentDirectory = getRoot();

    for(int i = 1; i < pathParts.length; i++) {
      if(currentDirectory.contains(pathParts[i])) {
        if(i == pathParts.length - 1) {
          return currentDirectory.getEntry(pathParts[i]);
        } else {
          currentDirectory = new BlockDirectory(currentDirectory.getEntry(pathParts[i]).);
        }
      }
    }
  }*/
}
