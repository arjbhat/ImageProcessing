import java.io.IOException;
import java.util.Objects;

import view.ImageProcessingView;

/**
 * A MockView to test the Controller.
 */
public class MockView implements ImageProcessingView {
  private final StringBuilder log;

  MockView(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public void renderMessage(String message) throws IOException {
    log.append(String.format("method: renderMessage message: %s", message)).append("\n");
  }
}
