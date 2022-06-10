package model;

/**
 * The image that the model returns so that the user cannot cast the Image from an ImageState to a
 * ImageTransform (where the transform method can be called on it).
 */
public class ObservableImage implements ImageState {


  @Override
  public int getMaxValue() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public RGBColor getColorAt(int row, int col) throws IllegalArgumentException {
    return null;
  }
}
