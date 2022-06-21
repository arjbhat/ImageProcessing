package model;

import java.util.List;

/**
 * The state (width, height, colors) of the image that we want to look at (by its name).
 */
public interface ImageProcessingState {
  /**
   * Returns the state of the image that we want to look at.
   * (This allows us to access the max value of the image, the height, the width, and the colors
   * in the array - without being able to mutate anything)
   *
   * @param name the name of the image
   * @return the image state
   */
  ImageState getImage(String name) throws IllegalArgumentException;

  /**
   * Returns the names of the images currently stored on the model.
   *
   * @return the names of the images stored
   */
  List<String> getImageNames();
}
