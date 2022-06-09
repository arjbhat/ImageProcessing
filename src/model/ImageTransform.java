package model;

/**
 * The image that allows that creation of other images like it
 * (where the original image has undergone some change).
 */
public interface ImageTransform extends ImageState {

  /**
   * To transform the image we currently have into a new image.
   *
   * @param map the function that we wish to change this image with
   * @return a new image with the necessary changes
   */
  Image transform(ColorFunction map) throws IllegalArgumentException;
}