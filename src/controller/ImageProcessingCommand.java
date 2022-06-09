package controller;

import model.ImageProcessingModel;

/**
 * Manipulations that we make on/with an image are represented as commands to the model.
 */
public interface ImageProcessingCommand {
  /**
   * Run this command on the model.
   *
   * @param model
   */
  void run(ImageProcessingModel model) throws IllegalArgumentException;
}