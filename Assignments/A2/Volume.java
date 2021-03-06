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

  private EntryDirectory root;
  private BlockVolumeInfo info;

  public Volume(String label) {
    super(label);
  }

  @Override
  public void format() {
    super.format();

    info = new BlockVolumeInfo();
    info.write();

    root = new EntryDirectory(info);
    System.out.println("Vol format");
  }

  @Override
  public void load() {
    super.load();

    info = BlockVolumeInfo.Parse();
    root = new EntryDirectory(info);
  }

  @Override
  public void write(Block block) {
    getInfo().setBitmap(block.getAddress(), !block.isEmpty());
    getInfo().size();
    if(block.getAddress() != 0) {
      getInfo().write();
    }

    super.write(block);
  }

  public EntryDirectory getRoot() {
    return root;
  }

  public BlockVolumeInfo getInfo() {
    return info;
  }

  public Entry getEntry(String path) {
    path = TinyDOS.TrimSlash(path);

    if(path.length() == 0) {
      return getRoot();
    }

    String[] components = path.split("/");
    LogicalDirectory directory = new LogicalDirectory(getRoot());
    for(int level = 0; level < components.length; level++) {
      Entry child = directory.getChild(components[level]);
      if(child == null) {
        return null;
      }
      if(level == components.length - 1) {
        return child;
      } else if(child instanceof EntryDirectory) {
        directory = new LogicalDirectory((EntryDirectory)child);
      }
    }
    return null;
  }
}
