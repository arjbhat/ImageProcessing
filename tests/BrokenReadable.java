import java.io.IOException;
import java.nio.CharBuffer;

public class BrokenReadable implements Readable {
  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException();
  }
}