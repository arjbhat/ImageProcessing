package model;

import model.macros.Macro;

/**
 * Represents the model that contains, manages, and works on images.
 */
public interface ImageProcessingModel extends ImageProcessingState {

  void createImage(RGBColor[][] colors, String name, int maxValue) throws IllegalArgumentException;

  void runCommand(Macro command, String target, String newName) throws IllegalArgumentException;
}