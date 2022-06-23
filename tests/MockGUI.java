import java.awt.image.BufferedImage;
import java.io.IOException;

import view.Features;
import view.ImageProcessingGUI;

public class MockGUI implements ImageProcessingGUI {
  private final StringBuilder log;

  public MockGUI(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void addImage(String imageName) {
    log.append(String.format("method: addImage image-name: %s", imageName))
        .append(System.lineSeparator());
  }

  @Override
  public void selectImage(String imageName, BufferedImage img) {
    log.append(String.format("method: selectImage image-name: %s image: %dx%d",
            imageName, img.getWidth(), img.getHeight()))
        .append(System.lineSeparator());
  }

  @Override
  public void displayError(String err) {
    log.append(String.format("method: displayError err: %s", err))
        .append(System.lineSeparator());
  }

  @Override
  public void addFeatures(Features f) {
    log.append("method: addFeatures").append(System.lineSeparator());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    // do nothing, we don't care
  }
}
