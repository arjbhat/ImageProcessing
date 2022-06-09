package model;

/**
 * The state (width, height, colors) of the image that we want to look at (by its name).
 */
public interface ImageProcessingState {

  /**
   * Returns the state of the image that we want to look at.
   *
   * @param name the name of the image
   * @return the image state
   */
  ImageState getImage(String name) throws IllegalArgumentException;
}
