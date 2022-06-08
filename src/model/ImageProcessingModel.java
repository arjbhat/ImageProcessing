package model;

import model.macros.Macro;

/**
 * Represents the model that contains, manages, and works on images.
 */
public interface ImageProcessingModel extends ImageProcessingState {
  void createImage(int[][][] colors, String name);

  void runCommand(Macro command, String target, String newName);
}
