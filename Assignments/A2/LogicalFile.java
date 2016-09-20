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

  public static LogicalFile Extract(Volume volume, String path) {
    return LogicalFile.Extract(volume, volume.getDirectoryEntry(path));
  }

  public static LogicalFile Extract(Volume volume, DirectoryEntry entry) {
    String content = "";

    for(int i = 0; i < entry.length(); i++) {
      int address = entry.getAddress(i);
      if(address != 0) {
        content += volume.readBlock(address).getContent();
      }
    }

    return new LogicalFile(content);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
