package model;

/**
 * The image that allows that creation of other images like it.
 * (where the original image has undergone some change)
 */
public interface ImageTransform extends ImageState {

  /**
   * To transform the image we currently have into a new image.
   *
   * @param map the function that we wish to change this image with
   * @return a new image with the necessary changes
   * @throws IllegalArgumentException if the new color we want to make has a channel above maximum
   *                                  value or below 0.
   */
  ImageTransform transform(ColorFunction map) throws IllegalArgumentException;
}
