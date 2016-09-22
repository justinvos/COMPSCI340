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

  public Entry getChild(int childIndex) {
    return children.get(childIndex);
  }

  public Entry getChild(String name) {
    int address = findChild(name);
    return address == -1 ? null : getChild(address);
  }

  public int length() {
    return children.size();
  }

  public int findChild(String name) {
    for(int childIndex = 0; childIndex < length(); childIndex++) {
      if(getChild(childIndex).getName().equals(name)) {
        return childIndex;
      }
    }
    return -1;
  }

  public boolean hasChild(String name) {
    return findChild(name) != -1;
  }

  public void makeFile(String name) {

  }

  public void makeDirectory(String name) {
    
  }

  @Override
  public String toString() {
    String output = "TYPE NAME     SIZE\n------------------";

    for(int childIndex = 0; childIndex < length(); childIndex++) {
      Entry child = getChild(childIndex);

      if(child.isDirectory()) {
        output += "\ndir ";
      } else {
        output += "\nfile ";
      }

      output += TinyDOS.AddPostPadding(child.getName(), 8, " ") + " ";

      output += TinyDOS.AddPrePadding(String.valueOf(child.getSize()), 4, "0");
    }


    return output;
  }
}
