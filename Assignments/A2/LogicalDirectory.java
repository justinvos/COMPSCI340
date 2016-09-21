import java.util.ArrayList;

public class LogicalDirectory {
  private EntryDirectory entry;

  public ArrayList<Entry> children;

  public LogicalDirectory(EntryDirectory entry) {
    this.entry = entry;

    children = new ArrayList<Entry>();

    for(int slot = 0; slot < entry.length(); slot++) {
      BlockDirectory block = entry.get(slot);
      if(block != null) {
        for(int index = 0; index < block.length(); index++) {
          Entry child = block.get(index);
          if(!child.getName().equals("")) {
            children.add(child);
          }
        }
      }
    }
  }
}
