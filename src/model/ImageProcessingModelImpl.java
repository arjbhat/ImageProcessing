package model;

import java.util.HashMap;
import java.util.Map;

import model.macros.Macro;

/**
 * An implementation of the ImageProcessingModel. It stores the images on a map and works with them.
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  // INVARIANT: keys cannot be null
  // INVARIANT: values cannot be null
  private final Map<String, ImageTransform> images;

  /**
   * The constructor for the model. Start with no images saved.
   */
  public ImageProcessingModelImpl() {
    this.images = new HashMap<>();
  }

  @Override
  public void createImage(Color[][] colors, String name, int maxValue)
          throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("String name cannot be null.");
    }
    this.images.put(name, new Image(colors, maxValue));
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
    if (!this.images.containsKey(target)) {
      throw new IllegalArgumentException("Unknown image.");
    }
    ImageTransform result = command.execute(this.images.get(target));
    if (result == null) {
      throw new IllegalArgumentException("Macro did not return an image");
    }
    this.images.put(newName, result);
  }

  @Override
  public ImageState getImage(String name) {
    if (!this.images.containsKey(name)) {
      throw new IllegalArgumentException("Unknown image.");
    }
    if (name == null) {
      throw new IllegalArgumentException("String name cannot be null.");
    }
    return new ObservableImage(this.images.get(name));
  }
}
