public class Test {

  public static Volume volume;

  public static void main(String[] args) {
    volume = new Volume("test3");
    volume.load();


    BlockVolumeInfo volumeInfoBlock = (BlockVolumeInfo)volume.readBlock(0);

    PhysicalDirectory root = new PhysicalDirectory(volumeInfoBlock, 128, 512);

    System.out.println(root);
    System.out.println();

    Entry entry = root.findEmptyEntry();
    //entry.setName("test");
    System.out.println(entry);
    System.out.println();

    System.out.println(root);
  }
}
