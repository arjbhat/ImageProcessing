package model;

import model.macros.Macro;

/**
 * Represents the model that contains, manages, and works on images.
 */
public interface ImageProcessingModel extends ImageProcessingState {
  /**
   * Creates an image with the Color array, the name provided and the max value - placing it
   * in the map that the Model uses.
   *
   * @param colors   the color array that the image is composed of
   * @param name     the name of the image
   * @param maxValue the max value for the channels
   * @throws IllegalArgumentException if we are trying to construct an invalid image
   *                                  (null Color array or maxValue below 1)
   */
  void createImage(Color[][] colors, String name, int maxValue) throws IllegalArgumentException;

  /**
   * Run this command on the image stored in the map with this target name and give the result a
   * new name.
   *
   * @param command the command that we want to run on the image
   * @param target  the name of the image
   * @param newName the new name of the image
   * @throws IllegalArgumentException if the target image does not exist or the command is null
   */
  void runCommand(Macro command, String target, String newName) throws IllegalArgumentException;
}
