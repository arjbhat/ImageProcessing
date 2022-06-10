package model;

import java.util.HashMap;
import java.util.Map;

import model.macros.Macro;

/**
 * An implementation of the ImageProcessingModel. It stores the images on a map and works with them.
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  private final Map<String, Image> images;

  /**
   * The constructor for the model.
   */
  public ImageProcessingModelImpl() {
    images = new HashMap<>();
  }

  @Override
  public void createImage(Color[][] colors, String name, int maxValue)
      throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("String name cannot be null.");
    }
    images.put(name, new Image(colors, maxValue));
  }

  @Override
  public void runCommand(Macro command, String target, String newName) {
    if (command == null) {
      throw new IllegalArgumentException("Macro cannot be null.");
    }
    if (target == null) {
      throw new IllegalArgumentException("String target cannot be null.");
    }
    if (newName == null) {
      throw new IllegalArgumentException("New String name cannot be null.");
    }
    if (!images.containsKey(target)) {
      throw new IllegalArgumentException("Unknown image.");
    }
    images.put(newName, command.execute(images.get(target)));
  }

  @Override
  public ImageState getImage(String name) {
    if (!images.containsKey(name)) {
      throw new IllegalArgumentException("Unknown image.");
    }
    if (name == null) {
      throw new IllegalArgumentException("String name cannot be null.");
    }
    return new ObservableImage(images.get(name));
  }
}
