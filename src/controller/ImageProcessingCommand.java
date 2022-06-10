package controller;

import model.ImageProcessingModel;

/**
 * Manipulations that we make on/with an image are represented as commands to the Image Processing
 * model.
 */
public interface ImageProcessingCommand {
  /**
   * Run this command on the Image Processing Model.
   *
   * @param model the model that we run commands on
   * @throws IllegalArgumentException if arguments passed to the command are invalid, like invalid
   *                                  file paths, image-names, and types instead (for brightness).
   */
  void run(ImageProcessingModel model) throws IllegalArgumentException;
}