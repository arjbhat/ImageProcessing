package view;

import java.io.IOException;

/**
 * Represents the view that sends output to the appendable. It is used by the controller.
 */
public interface ImageProcessingView {
  /**
   * Render a specific message to the provided data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  void renderMessage(String message) throws IOException;
}