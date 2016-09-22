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

  public int size() {
    return entry.size();
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

  public EntryFile makeFile(String name) {
    if(hasChild(name)) {
      return null;
    }

    for(int slot = 0; slot < entry.length(); slot++) {
      BlockDirectory block = entry.get(slot);
      int index = 0;
      if(block == null) {
        block = new BlockDirectory(TinyDOS.volume.getEmptyBlock());
        entry.set(slot, block);
        entry.getParent().write();
      } else {
        index = block.findEmpty();
      }

      if(index != -1) {
        EntryFile file = new EntryFile(block, index);
        file.setName(name);
        file.getParent().set(file.getIndex(), file);

        children.add(file);

        file.getParent().write();
        return file;
      }
    }
    return null;
  }

  public EntryDirectory makeDirectory(String name) {
    if(hasChild(name)){
      return null;
    }

    for(int slot = 0; slot < entry.length(); slot++) {
      BlockDirectory block = entry.get(slot);
      int index = 0;
      if(block == null) {
        block = new BlockDirectory(TinyDOS.volume.getEmptyBlock());
        entry.set(slot, block);
        entry.getParent().write();
      } else {
        index = block.findEmpty();
      }

      if(index != -1) {
        EntryDirectory directory = new EntryDirectory(block, index);
        directory.setName(name);

        directory.getParent().set(directory.getIndex(), directory);

        children.add(directory);

        directory.getParent().write();
        return directory;
      }
    }
    return null;
  }

  public void delete() {
    if(length() == 0) {
      for(int slot = 0; slot < entry.length(); slot++) {
        entry.remove(slot);
      }
      entry.getParent().remove(entry.getIndex());
      entry.getParent().write();
    }
  }

  @Override
  public String toString() {
    String output = "TYPE NAME     SIZE\n------------------";

    for(int childIndex = 0; childIndex < length(); childIndex++) {
      Entry child = getChild(childIndex);

      if(child.isDirectory()) {
        output += "\ndir  ";
      } else {
        output += "\nfile ";
      }

      output += TinyDOS.AddPostPadding(child.getName(), 8, " ") + " ";

      output += TinyDOS.AddPrePadding(String.valueOf(child.getSize()), 4, "0");
    }


    return output;
  }
}
