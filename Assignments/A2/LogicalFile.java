public class LogicalFile {
  private EntryFile entry;
  private String content;

  public LogicalFile(EntryFile entry) {
    this.entry = entry;

    content = "";

    for(int slot = 0; slot < entry.length(); slot++) {
      BlockData block = entry.get(slot);
      if(block != null && !block.isEmpty()) {
        content += block.toString();
      }
    }
  }

  public int size() {
    return entry.size();
  }

  public int length() {
    return content.length();
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;

    int numberOfChunks = (int)Math.ceil(length() / (double)Block.MAX_LENGTH);
    String[] chunks = new String[numberOfChunks];
    for(int slot = 0; slot < entry.length(); slot++) {
      BlockData block = entry.get(slot);

      if(slot < chunks.length) {
        int chunkIndex = slot;
        chunks[chunkIndex] = getContent().substring(chunkIndex * Block.MAX_LENGTH, Math.min(length(), (chunkIndex + 1) * Block.MAX_LENGTH));


        if(block != null && !block.isEmpty()) {
          block.setData(chunks[chunkIndex]);
        } else {
          block = new BlockData(TinyDOS.volume.getEmptyBlock(), chunks[chunkIndex]);
          entry.set(chunkIndex, block);
          entry.getParent().write();
        }
      } else {
        if(block != null) {
          block.setData(Block.EMPTY);
          entry.set(slot, null);
          entry.getParent().write();
        }
      }
    }
    entry.setSize(size());
    entry.getParent().write();
  }

  public void appendContent(String content) {
    setContent(getContent() + content);
  }

  public void delete() {
    setContent("");
    entry.getParent().remove(entry.getIndex());
    //entry.getParent().set(entry.getIndex(), new EntryFile(entry.getParent(), entry.getIndex()));
    entry.getParent().write();
  }

  @Override
  public String toString() {
    return content;
  }
}
