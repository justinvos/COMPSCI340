import java.util.ArrayList;

/**
 * LogicalDirectory
 *
 * Name: Justin Vos
 * ID: 6914129
 * UPI: jvos137
 *
 * @author      Justin Vos
 */
public class LogicalDirectory {
  private PhysicalDirectory parent;

  private Entry directoryEntry;

  public LogicalDirectory(Entry directoryEntry) {
    this.directoryEntry = directoryEntry;
  }

  private ArrayList<Entry> getChildren() {
    ArrayList<Entry> children = new ArrayList<Entry>();

    for(int addressIndex = 0; addressIndex < Entry.NUM_ADDRESSES; addressIndex++) {
      int address = directoryEntry.getAddress(addressIndex);
      if(address != 0) {
        PhysicalDirectory physicalDirectory = new PhysicalDirectory(Test.volume.getBlock(address));

        for(int index = 0; index < physicalDirectory.length(); index++) {
          Entry childEntry = physicalDirectory.get(index);
          if(!childEntry.getName().equals("")) {
            children.add(childEntry);
          }
        }
      }
    }

    return children;
  }

  public Entry get(int childIndex) {
    return getChildren().get(childIndex);
  }

  public int length() {
    return getChildren().size();
  }

  public Entry findEmptyEntry() {
    return directoryEntry.findEmptyEntry();
  }

  public void add(String name) {
    Entry entry = findEmptyEntry();

    System.out.println(entry.getParent());

    entry.setName(name);

    System.out.println("ADD:" + entry.getParent().getAddress());
    System.out.println(entry.getParent());


  }




  public PhysicalDirectory getParent() {
    return parent;
  }
}
