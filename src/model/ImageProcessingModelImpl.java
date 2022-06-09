package model;

import java.util.HashMap;
import java.util.Map;

import model.macros.Macro;

/**
 * An implementation of the ImageProcessingModel. It stores the images on a map and works with them.
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  Map<String, Image> images;

  public ImageProcessingModelImpl() {
    images = new HashMap<>();
  }

  @Override
  public void createImage(RGBColor[][] colors, String name, int maxValue) {
    images.put(name, new Image(colors, maxValue));
  }

  @Override
  public void runCommand(Macro command, String target, String newName) {
    if (!images.containsKey(target)) {
      throw new IllegalArgumentException("Unknown image");
    }
    images.put(newName, command.execute(images.get(target)));
  }

  @Override
  public ImageState getImage(String name) {
    if (!images.containsKey(name)) {
      throw new IllegalArgumentException("Unknown image");
    }
    return images.get(name);
  }
}