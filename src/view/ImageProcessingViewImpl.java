package view;

import java.io.IOException;

import model.ImageProcessingState;

/**
 * Transmits the output to the appendable (destination).
 */
public class ImageProcessingViewImpl implements ImageProcessingView {
  private final Appendable destination;

  /**
   * Constructor for our Image Processing view.
   */
  public ImageProcessingViewImpl() throws IllegalArgumentException {
    this.destination = System.out;
  }

  /**
   * Second Constructor for our Image processing view that uses a given appendable object for its
   * destination.
   *
   * @param destination the appendable object that this view uses as its destination
   * @throws IllegalArgumentException If the appendable is of type null
   */
  public ImageProcessingViewImpl(Appendable destination)
          throws IllegalArgumentException {
    if (destination == null) {
      throw new IllegalArgumentException("Invalid appendable of type null.");
    }

    this.destination = destination;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    destination.append(message);
  }
}
