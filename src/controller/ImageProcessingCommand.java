package controller;

import model.ImageProcessingModel;

/**
 * Manipulations that we make on/with an image are represented as commands
 */
public interface ImageProcessingCommand {
  void run(ImageProcessingModel model);
}