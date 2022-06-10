import java.io.IOException;

public class BrokenAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("Unable to successfully transmit output");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("Unable to successfully transmit output");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("Unable to successfully transmit output");
  }
}