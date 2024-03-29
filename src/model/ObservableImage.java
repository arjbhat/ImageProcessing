package model;

/**
 * The image that the model returns so that the user cannot cast the Image from an ImageState to an
 * ImageTransform (where the transform method can be called on it).
 */
public class ObservableImage implements ImageState {
  private final ImageState state;

  /**
   * Construct an observable image using an image state.
   *
   * @param state the image state to observe with this.
   * @throws IllegalArgumentException if the given image state is null
   */
  public ObservableImage(ImageState state) throws IllegalArgumentException {
    if (state == null) {
      throw new IllegalArgumentException("Image cannot be null.");
    }
    this.state = state;
  }

  @Override
  public int getMaxValue() {
    return this.state.getMaxValue();
  }

  @Override
  public int getHeight() {
    return this.state.getHeight();
  }

  @Override
  public int getWidth() {
    return this.state.getWidth();
  }

  @Override
  public Color getColorAt(int row, int col) throws IllegalArgumentException {
    return this.state.getColorAt(row, col);
  }

  @Override
  public boolean equals(Object obj) {
    return this.state.equals(obj);
  }

  @Override
  public int hashCode() {
    return this.state.hashCode();
  }
}
