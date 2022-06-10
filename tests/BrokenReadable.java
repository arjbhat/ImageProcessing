import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Represents a readable that only throws IOExceptions.
 */
public class BrokenReadable implements Readable {
  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException();
  }
}
