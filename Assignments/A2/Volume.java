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

  public BlockDirectory getRoot() {
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
