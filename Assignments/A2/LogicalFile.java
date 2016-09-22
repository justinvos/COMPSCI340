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
    for(int chunkIndex = 0; chunkIndex < chunks.length; chunkIndex++) {
      chunks[chunkIndex] = getContent().substring(chunkIndex * Block.MAX_LENGTH, Math.min(length(), (chunkIndex + 1) * Block.MAX_LENGTH));

      BlockData block = entry.get(chunkIndex);
      if(block != null && !block.isEmpty()) {
        block.setData(chunks[chunkIndex]);
      } else {
        block = new BlockData(TinyDOS.volume.getEmptyBlock(), chunks[chunkIndex]);
        entry.set(chunkIndex, block);
        entry.getParent().write();
      }
    }
  }

  public void appendContent(String content) {
    setContent(getContent() + content);
  }

  @Override
  public String toString() {
    return content;
  }
}
