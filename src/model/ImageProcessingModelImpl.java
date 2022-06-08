package model;

import java.util.HashMap;
import java.util.Map;

import model.macros.Macro;

/**
 * An implementation of the ImageProcessingModel
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  Map<String, Image> images;

  public ImageProcessingModelImpl() {
    images = new HashMap<>();
  }

  @Override
  public void createImage(int[][][] colors, String name) {
    images.put(name, new Image(colors));
  }

  @Override
  public void runCommand(Macro command, String target, String newName) {
    images.put(newName, command.execute(images.get(target)));
  }

  @Override
  public ImageState getImage(String name) {
    return images.getOrDefault(name, null);
  }
}