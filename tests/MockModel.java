import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Color;
import model.ImageProcessingModel;
import model.ImageState;
import model.RGBColor;
import model.macros.Macro;

/**
 * A MockModel to test the Controller.
 */
public class MockModel implements ImageProcessingModel {
  private final StringBuilder log;
  private final ArrayList<Macro> macroLog;

  public MockModel(StringBuilder log, ArrayList<Macro> macroLog) {
    this.log = Objects.requireNonNull(log);
    this.macroLog = Objects.requireNonNull(macroLog);
  }

  @Override
  public void createImage(Color[][] colors, String name, int maxValue)
      throws IllegalArgumentException {
    log.append(String.format("method: createImage image-name: %s maxValue: %d", name, maxValue))
        .append("\n");
  }

  @Override
  public void runCommand(Macro command, String target, String newName)
      throws IllegalArgumentException {
    log.append(String.format("method: runCommand image-name: %s dest-image-name: %s",
            target, newName))
        .append("\n");
    macroLog.add(command);
  }

  @Override
  public ImageState getImage(String name) throws IllegalArgumentException {
    log.append(String.format("method: getImage image-name: %s", name))
        .append("\n");
    return new ImageState() {
      @Override
      public int getMaxValue() {
        return 255;
      }

      @Override
      public int getHeight() {
        return 1;
      }

      @Override
      public int getWidth() {
        return 1;
      }

      @Override
      public Color getColorAt(int row, int col) throws IllegalArgumentException {
        return new RGBColor(0, 0, 0);
      }
    };
  }

  @Override
  public List<String> getImageNames() {
    log.append("method: getImageNames");
    return null;
  }
}
